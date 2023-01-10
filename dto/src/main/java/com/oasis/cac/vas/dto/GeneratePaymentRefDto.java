package com.oasis.cac.vas.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class GeneratePaymentRefDto {

    @NotBlank
    private Long amountInKobo;
}
