package com.oasis.cac.vas.dao.psql;

import com.oasis.cac.vas.models.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentMethodsDao extends JpaRepository<PaymentMethod, Long> {

    Optional<PaymentMethod> findByName(String name);
}

