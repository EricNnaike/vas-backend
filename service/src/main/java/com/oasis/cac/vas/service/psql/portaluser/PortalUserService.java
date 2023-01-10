package com.oasis.cac.vas.service.psql.portaluser;

import com.oasis.cac.vas.dto.AdminPortalDto;
import com.oasis.cac.vas.dto.LoginDto;
import com.oasis.cac.vas.dto.PortalUserSignUpDto;
import com.oasis.cac.vas.models.PortalUser;

import java.util.List;

public interface PortalUserService {

    List<PortalUser> findAll();

    PortalUser findPortalUserByEmail(String email);

    PortalUser findPortalUserById(Long id);

    PortalUser findByCode(String code);

    PortalUser findByPortalUserCode(String code);

    String getUniqueId();

    Long count();

    void create(AdminPortalDto adminPortalDto);

    void createViaSocial(PortalUserSignUpDto signUpDto);

    PortalUser updateUserProfile(Object portalUserDto);
    PortalUser loginPortalUser(LoginDto loginDto);
}
