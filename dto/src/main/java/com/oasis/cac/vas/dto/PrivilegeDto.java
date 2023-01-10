package com.oasis.cac.vas.dto;

import javax.validation.constraints.NotBlank;

public class PrivilegeDto {

    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
