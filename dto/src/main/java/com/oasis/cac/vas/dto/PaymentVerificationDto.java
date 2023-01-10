package com.oasis.cac.vas.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class PaymentVerificationDto {

    @NotBlank
    private String email;

    @NotBlank
    private Long amountInKobo;

    @NotBlank
    private String transactionRef;

    private String rcNumber;

    private String companyName;

    private CountBuilderDto[] queries;

    @NotBlank
    private String paymentMethodName;
}
