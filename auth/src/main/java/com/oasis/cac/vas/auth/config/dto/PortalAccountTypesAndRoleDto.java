package com.oasis.cac.vas.auth.config.dto;


import com.oasis.cac.vas.utils.oasisenum.PortalUserTypeConstant;

import java.util.List;

public class PortalAccountTypesAndRoleDto {

    private String portalAccountName;

    private PortalUserTypeConstant portalUserTypeConstant;

    private List<UserAccountDescriptionDto> portalAccountDescriptionDtoList;

    public PortalUserTypeConstant getPortalUserTypeConstant() {
        return portalUserTypeConstant;
    }

    public void setPortalUserTypeConstant(PortalUserTypeConstant portalUserTypeConstant) {
        this.portalUserTypeConstant = portalUserTypeConstant;
    }

    public String getPortalAccountName() {
        return portalAccountName;
    }

    public void setPortalAccountName(String portalAccountName) {
        this.portalAccountName = portalAccountName;
    }

    public List<UserAccountDescriptionDto> getPortalAccountDescriptionDtoList() {
        return portalAccountDescriptionDtoList;
    }

    public void setPortalAccountDescriptionDtoList(List<UserAccountDescriptionDto> portalAccountDescriptionDtoList) {
        this.portalAccountDescriptionDtoList = portalAccountDescriptionDtoList;
    }
}
