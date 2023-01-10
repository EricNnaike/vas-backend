package com.oasis.cac.vas.dto;

import javax.validation.constraints.NotBlank;

public class LicensedPartnerDto {

    @NotBlank
    private String email;

    @NotBlank
    private String fullName;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
