package com.oasis.cac.vas.dto;

import com.oasis.cac.vas.utils.oasisenum.PortalAccountTypeConstant;

public class SignUpDto {

    private String email;
    private String password;
    private PortalAccountTypeConstant portalAccountTypeConstant;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public PortalAccountTypeConstant getPortalAccountTypeConstant() {
        return portalAccountTypeConstant;
    }

    public void setPortalAccountTypeConstant(PortalAccountTypeConstant portalAccountTypeConstant) {
        this.portalAccountTypeConstant = portalAccountTypeConstant;
    }
}
