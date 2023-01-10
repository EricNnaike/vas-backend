package com.oasis.cac.vas.dto;


import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PaymentMethodDto {

    @NotBlank
    private String name;

    private boolean isEnabled;

    private boolean isLiveActive;

    private String liveVerifyUrl;

    private String liveSecret;

    private boolean isVerificationInLine;

    private String livePublicKey;

    private String testVerifyUrl;

    private String testSecret;

    private String testPublicKey;

    @Override
    public String toString() {
        return "PaymentMethodDto{" +
                "name='" + name + '\'' +
                ", isEnabled=" + isEnabled +
                ", isLiveActive=" + isLiveActive +
                ", liveVerifyUrl='" + liveVerifyUrl + '\'' +
                ", liveSecret='" + liveSecret + '\'' +
                ", livePublicKey='" + livePublicKey + '\'' +
                ", testVerifyUrl='" + testVerifyUrl + '\'' +
                ", testSecret='" + testSecret + '\'' +
                ", testPublicKey='" + testPublicKey + '\'' +
                '}';
    }
}
