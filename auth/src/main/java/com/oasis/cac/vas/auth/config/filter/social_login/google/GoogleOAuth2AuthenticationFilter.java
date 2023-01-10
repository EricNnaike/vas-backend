package com.oasis.cac.vas.auth.config.filter.social_login.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.oasis.cac.vas.auth.config.authentication_token_service.AuthenticationTokenService;
import com.oasis.cac.vas.auth.config.dto.AngularSSODto;
import com.oasis.cac.vas.auth.config.dto.UserAccountDescriptionDto;
import com.oasis.cac.vas.auth.config.dto.UserDetailsDto;
import com.oasis.cac.vas.models.PortalAccount;
import com.oasis.cac.vas.models.PortalUser;
import com.oasis.cac.vas.models.Role;
import com.oasis.cac.vas.service.psql.portaluser.PortalUserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class GoogleOAuth2AuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final JacksonFactory jacksonFactory = new JacksonFactory();
    private static final HttpTransport transport = new NetHttpTransport();
    private static final Logger logger = LoggerFactory.getLogger(GoogleOAuth2AuthenticationFilter.class);

    @Autowired
    private AuthenticationTokenService authenticationTokenService;

    @Autowired
    private Gson gson;

    @Autowired
    private PortalUserService portalUserService;

    public GoogleOAuth2AuthenticationFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
        System.out.println(defaultFilterProcessesUrl);
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        try {

            SecurityContext context = SecurityContextHolder.getContext();
            InputStream in = request.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            AngularSSODto googleOauthDto = gson.fromJson(new JsonReader(reader), AngularSSODto.class);
            logger.info(gson.toJson(googleOauthDto));

            if (StringUtils.isNotBlank(googleOauthDto.getAuthToken())) {

                // vRRvP3rpeO1psMtuTBKmMh8V
                // @Value("${googleClientId}")
                String GOOGLE_CLIENT_ID = "554040801458-svoog7dgmuj1oarkudvtd409mgdr7fp2.apps.googleusercontent.com";
                GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jacksonFactory)
                        .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
                        .build();

                // (Receive idTokenString by HTTPS POST)

                String idTokenString = googleOauthDto.getAuthToken();

                //logger.info("id_token: " + googleOauthDto.getToken());

                GoogleIdToken idToken = null;
                try {
                    logger.info(idTokenString);
                    idToken = verifier.verify(idTokenString);
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                }


                if (idToken != null) {
                    GoogleIdToken.Payload payload = idToken.getPayload();

                    // Print user identifier
                    String userId = payload.getSubject();

                    // Get profile information from payload
                    String email = payload.getEmail();
                    PortalUser portalUser = null;
                    portalUser = portalUserService.findPortalUserByEmail(email);
                    PortalAccount portalAccount = null;

                    if (portalUser == null) {
                        throw new UsernameNotFoundException("message.socialBadEmail");
                    }


                    portalAccount = portalUser.getPortalAccount();


                    UserDetailsDto userDetailsDto = new UserDetailsDto(portalUser.getEmail(), "",
                            portalUser.isEmailVerified(), true,
                            true, !portalUser.isLockedOut(), new ArrayList<>());


                    List<UserAccountDescriptionDto> userAccountDescriptionDtoList = new ArrayList<>();

                    Set<Role> roleSet = portalUser.getRoles();
                    List<String> userRoles = new ArrayList<>();
                    roleSet.forEach((Role role) -> {
                        userRoles.add(role.getRoleName());
                    });

                    UserAccountDescriptionDto userAccountDescriptionDto = new UserAccountDescriptionDto();

                    userAccountDescriptionDto.setRoleNames(userRoles);

                    userAccountDescriptionDtoList.add(userAccountDescriptionDto);

                    userDetailsDto.setUserDetails(portalUser, userAccountDescriptionDtoList);

                    if (!userDetailsDto.isEnabled()) {
                        throw new UsernameNotFoundException("User is disabled");
                    }

                    Authentication authentication = new UsernamePasswordAuthenticationToken(userDetailsDto, null);
                    context.setAuthentication(authentication);
                    return authentication;

                } else {
                    System.out.println("Invalid ID token.");
                    return null;
                }

            }
            else {
                return null;
            }

        } catch (IOException e) {
            logger.info(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
        authenticationTokenService.sendTokenToUser((User) auth.getPrincipal(), res, req);
    }

    @Override
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        String errorMessage = messages.getMessage("message.socialBadEmail", (Object[]) null);
        if (failed.getMessage().equalsIgnoreCase("User is disabled")) {
            errorMessage = messages.getMessage("auth.user.is.deactivated", (Object[]) null);
        } else if (failed.getMessage().equalsIgnoreCase("User account has expired")) {
            errorMessage = messages.getMessage("auth.message.expired", (Object[]) null);
        }

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, errorMessage);
    }
}
