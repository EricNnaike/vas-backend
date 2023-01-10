package com.oasis.cac.vas.dto.licensed_partner;

import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;


@Data
public class LicensedPartnerCompanyDetailsDto {

    @NotBlank
    private String companyName;

    @NotBlank
    private String companyType;

    @NotBlank
    private String registrationNumber;

    @NotBlank
    private String companyEmail;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String countryOfResidence;

    @NotBlank
    private String state;

    @NotBlank
    private Date dateOfIncorporation;

    @Email
    @NotBlank
    private String username;

    @NotNull
    private SelectedPhoneNumberDto selectedPhoneNumber;

    @NotBlank
    private String password;

    @NotBlank
    private String confirmPassword;

    @NotBlank
    private String officeAddress;

    @Email
    @NotBlank
    private String email;
}
