package com.oasis.cac.vas.auth.config.dto;


import com.oasis.cac.vas.models.PortalAccount;
import com.oasis.cac.vas.models.PortalUser;
import com.oasis.cac.vas.models.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class UserDetailsDto extends User {

    public Long id;
    public String code;
    public String lastName;
    public String firstName;
    public String otherName;
    public String email;
    public boolean enabled;
    public boolean accountNonExpired = true;
    public boolean credentialsNonExpired = true;
    public boolean accountNonLocked = true;
    public PortalAccount portalAccount;
    public List<Role> roles;
    public Date dateCreated;
    public List<UserAccountDescriptionDto> userAccountDescriptionDtoList;


    public UserDetailsDto(String username, String password,
                          boolean enabled, boolean accountNonExpired,
                          boolean credentialsNonExpired, boolean accountNonLocked,
                          Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.enabled = enabled;
    }



    public void setUserDetails(PortalUser portalUser, List<UserAccountDescriptionDto> userAccountDescriptionDtoList) {
        this.id = portalUser.getId();
        this.lastName = portalUser.getLastName();
        this.firstName = portalUser.getFirstName();
        this.otherName = portalUser.getOtherName();
        this.accountNonLocked = portalUser.isLockedOut();
        this.enabled = portalUser.isEmailVerified();
        this.email = portalUser.getEmail();
        this.roles = new ArrayList<>(portalUser.getRoles());
        this.portalAccount = portalUser.getPortalAccount();
        this.userAccountDescriptionDtoList = userAccountDescriptionDtoList;
        this.code = portalUser.getCode();
        this.dateCreated = portalUser.getDateCreated();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<UserAccountDescriptionDto> getUserAccountDescriptionDtoList() {
        return userAccountDescriptionDtoList;
    }

    public void setUserAccountDescriptionDtoList(List<UserAccountDescriptionDto> userAccountDescriptionDtoList) {
        this.userAccountDescriptionDtoList = userAccountDescriptionDtoList;
    }

    public PortalAccount getPortalAccount() {
        return portalAccount;
    }

    public void setPortalAccount(PortalAccount portalAccount) {
        this.portalAccount = portalAccount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
