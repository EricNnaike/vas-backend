package com.oasis.cac.vas.test_service;

import com.oasis.cac.vas.dao.psql.PortalUserDao;
import com.oasis.cac.vas.models.PortalUser;
import com.oasis.cac.vas.service.psql.email_verification.EmailVerificationService;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailVerificationServiceImplTest {

//    @Autowired
//    private EmailVerificationService emailVerificationService;

    @Mock
    private PortalUserDao portalUserDao;

    private PortalUser portalUser;


    @BeforeEach
    void getPortalUser() {
        this.portalUser = portalUserDao.findByEmail("robinhojohn07@gmail.com");
    }

//    @Test
//    public void generateToken() {
//        this.emailVerificationService.generateToken(this.portalUser);
//    }
}
