package com.oasis.cac.vas.auth.config.filter.social_login.facebook;

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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class FacebookOAuth2AuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String FACEBOOK_CLIENT_ID = "350492232194631";
    private static final Logger logger = LoggerFactory.getLogger(FacebookOAuth2AuthenticationFilter.class);


    @Autowired
    private AuthenticationTokenService authenticationTokenService;


    @Autowired
    private PortalUserService portalUserService;

    public FacebookOAuth2AuthenticationFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        try {

            SecurityContext context = SecurityContextHolder.getContext();

            Gson gson = new Gson();
            InputStream in = request.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            AngularSSODto facebookOauthDto = gson.fromJson(new JsonReader(reader), AngularSSODto.class);
            //logger.info(gson.toJson(facebookOauthDto));



            //logger.info(gson.toJson(request.getInputStream().readLine()));

            // logger.info(facebookOauthDto.getToken());

            if (StringUtils.isNotBlank(facebookOauthDto.getAuthToken())) {

                String fbGraph = this.getFBGraph(facebookOauthDto.getAuthToken());
                AngularSSODto fbProfile = this.getGraphData(fbGraph);

                if(StringUtils.isNotBlank(fbGraph)) {

                    return this.getAuthentication(facebookOauthDto, context);

                } else {
                    return null;
                }

            } else {

                return null;
            }

        } catch (IOException e) {
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
    public void setAuthenticationManager(AuthenticationManager authenticationManager){
        super.setAuthenticationManager(authenticationManager);
    }



    private String getFBGraph(String accessToken) {
        String graph = null;
        try {

            String g = "https://graph.facebook.com/me?access_token=" + accessToken;
            URL u = new URL(g);
            URLConnection c = u.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    c.getInputStream()));
            String inputLine;
            StringBuffer b = new StringBuffer();
            while ((inputLine = in.readLine()) != null)
                b.append(inputLine + "\n");
            in.close();
            graph = b.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("ERROR in getting FB graph data. " + e);
        }
        return graph;
    }



    private AngularSSODto getGraphData(String fbGraph) {
        Gson gson = new Gson();
        return gson.fromJson(fbGraph, AngularSSODto.class);
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


    private Authentication getAuthentication(AngularSSODto facebookOauthDto, SecurityContext context) {

        String email = facebookOauthDto.getEmail();

        //logger.info("email: "+email);

        if(StringUtils.isNotBlank(email)) {

            PortalUser portalUser = null;
            PortalAccount portalAccount = null;
            portalUser = portalUserService.findPortalUserByEmail(email.toLowerCase());

            if (portalUser == null) {
                throw new UsernameNotFoundException("message.socialBadEmail");
            }

            portalAccount = portalUser.getPortalAccount();

            UserDetailsDto userDetailsDto = new UserDetailsDto(portalUser.getEmail(), "",
                    portalUser.isEmailVerified(), true,
                    true, portalUser.isLockedOut(), new ArrayList<>());


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
            return  null;
        }
    }
}
