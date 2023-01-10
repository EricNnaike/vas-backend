package com.oasis.cac.vas.auth.config;

import com.oasis.cac.vas.auth.transformation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static com.oasis.cac.vas.auth.transformation.Transformer.*;

@Component
public class StartUpActions {

    private static final Logger logger = LoggerFactory.getLogger(StartUpActions.class);

    @Autowired
    private CreatePrivilegesJsonTransform createPrivilegesJsonTransform;

    @Autowired
    private CreatePortalUserRolesJsonTransform createPortalUserRolesJsonTransform;

    @Autowired
    private CreateAdminPortalUserJsonTransformer createAdminPortalUserJsonTransformer;

    @Autowired
    private CreatePaymentMethodsJsonTransform createPaymentMethodsJsonTransform;

    @PostConstruct()
    public void initialize() {

        //Adding privileges
        try {
            createPrivilegesJsonTransform.createPrivileges();
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            createPortalUserRolesJsonTransform.createUserRoles();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            createAdminPortalUserJsonTransformer.createPortalUser(ADMIN_GENERAL_USER_FILE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            createAdminPortalUserJsonTransformer.createPortalUser(ADMIN_LICENSED_USER_FILE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            this.createPaymentMethodsJsonTransform.create(PAYMENT_METHODS_FILE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
