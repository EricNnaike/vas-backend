package com.oasis.cac.vas.auth.config.service;

import com.oasis.cac.vas.auth.config.dto.UserDetailsDto;
import com.oasis.cac.vas.auth.config.service.user_service.UserService;
import com.oasis.cac.vas.dao.psql.RolesDao;
import com.oasis.cac.vas.models.PortalUser;
import com.oasis.cac.vas.models.Role;
import com.oasis.cac.vas.service.psql.portaluser.PortalUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;


@Service(value = "userService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = Logger.getLogger(UserDetailsServiceImpl.class.getSimpleName());

    @Autowired
    private PortalUserService portalUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private RolesDao rolesDao;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        PortalUser portalUser = portalUserService.findPortalUserByEmail(username.toLowerCase());
        List<Role> roles = new ArrayList<>();

        if (portalUser == null) {
            throw new UsernameNotFoundException(username);
        }

        Set<Role> roleSet = portalUser.getRoles();

        roleSet.forEach((Role role) -> {
            Optional<Role> roleOptional = rolesDao.findById(role.getId());
            roleOptional.ifPresent(roles::add);
        });

        return new UserDetailsDto(
                portalUser.getEmail(),
                portalUser.getPassword(),
                portalUser.isEmailVerified(),
                portalUser.isAccountNonLocked(),
                true,
                portalUser.isLockedOut(),
                userService.getAuthorities(roles)
        );
    }
}
