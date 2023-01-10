package com.oasis.cac.vas.auth.transformation;

import com.google.gson.Gson;
import com.oasis.cac.vas.dto.AdminPortalDto;
import com.oasis.cac.vas.models.PortalUser;
import com.oasis.cac.vas.service.psql.portaluser.PortalUserService;
import com.oasis.cac.vas.utils.ResourceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import static com.oasis.cac.vas.auth.transformation.Transformer.*;


@Component
public class CreateAdminPortalUserJsonTransformer {

    private static final Logger logger = Logger.getLogger(CreateAdminPortalUserJsonTransformer.class.getSimpleName());

    @Autowired
    private PortalUserService portalUserService;

    private final Gson gson;

    @Autowired
    public CreateAdminPortalUserJsonTransformer() {
        this.gson = new Gson();
    }

    public void createPortalUser(String fileName) {

        BufferedReader bufferedReader = null;

        String path =  TRANSFORMATION_DATA_FOLDER + File.separator + JSON_FOLDER + File.separator + fileName;

        InputStream inputStream = ResourceUtil.getResourceAsStream(path);

        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        AdminPortalDto adminPortalDto = gson.fromJson(bufferedReader, AdminPortalDto.class);

        PortalUser portalUser = this.portalUserService.findPortalUserByEmail(adminPortalDto.getEmail());
        if(portalUser != null) {
            logger.info(path + " admin account already exists");
        } else {
            this.portalUserService.create(adminPortalDto);
            logger.info(path + " added admin account");
        }
    }
}
