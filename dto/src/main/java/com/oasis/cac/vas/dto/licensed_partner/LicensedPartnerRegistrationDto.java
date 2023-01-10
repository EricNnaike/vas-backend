package com.oasis.cac.vas.dto.licensed_partner;

import lombok.Data;

import java.util.Set;

@Data
public class LicensedPartnerRegistrationDto {

    private LicensedPartnerCompanyDetailsDto companyDetails;

    private LicensedPartnerRepresentativeDetails representativeDetails;

    private Set<LicensedPartnerFileUploadDto> fileUploadSet;

    private String paymentRef;

}
