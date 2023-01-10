package com.oasis.cac.vas.service.psql.role;


import com.oasis.cac.vas.dto.RolesDto;
import com.oasis.cac.vas.models.Role;
import com.oasis.cac.vas.utils.oasisenum.RoleTypeConstant;

import java.util.List;

public interface RolesService {

    Role save(RolesDto rolesDto);

    List<Role> findAll();

    Role findByRoleType(RoleTypeConstant roleType);

    Role findById(Long id);

    List<Role> findRoleByPortalAccountAndPortalUser(String portalAccountCode, String portalUserCode);

}
