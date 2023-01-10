package com.oasis.cac.vas.dto;

import com.oasis.cac.vas.utils.oasisenum.GenericStatusConstant;
import com.oasis.cac.vas.utils.oasisenum.PortalAccountTypeConstant;

public class AdminPortalDto {

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String otherName;
    private String[] roles;
    private boolean isEmailVerified;
    private String companyName;
    private GenericStatusConstant userStatus;
    private PortalAccountTypeConstant portalAccountTypeConstant;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public PortalAccountTypeConstant getPortalAccountTypeConstant() {
        return portalAccountTypeConstant;
    }

    public void setPortalAccountTypeConstant(PortalAccountTypeConstant portalAccountTypeConstant) {
        this.portalAccountTypeConstant = portalAccountTypeConstant;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public GenericStatusConstant getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(GenericStatusConstant userStatus) {
        this.userStatus = userStatus;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
