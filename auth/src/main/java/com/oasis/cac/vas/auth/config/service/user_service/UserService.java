package com.oasis.cac.vas.auth.config.service.user_service;

import com.oasis.cac.vas.auth.config.dto.UserDetailsDto;
import com.oasis.cac.vas.models.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

public interface UserService {

    UserDetailsDto getPrincipal(HttpServletResponse res, HttpServletRequest request, Authentication authentication);

    String fetchFormOfIdentification();

    Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles);
}
