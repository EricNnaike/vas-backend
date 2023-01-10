package com.oasis.cac.vas.dto.paystack;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PayStackDto {
    @NotBlank
    private String email;
    @NotBlank
    private String amount;
}
