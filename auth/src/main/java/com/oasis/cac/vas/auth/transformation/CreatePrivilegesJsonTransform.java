package com.oasis.cac.vas.auth.transformation;


import com.google.gson.Gson;
import com.oasis.cac.vas.dto.PrivilegeDto;
import com.oasis.cac.vas.models.Privilege;
import com.oasis.cac.vas.service.psql.privileges.PrivilegeService;
import com.oasis.cac.vas.utils.ResourceUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import static com.oasis.cac.vas.auth.transformation.Transformer.*;


@Component
public class CreatePrivilegesJsonTransform {

    private static final Logger logger = Logger.getLogger(CreatePrivilegesJsonTransform.class.getSimpleName());

    @Autowired
    private PrivilegeService privilegeService;

    private Gson gson;

    public CreatePrivilegesJsonTransform() {
        this.gson = new Gson();
    }

    public void createPrivileges() {

        BufferedReader bufferedReader = null;

        InputStream inputStream = ResourceUtil.getResourceAsStream(TRANSFORMATION_DATA_FOLDER + File.separator + JSON_FOLDER + File.separator + PRIVILEGES);

        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        PrivilegeDto[] privilegeDtos = gson.fromJson(bufferedReader, PrivilegeDto[].class);

        logger.info("Adding privileges");

        for (PrivilegeDto dto : privilegeDtos) {

            Privilege privilege = null;

            if (StringUtils.isNotBlank(dto.getName())) {

                privilege = privilegeService.findName(dto.getName());

                if (privilege == null) {
                    PrivilegeDto privilegeDto = new PrivilegeDto();
                    privilegeDto.setName(dto.getName());
                    privilegeService.save(privilegeDto);

                } else {
                    logger.info(" privilege,  " + privilege.getName() + " already exist");
                }
            }
        }

        logger.info("Done adding privileges");

    }
}
