package com.oasis.cac.vas.auth.config;

import com.oasis.cac.vas.auth.config.authentication_token_service.AuthenticationTokenService;
import com.oasis.cac.vas.auth.config.service.user_service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LogoutHandler extends SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler {


    @Autowired
    private AuthenticationTokenService authenticationTokenService;


    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(LogoutHandler.class);


    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        authenticationTokenService.clearUserToken(response, request);
    }

}
