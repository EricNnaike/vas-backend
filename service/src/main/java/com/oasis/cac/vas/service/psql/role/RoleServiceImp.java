package com.oasis.cac.vas.service.psql.role;

import com.google.gson.Gson;
import com.oasis.cac.vas.dao.psql.RolesDao;
import com.oasis.cac.vas.dto.RolesDto;
import com.oasis.cac.vas.models.Privilege;
import com.oasis.cac.vas.models.Role;
import com.oasis.cac.vas.service.psql.privileges.PrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.oasis.cac.vas.utils.oasisenum.RoleTypeConstant;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class RoleServiceImp implements RolesService {

    private static Logger logger = Logger.getLogger(RoleServiceImp.class.getSimpleName());


    private Gson gson;

    @Autowired
    private RolesDao rolesDao;

//    @Autowired
//    private PortalAccountAndPortalUserRoleMapperService portalAccountAndPortalUserRoleMapperService;

    @Autowired
    private PrivilegeService privilegeService;


    @Autowired
    public RoleServiceImp() {
       this.gson = new Gson();
    }

    @Override
    @Transactional
    public Role save(RolesDto rolesDto) {

        Role role = new Role();
        role.setRoleName(rolesDto.getName());
        role.setRoleType(RoleTypeConstant.valueOf(rolesDto.getRoleType()));
        rolesDto.getPrivileges().forEach(privilegeDto -> {
            logger.info(privilegeDto.getName());
            Privilege privilege = this.privilegeService.findName(privilegeDto.getName());
            role.getPrivileges().add(privilege);
        });

        this.rolesDao.save(role);
        return role;
    }

    @Override
    public List<Role> findAll() {
        return this.rolesDao.findAll();
    }

    @Override
    public Role findByRoleType(RoleTypeConstant roleType) {
        return rolesDao.findByRoleType(roleType);
    }

    @Override
    public Role findById(Long id) {
        Optional<Role> roleOptional = rolesDao.findById(id);
        return roleOptional.orElse(null);
    }

    @Override
    public List<Role> findRoleByPortalAccountAndPortalUser(String portalAccountCode, String portalUserCode) {

//        List<Role> roles = new ArrayList<>();
//        List<PortalAccountAndPortalUserRoleMappper> portalAccountAndPortalUserRoleMapppers =
//                this.portalAccountAndPortalUserRoleMapperService.findByPortalAccountCodeAndPortalUserCode(portalAccountCode, portalUserCode);
//
//        portalAccountAndPortalUserRoleMapppers.forEach(portalAccountAndPortalUserRoleMappper -> {
//            Optional<Role> roleOptional = this.rolesDao.findById(portalAccountAndPortalUserRoleMappper.getRoleId());
//            if(roleOptional.isPresent()){
//                roles.add(roleOptional.get());
//            }
//        });

        return new ArrayList<>();
    }
}
