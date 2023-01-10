package com.oasis.cac.vas.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class EmailVerificationDto {

    @NotBlank
    private Long userId;

    @NotBlank
    private String code;
}
