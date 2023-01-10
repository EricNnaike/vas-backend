package com.oasis.cac.vas.service.payment_methods;

import com.oasis.cac.vas.dto.PaymentMethodDto;
import com.oasis.cac.vas.models.PaymentMethod;
import com.oasis.cac.vas.pojo.PaymentMethodPojo;

import java.util.List;
import java.util.Optional;

public interface PaymentMethodsService {

    void massCreation(List<PaymentMethodDto> paymentMethodDtoList);

    PaymentMethod save(PaymentMethodDto paymentMethodDto);

    List<PaymentMethodPojo> index();

    Optional<PaymentMethodPojo> findByName(String name);

    void update(PaymentMethodDto paymentMethodDto, PaymentMethod paymentMethod);

    Optional<PaymentMethod> findByNameForPaymentMethodObj(String name);
}
