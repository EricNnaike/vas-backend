package com.oasis.cac.vas.pojo;

import lombok.Data;

@Data
public class PaymentMethodPojo {

    private Long id;

    private String name;

    private boolean isEnabled;

    private boolean isLiveActive;

    private boolean isVerificationInLine;

    private String liveVerifyUrl;

    private String liveSecret;

    private String livePublicKey;

    private String testVerifyUrl;

    private String testSecret;

    private String testPublicKey;
}
