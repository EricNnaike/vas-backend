package com.oasis.cac.vas.service.sequence.payment_transaction_ref;

import com.oasis.cac.vas.service.sequence.SequenceServiceImp;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@PaymentTransactionRefSequence
@Component
public class PaymentTransactionRefSequenceService extends SequenceServiceImp {
     public PaymentTransactionRefSequenceService(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate,"payment_transaction_ref");
    }
}
