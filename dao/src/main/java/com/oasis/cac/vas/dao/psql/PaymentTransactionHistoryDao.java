package com.oasis.cac.vas.dao.psql;

import com.oasis.cac.vas.models.PaymentTransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PaymentTransactionHistoryDao extends JpaRepository<PaymentTransactionHistory, Long> {

    @Query("select p from PaymentTransactionHistory p where lower(p.transactionRef) = :ref")
    Optional<PaymentTransactionHistory> findByTransactionRef(@Param("ref") String ref);
}