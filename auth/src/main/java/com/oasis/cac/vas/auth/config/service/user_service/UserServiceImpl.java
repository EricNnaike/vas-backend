package com.oasis.cac.vas.auth.config.service.user_service;

import com.oasis.cac.vas.auth.config.dto.UserAccountDescriptionDto;
import com.oasis.cac.vas.auth.config.dto.UserDetailsDto;
import com.oasis.cac.vas.dao.psql.PortalUserDao;
import com.oasis.cac.vas.dao.psql.RolesDao;
import com.oasis.cac.vas.models.PortalUser;
import com.oasis.cac.vas.models.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public class UserServiceImpl implements UserService {

    private static final Logger logger = Logger.getLogger(UserServiceImpl.class.getSimpleName());

    @Autowired
    private PortalUserDao portalUserDao;

    @Autowired
    private RolesDao roleDao;


    @Override
    public UserDetailsDto getPrincipal(HttpServletResponse res, HttpServletRequest request, Authentication authentication) {

        try {

            if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null) {
                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                PortalUser portalUser = null;
                List<Role> roles = new ArrayList<>();

                if (principal instanceof UserDetails) {

                    String username = ((UserDetails) principal).getUsername();

                    portalUser = portalUserDao.findByEmail(username.toLowerCase());

                    List<UserAccountDescriptionDto> userAccountDescriptionDtoList = new ArrayList<>();

                    if (portalUser != null) {

                        Set<Role> roleSet = portalUser.getRoles();
                        List<String> userRoles = new ArrayList<>();
                        roleSet.forEach((Role role) -> {
                            userRoles.add(role.getRoleName());
                        });

                        UserAccountDescriptionDto userAccountDescriptionDto = new UserAccountDescriptionDto();

                        userAccountDescriptionDto.setRoleNames(userRoles);

                        userAccountDescriptionDtoList.add(userAccountDescriptionDto);

                    }


                    UserDetailsDto userDetailsDto = new UserDetailsDto(((UserDetails) principal).getUsername(),
                            ((UserDetails) principal).getPassword(), true, true, true, true,
                            getAuthorities(roles));

                    assert portalUser != null;

                    userDetailsDto.setUserDetails(portalUser, userAccountDescriptionDtoList);

                    return userDetailsDto;
                }

            }

        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String fetchFormOfIdentification() {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return null;
        }

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {
        return roles.stream().map(role ->
                new SimpleGrantedAuthority(role.getRoleType().name())).collect(Collectors.toList());
    }
}
