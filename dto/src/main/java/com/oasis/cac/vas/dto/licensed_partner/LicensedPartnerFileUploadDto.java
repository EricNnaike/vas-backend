package com.oasis.cac.vas.dto.licensed_partner;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class LicensedPartnerFileUploadDto {

    @NotBlank
    @NotNull
    private String docName;

    @NotBlank
    @NotNull
    private String url;

    @NotNull
    @NotBlank
    private Long documentId;
}
