package com.oasis.cac.vas.service.psql.pre_licensed_partner;


import com.google.gson.Gson;
import com.oasis.cac.vas.dao.psql.PreLicensedPartnerDao;
import com.oasis.cac.vas.dao.psql.PreLicensedPartnerVerificationTokenDao;
import com.oasis.cac.vas.dto.Base64ImageDto;
import com.oasis.cac.vas.dto.LicensedPartnerDto;
import com.oasis.cac.vas.dto.PreLicensedPartnerDto;
import com.oasis.cac.vas.dto.licensed_partner.LicensedPartnerRegistrationDto;
import com.oasis.cac.vas.models.EmailVerificationToken;
import com.oasis.cac.vas.models.PortalUser;
import com.oasis.cac.vas.models.PreLicensedPartner;
import com.oasis.cac.vas.models.PreLicensedPartnerVerificationToken;
import com.oasis.cac.vas.pojo.PreLicensedPartnerInnerResponsePojo;
import com.oasis.cac.vas.service.sequence.email_verification.EmailVerificationTokenSequence;
import com.oasis.cac.vas.utils.ResourceUtil;
import com.oasis.cac.vas.utils.oasisenum.GenericStatusConstant;
import org.apache.commons.lang3.RandomStringUtils;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class PreLicensedPartnerServiceImpl implements PreLicensedPartnerService {

    @Autowired
    private PreLicensedPartnerDao preLicensedPartnerDao;

    @Autowired
    private PreLicensedPartnerVerificationTokenDao preLicensedPartnerVerificationTokenDao;

    @Autowired
    private EmailVerificationTokenSequence emailVerificationTokenSequence;


    @Value("${emailValidForInMinutes}")
    private String emailValidForInMinutes;

    @Override
    public PreLicensedPartnerInnerResponsePojo save(PreLicensedPartnerDto preLicensedPartnerDto) {

        PreLicensedPartner preLicensedPartner = new PreLicensedPartner();
        preLicensedPartner.setEmail(preLicensedPartnerDto.getEmail());
        preLicensedPartner.setFullName(preLicensedPartnerDto.getFullName());
        preLicensedPartner = preLicensedPartnerDao.save(preLicensedPartner);

        PreLicensedPartnerVerificationToken preLicensedPartnerVerificationToken =
                new PreLicensedPartnerVerificationToken();

        preLicensedPartnerVerificationToken.setPreLicensedPartner(preLicensedPartner);

        String token = this.generateToken();
        preLicensedPartnerVerificationToken.setToken(token);

        Date after = addTimeToDate();

        preLicensedPartnerVerificationToken.setExpiryDate(after);

        preLicensedPartnerVerificationToken.setExpiryDate(after);

        this.preLicensedPartnerVerificationTokenDao.save(preLicensedPartnerVerificationToken);

        PreLicensedPartnerInnerResponsePojo preLicensedPartnerInnerResponsePojo = new PreLicensedPartnerInnerResponsePojo();
        preLicensedPartnerInnerResponsePojo.setCode(token);
        preLicensedPartnerInnerResponsePojo.setUserId(preLicensedPartner.getId());
        return preLicensedPartnerInnerResponsePojo;
    }

    @Override
    public Optional<PreLicensedPartner> findByEmail(String email) {
       PreLicensedPartner preLicensedPartner = this.preLicensedPartnerDao.findByEmail(email);
       if(preLicensedPartner != null) {
           return Optional.of(preLicensedPartner);
       }

       return Optional.empty();
    }

    @Override
    public Optional<PreLicensedPartner> findById(Long id) {
        return this.preLicensedPartnerDao.findById(id);
    }

    @Override
    public String generateToken() {
        String generatedString = RandomStringUtils.random(100, true, true);
        String token = generatedString  + "" + emailVerificationTokenSequence.getNextId();
        System.out.println(token);
        return token;
    }

    @Override
    public Date addTimeToDate() {
        Calendar date = Calendar.getInstance();
        long timeInSecs = date.getTimeInMillis();
        return new Date(timeInSecs + ((long) Integer.parseInt(emailValidForInMinutes) * 60 * 1000));
    }

    @Override
    public PreLicensedPartnerInnerResponsePojo regenerate(PreLicensedPartner preLicensedPartner) {

            PreLicensedPartnerVerificationToken preLicensedPartnerVerificationToken = new PreLicensedPartnerVerificationToken();

            preLicensedPartnerVerificationToken.setPreLicensedPartner(preLicensedPartner);

            String token = this.generateToken();
            preLicensedPartnerVerificationToken.setToken(token);


            Date after = addTimeToDate();

            preLicensedPartnerVerificationToken.setExpiryDate(after);

            this.preLicensedPartnerVerificationTokenDao.save(preLicensedPartnerVerificationToken);

            PreLicensedPartnerInnerResponsePojo preLicensedPartnerInnerResponsePojo = new PreLicensedPartnerInnerResponsePojo();
            preLicensedPartnerInnerResponsePojo.setUserId(preLicensedPartner.getId());
            preLicensedPartnerInnerResponsePojo.setCode(token);
            return preLicensedPartnerInnerResponsePojo;
    }

    @Override
    public boolean checkIfValid(PreLicensedPartner preLicensedPartner, String token) {

        Optional<PreLicensedPartnerVerificationToken> optionalPreLicensedPartnerVerificationToken =
                preLicensedPartnerVerificationTokenDao.findByPreLicensedPartnerAndTokenAndTokenExpired(preLicensedPartner, token, false);


        if(optionalPreLicensedPartnerVerificationToken.isPresent()) {
            PreLicensedPartnerVerificationToken preLicensedPartnerVerificationToken = optionalPreLicensedPartnerVerificationToken.get();

            if (preLicensedPartnerVerificationToken.getToken() == null) {
                return false;
            }

            Calendar cal = Calendar.getInstance();
            if ((preLicensedPartnerVerificationToken.getExpiryDate()
                    .getTime() - cal.getTime()
                    .getTime()) <= 0) {

                preLicensedPartnerVerificationToken.setTokenExpired(true);
                preLicensedPartnerVerificationTokenDao.save(preLicensedPartnerVerificationToken);
                return false;
            }

            return true;

        } else {

            return false;
        }
    }

    @Override
    public void updateForFormWizardRegistration(LicensedPartnerRegistrationDto licensedPartnerRegistrationDto,
                                                PreLicensedPartner preLicensedPartner,
                                                int formWizardStep) {

        String json = new Gson().toJson(licensedPartnerRegistrationDto);
        preLicensedPartner.setUserRegistrationInformation(json);
        preLicensedPartner.setCurrentFormWizardStep(formWizardStep);
        this.preLicensedPartnerDao.save(preLicensedPartner);
    }
}
