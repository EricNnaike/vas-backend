package com.oasis.cac.vas.pojo;

import java.util.Date;

public class UserResponsePojo {

    private Long id;

    private String code;

    private String email;

    private String firstName;

    private String lastName;

    private String otherName;

    private boolean isEmailVerified;

    private Long portalAccountId;

    private String portalAccountName;

    private String portalAccountCode;

    private String[] roles;

    private Date dateCreated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Long getPortalAccountId() {
        return portalAccountId;
    }

    public void setPortalAccountId(Long portalAccountId) {
        this.portalAccountId = portalAccountId;
    }

    public String getPortalAccountName() {
        return portalAccountName;
    }

    public void setPortalAccountName(String portalAccountName) {
        this.portalAccountName = portalAccountName;
    }

    public String getPortalAccountCode() {
        return portalAccountCode;
    }

    public void setPortalAccountCode(String portalAccountCode) {
        this.portalAccountCode = portalAccountCode;
    }
}
