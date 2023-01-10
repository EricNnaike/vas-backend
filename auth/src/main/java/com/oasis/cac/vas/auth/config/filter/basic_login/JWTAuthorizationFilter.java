package com.oasis.cac.vas.auth.config.filter.basic_login;

import com.google.gson.Gson;
import com.oasis.cac.vas.auth.config.authentication_token_service.AuthenticationTokenService;
import com.oasis.cac.vas.auth.config.constants.SecurityConstants;
import com.oasis.cac.vas.auth.config.service.UserDetailsBean;
import com.oasis.cac.vas.auth.config.service.user_service.UserService;
import com.oasis.cac.vas.dao.psql.RolesDao;
import com.oasis.cac.vas.models.PortalUser;
import com.oasis.cac.vas.models.Role;
import com.oasis.cac.vas.service.psql.portaluser.PortalUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    @Value("${authToken}")
    private String authTokenName;

    @Autowired
    private AuthenticationTokenService authenticationTokenService;

    @Autowired
    private PortalUserService portalUserService;

    @Autowired
    private RolesDao roleDao;

    private Gson gson;

    @Autowired
    private UserService userService;


    public JWTAuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
        this.gson = new Gson();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {

        PortalUser portalUser = null;

        String token = request.getHeader(SecurityConstants.HEADER_STRING);

        if (token != null) {
            portalUser = authenticationTokenService.fetchUserFromRequestFormHeader(request);

            if (portalUser != null) {

                Collection<? extends GrantedAuthority> authorities = fetchAuthorities(portalUser);
                UserDetailsBean userDetailsBean = this.getUserDetails(portalUser, authorities);
                return new UsernamePasswordAuthenticationToken(userDetailsBean, null, new ArrayList<>());
            }
            return null;
        }

        portalUser = authenticationTokenService.fetchUserFromRequestFormCookie(request);

        if (portalUser != null) {

            Collection<? extends GrantedAuthority> authorities = fetchAuthorities(portalUser);
            UserDetailsBean userDetailsBean = this.getUserDetails(portalUser, authorities);
           return new UsernamePasswordAuthenticationToken(userDetailsBean, null, authorities);
        }

        return null;
    }

    private UserDetailsBean getUserDetails(PortalUser portalUser, Collection<? extends GrantedAuthority> authorities) {

        String value = portalUser.getEmail();

        return new UserDetailsBean(value, portalUser.getPassword(),
                portalUser.isEmailVerified(),
                portalUser.isAccountNonLocked(), portalUser.isLockedOut(),
                        true, authorities);
    }


    private Collection<? extends GrantedAuthority> fetchAuthorities(PortalUser foundPortalUser) {

        List<Role> roles = new ArrayList<>();

        PortalUser portalUser = this.portalUserService.findPortalUserById(foundPortalUser.getId());

        Set<Role> roleSet = portalUser.getRoles();

        roleSet.forEach((Role role) -> {
            roles.add(role);
        } );

       return this.userService.getAuthorities(roles);

    }

}