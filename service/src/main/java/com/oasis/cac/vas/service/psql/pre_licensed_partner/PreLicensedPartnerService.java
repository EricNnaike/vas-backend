package com.oasis.cac.vas.service.psql.pre_licensed_partner;


import com.oasis.cac.vas.dto.PreLicensedPartnerDto;
import com.oasis.cac.vas.dto.licensed_partner.LicensedPartnerRegistrationDto;
import com.oasis.cac.vas.models.PreLicensedPartner;
import com.oasis.cac.vas.pojo.PreLicensedPartnerInnerResponsePojo;

import java.util.Date;
import java.util.Optional;

public interface PreLicensedPartnerService {

    PreLicensedPartnerInnerResponsePojo save(PreLicensedPartnerDto preLicensedPartnerDto);

    Optional<PreLicensedPartner> findByEmail(String email);

    Optional<PreLicensedPartner> findById(Long id);

    String generateToken();

    Date addTimeToDate();

    PreLicensedPartnerInnerResponsePojo regenerate(PreLicensedPartner preLicensedPartner);

    boolean checkIfValid(PreLicensedPartner preLicensedPartner, String code);

    void updateForFormWizardRegistration(LicensedPartnerRegistrationDto licensedPartnerRegistrationDto,
                                         PreLicensedPartner preLicensedPartner,
                                         int formWizardStep);

}
