package com.oasis.cac.vas.service.payment_transaction_ref;

import com.oasis.cac.vas.dto.PaymentVerificationDto;
import com.oasis.cac.vas.models.PaymentMethod;
import com.oasis.cac.vas.models.PaymentTransactionHistory;
import com.oasis.cac.vas.utils.errors.ApiError;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

public interface PaymentTransactionRefService {

    String generateAndSave(String amountInKobi);

    Optional<PaymentTransactionHistory> findByRef(String ref);

    void update(PaymentVerificationDto paymentVerificationDto,
                PaymentTransactionHistory paymentTransactionHistory,
                PaymentMethod paymentMethod,
                boolean isVerified);

    Boolean doApiVerification(String url, String authValue, String paymentType) throws IOException;

    ApiError doVerification(PaymentVerificationDto paymentVerificationDto,
                            String lang,
                            PaymentTransactionHistory paymentTransactionHistory) throws IOException, URISyntaxException;
}
