package com.oasis.cac.vas.dto.licensed_partner;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class LicensedPartnerRepresentativeDetails {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotNull
    private SelectedPhoneNumberDto selectedPhoneNumber;

    @NotBlank
    private String gender;

    @NotBlank
    private String email;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String address;

    @NotBlank
    private String nationality;

    @NotBlank
    private String state;

    @NotBlank
    private Date dob;

}
