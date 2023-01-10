package com.oasis.cac.vas.service.paystack;

import com.oasis.cac.vas.dto.paystack.PayStackDto;

import java.io.IOException;

public interface PayStackService {

    boolean verifyTransaction(String reference) throws IOException;

    boolean initPayment(PayStackDto payStackDto) throws IOException;
}
