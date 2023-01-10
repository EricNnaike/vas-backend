package com.oasis.cac.vas.restfulapi.controllers.auth;

import com.oasis.cac.vas.auth.config.authentication_service.AuthenticationService;
import com.oasis.cac.vas.auth.config.dto.UserDetailsDto;
import com.oasis.cac.vas.auth.config.service.user_service.UserService;
import com.oasis.cac.vas.models.PortalAccount;
import com.oasis.cac.vas.models.Role;
import com.oasis.cac.vas.pojo.UserResponsePojo;
import com.oasis.cac.vas.utils.MessageUtil;
import com.oasis.cac.vas.utils.controllers.ProtectedBaseApiController;
import com.oasis.cac.vas.utils.errors.ApiError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ProtectedAuthenticationController extends ProtectedBaseApiController {

    private static final Logger logger = LoggerFactory.getLogger(ProtectedAuthenticationController.class);

    @Autowired
    private MessageUtil messageUtil;

    @Autowired
    private AuthenticationService authenticationService;


    @Autowired
    private UserService userService;

    @GetMapping("/auth/me")
    public ResponseEntity<?> me(HttpServletResponse response, HttpServletRequest request, Authentication authentication) {

        ApiError apiError = null;
        UserDetailsDto requestPrincipal = fetchUser(response, request, authentication);

        try {

            UserResponsePojo userResponsePojo = new UserResponsePojo();
            userResponsePojo.setId(requestPrincipal.getId());
            userResponsePojo.setEmail(requestPrincipal.getEmail());
            userResponsePojo.setCode(requestPrincipal.getCode());
            userResponsePojo.setFirstName(requestPrincipal.getFirstName());
            userResponsePojo.setLastName(requestPrincipal.getLastName());
            userResponsePojo.setOtherName(requestPrincipal.getOtherName());
            List<Role> roleList = requestPrincipal.getRoles();

            String[] rolesArray = new String[roleList.size()];
            int i = 0;
            for (Role role : roleList) {
                rolesArray[i] = role.getRoleName();
                i++;
            }
            userResponsePojo.setRoles(rolesArray);
            // logger.info(requestPrincipal.portalAccount.toString());
            PortalAccount portalAccount = requestPrincipal.getPortalAccount();
            userResponsePojo.setPortalAccountName(portalAccount.getName());
            userResponsePojo.setPortalAccountCode(portalAccount.getCode());
            userResponsePojo.setPortalAccountId(portalAccount.getId());
            userResponsePojo.setDateCreated(requestPrincipal.getDateCreated());

            apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK,
                    messageUtil.getMessage("user.login.successful", "en"),
                    true,
                    new ArrayList<>(),
                    userResponsePojo);

            return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());

        } catch (Exception e) {

            e.printStackTrace();

            apiError = new ApiError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED, messageUtil.getMessage("user.unauthorized", "en"),
                    false, new ArrayList<>(), null);
            return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
        }

    }

    private UserDetailsDto fetchUser(HttpServletResponse response, HttpServletRequest request, Authentication authentication) {

        UserDetailsDto requestPrincipal = null;

        requestPrincipal = userService.getPrincipal(response, request, authentication);

        if (requestPrincipal == null) {
            throw new RuntimeException();
        }

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            String password = ((UserDetails) principal).getPassword();

            authenticationService.setPrincipal(new User(
                    email,
                    password,
                    new ArrayList<>()
            ), response, request);

        } else {
            throw new RuntimeException();
        }

        return requestPrincipal;
    }

}
