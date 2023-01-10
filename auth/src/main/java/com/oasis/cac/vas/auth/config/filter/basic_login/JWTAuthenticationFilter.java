package com.oasis.cac.vas.auth.config.filter.basic_login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.oasis.cac.vas.auth.config.authentication_token_service.AuthenticationTokenService;
import com.oasis.cac.vas.dto.LoginDto;
import com.oasis.cac.vas.models.PortalUser;
import com.oasis.cac.vas.service.psql.portaluser.PortalUserService;
import com.oasis.cac.vas.utils.MessageUtil;
import com.oasis.cac.vas.utils.errors.ApiError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

   private static final Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

   @Autowired
   private AuthenticationTokenService authenticationTokenService;

   private String loginEmail;

   @Autowired
   private MessageUtil messageUtil;

   private final Gson gson;

   @Autowired
   private PortalUserService portalUserService;

   public JWTAuthenticationFilter() {
       this.gson = new Gson();
   }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {

            LoginDto loginDto = new ObjectMapper().readValue(req.getInputStream(), LoginDto.class);
            String email = loginDto.getEmail().replace(" ", "");
            this.loginEmail = email;
            String password = loginDto.getPassword().replace(" ", "");

            Authentication authentication =  super.getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(
                            email,
                            password,
                            new ArrayList<>())
            );

            PortalUser portalUser = portalUserService.findPortalUserByEmail(email.toLowerCase());

             if (authentication.isAuthenticated()) {

                 return authentication;

            } else {
                String message = "Bad credentials";
                throw new BadCredentialsException(message);
            }



        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private UsernamePasswordAuthenticationToken getAuthRequest(
            HttpServletRequest request) {

        String username = obtainUsername(request);
        String password = obtainPassword(request);

//        String usernameDomain = String.format("%s%s%s", username.trim(),
//                String.valueOf(Character.LINE_SEPARATOR), domain);
        return new UsernamePasswordAuthenticationToken(
                username, password);
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

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        ApiError apiError = null;
        PortalUser portalUser = null;
        portalUser = portalUserService.findPortalUserByEmail(loginEmail.toLowerCase());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        if (failed.getMessage().equalsIgnoreCase("User is disabled")) {
            apiError = new ApiError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED, messageUtil.getMessage("auth.user.is.deactivated", "en"),
                    false, new ArrayList<>(), null);
        } else if (failed.getMessage().equalsIgnoreCase("User account has expired")) {

            apiError = new ApiError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED, messageUtil.getMessage("auth.message.expired", "en"),
                    false, new ArrayList<>(), null);
        } else if (failed.getMessage().equalsIgnoreCase("Bad credentials")) {

            apiError = new ApiError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED, messageUtil.getMessage("auth.message.bad.credentials", "en"),
                    false, new ArrayList<>(), null);

        } else if(failed.getMessage().equalsIgnoreCase("blocked")) {
            apiError = new ApiError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED, messageUtil.getMessage("auth.message.locked.out.credentials", "en"),
                    false, new ArrayList<>(), null);
        }

        response.getWriter().print(this.gson.toJson(apiError));
        response.getWriter().flush();
    }
}
