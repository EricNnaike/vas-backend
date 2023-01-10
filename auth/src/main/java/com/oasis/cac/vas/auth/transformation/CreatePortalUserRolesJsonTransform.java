package com.oasis.cac.vas.auth.transformation;

import com.google.gson.Gson;
import com.oasis.cac.vas.dto.RolesDto;
import com.oasis.cac.vas.models.Role;
import com.oasis.cac.vas.service.psql.role.RolesService;
import com.oasis.cac.vas.utils.ResourceUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;
import com.oasis.cac.vas.utils.oasisenum.RoleTypeConstant;

import static com.oasis.cac.vas.auth.transformation.Transformer.*;


@Component
public class CreatePortalUserRolesJsonTransform {

    private static final Logger logger = Logger.getLogger(CreatePortalUserRolesJsonTransform.class.getSimpleName());

    @Autowired
    private RolesService rolesService;

    private final Gson gson;

    @Autowired
    public CreatePortalUserRolesJsonTransform(){
        this.gson = new Gson();
    }

    public void createUserRoles() {

        BufferedReader bufferedReader = null;

        InputStream inputStream = ResourceUtil.getResourceAsStream(TRANSFORMATION_DATA_FOLDER + File.separator + JSON_FOLDER + File.separator + USER_ROLES_FILE_NAME);

        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        RolesDto[] rolesDtos = gson.fromJson(bufferedReader, RolesDto[].class);

        logger.info("Adding role types");

        for (RolesDto dto : rolesDtos) {

            Role role = null;

            if (StringUtils.isNotBlank(dto.getRoleType())) {

                role = rolesService.findByRoleType(RoleTypeConstant.valueOf(dto.getRoleType()));
                if (role == null) {
                    logger.info("role types :" + dto.getRoleType());
                    RolesDto rolesDto = new RolesDto();
                    rolesDto.setName(dto.getName());
                    rolesDto.setRoleType(dto.getRoleType());
                    rolesDto.setPrivileges(dto.getPrivileges());
                    this.rolesService.save(rolesDto);
                } else {
                    logger.info("Role types " + RoleTypeConstant.valueOf(dto.getRoleType()) + " already exist");
                }
            }
        }

        logger.info("Done adding role types");

    }
}
