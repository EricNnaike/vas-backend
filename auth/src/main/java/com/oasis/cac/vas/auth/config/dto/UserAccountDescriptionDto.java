package com.oasis.cac.vas.auth.config.dto;

import java.util.List;

public class UserAccountDescriptionDto {

    private List<String> roleNames;

    public List<String> getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(List<String> roleNames) {
        this.roleNames = roleNames;
    }
}
