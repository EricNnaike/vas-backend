package com.oasis.cac.vas.auth.config.authentication_token_service;

import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.gson.Gson;
import com.oasis.cac.vas.models.PortalUser;
import com.oasis.cac.vas.service.psql.portaluser.PortalUserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.oasis.cac.vas.utils.ResourceUtil;

import static com.oasis.cac.vas.auth.config.constants.SecurityConstants.HEADER_STRING;
import static com.oasis.cac.vas.auth.config.constants.SecurityConstants.TOKEN_PREFIX;
import static org.apache.commons.io.IOUtils.toByteArray;

@Component
public class AuthenticationTokenServiceImpl implements AuthenticationTokenService {

    @Value("${tokenIssuer}")
    private String issuer;

    private Gson gson;

    private final Logger logger = LoggerFactory.getLogger(AuthenticationTokenServiceImpl.class);

    @Value("${authToken}")
    private String authTokenName;

    @Autowired
    private PortalUserService portalUserService;

    private PublicKey publicKey;

    private PrivateKey privateKey;

    public AuthenticationTokenServiceImpl() throws IllegalArgumentException {

        this.gson = new Gson();
        try {

            String publicPEM = "security" + File.separator + "rsa256-key-public.pem";
            String privatePEM = "security" + File.separator + "rsa256-key-private.pem";
            this.publicKey = getPemPublicKey(publicPEM, "RSA");
            this.privateKey = getPemPrivateKey(privatePEM, "RSA");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void sendTokenToUser(User user, HttpServletResponse res, HttpServletRequest request) {
        try {

            PortalUser portalUser = null;
            portalUser = portalUserService.findPortalUserByEmail(user.getUsername().toLowerCase());

            assert portalUser != null;
            String token = this.getTokenString(portalUser);
            res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
            Cookie cookie = new Cookie(authTokenName, token);
            cookie.setMaxAge(60 * 60 * 24 * 365);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            res.addCookie(cookie);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String generateUserToken(PortalUser portalUser) {
        return this.getTokenString(portalUser);
    }

    private String getTokenString(PortalUser portalUser) {
        final Instant now = Instant.now();
        String token = null;
        try {
            token = Jwts.builder()
                    .claim("id", portalUser.getId())
                    .setIssuer(issuer)
                    .setIssuedAt(Timestamp.from(now))
                    .setExpiration(Timestamp.from(now.plus(60 * 24 * 365, ChronoUnit.MINUTES)))
                    .signWith(SignatureAlgorithm.RS256, privateKey).compact();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return token;
    }

    @Override
    public void clearUserToken(HttpServletResponse res, HttpServletRequest request) {
        Cookie cookie = new Cookie(authTokenName, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        res.addCookie(cookie);
    }

    @Override
    public PortalUser fetchUserFromRequestFormCookie(HttpServletRequest request) {

        Claims claims = null;

        if (request.getCookies() == null) {
            return null;
        }
        String authToken = null;
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(authTokenName)) {
                authToken = cookie.getValue();
                break;
            }
        }
        if (authToken == null) {
            return null;
        }


        try {
            claims = verifyToken(authToken);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (claims != null) {

            try {
                return this.getPortalUser(claims);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        return null;
    }

    @Override
    public PortalUser fetchUserFromRequestFormHeader(HttpServletRequest request) {

        try {
            String authToken = request.getHeader(HEADER_STRING);

            //Check if it has bearer token
            String token = authToken;
            Pattern pattern = Pattern.compile("^Bearer ");
            Matcher matcher = pattern.matcher(authToken);
            if(matcher.find()) {
                String[] arr = authToken.split(" ");
                token = arr[1];
            }

            Claims claims = verifyToken(token);
            return this.getPortalUser(claims);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private PortalUser getPortalUser(Claims claims) {

        try {
            String id = claims.get("id").toString();
            if (id != null) {
                PortalUser portalUserDto = portalUserService.findPortalUserById(Long.valueOf(id));
                return portalUserDto;
            }
            return null;
        } catch (Exception e) {
           // e.printStackTrace();
            return null;
        }
    }

    private Claims verifyToken(String token) {
        Claims claims;
        try {

            claims = Jwts.parser().setSigningKey(this.publicKey).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    private PrivateKey getPemPrivateKey(String filename, String algorithm) throws Exception {
        String temp = this.getPemKeyValue(filename);
        assert temp != null;
        String privKeyPEM = temp.replace("-----BEGIN PRIVATE KEY-----\n", "");
        privKeyPEM = privKeyPEM.replace("-----END PRIVATE KEY-----", "");
        Base64 b64 = new Base64();
        byte[] decoded = b64.decode(privKeyPEM);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
        KeyFactory kf = KeyFactory.getInstance(algorithm);
        return kf.generatePrivate(spec);
    }

    private PublicKey getPemPublicKey(String filename, String algorithm) throws Exception {
        String temp = this.getPemKeyValue(filename);

        if(temp != null) {
            String publicKeyPEM = temp.replace("-----BEGIN PUBLIC KEY-----\n", "");
            publicKeyPEM = publicKeyPEM.replace("-----END PUBLIC KEY-----", "");
            Base64 b64 = new Base64();
            byte[] decoded = b64.decode(publicKeyPEM);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
            KeyFactory kf = KeyFactory.getInstance(algorithm);
            return kf.generatePublic(spec);
        }

        return null;
    }

    private String getPemKeyValue(String filename) {
        InputStream inputStream = ResourceUtil.getResourceAsStream(filename);
        byte[] keyBytes = null;
        try {

            keyBytes = toByteArray(inputStream);
            return new String(keyBytes);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
