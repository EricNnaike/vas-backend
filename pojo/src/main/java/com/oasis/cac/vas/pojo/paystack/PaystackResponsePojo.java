package com.oasis.cac.vas.pojo.paystack;

import lombok.Data;

@Data
public class PaystackResponsePojo {

    private boolean status;
    private String message;
    private Object data;
}
