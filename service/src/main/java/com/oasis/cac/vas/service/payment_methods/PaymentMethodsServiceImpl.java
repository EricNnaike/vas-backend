package com.oasis.cac.vas.service.payment_methods;

import com.oasis.cac.vas.dao.psql.PaymentMethodsDao;
import com.oasis.cac.vas.dto.PaymentMethodDto;
import com.oasis.cac.vas.models.PaymentMethod;
import com.oasis.cac.vas.pojo.PaymentMethodPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class PaymentMethodsServiceImpl implements PaymentMethodsService {

    @Autowired
    private PaymentMethodsDao paymentMethodsDao;

    @Transactional
    @Override
    public void massCreation(List<PaymentMethodDto> paymentMethodDtoList) {

        for(PaymentMethodDto paymentMethodDto: paymentMethodDtoList) {

            PaymentMethod paymentMethod = new PaymentMethod();

            paymentMethod.setEnabled(paymentMethodDto.isEnabled());

            paymentMethod.setName(paymentMethodDto.getName().toLowerCase());

            paymentMethod.setLiveActive(paymentMethodDto.isLiveActive());


            paymentMethod.setVerificationInLine(paymentMethodDto.isVerificationInLine());

            paymentMethod.setLivePublicKey(paymentMethodDto.getLivePublicKey());
            paymentMethod.setLiveSecret(paymentMethodDto.getLiveSecret());
            paymentMethod.setLiveVerifyUrl(paymentMethodDto.getLiveVerifyUrl());

            paymentMethod.setTestPublicKey(paymentMethodDto.getTestPublicKey());
            paymentMethod.setTestSecret(paymentMethodDto.getTestSecret());
            paymentMethod.setTestVerifyUrl(paymentMethodDto.getTestVerifyUrl());

            this.paymentMethodsDao.save(paymentMethod);
        }

    }

    @Transactional
    @Override
    public PaymentMethod save(PaymentMethodDto paymentMethodDto) {

        PaymentMethod paymentMethod = new PaymentMethod();

        paymentMethod.setName(paymentMethodDto.getName().toLowerCase());

        paymentMethod.setVerificationInLine(paymentMethodDto.isVerificationInLine());

        paymentMethod.setEnabled(paymentMethodDto.isEnabled());

        paymentMethod.setLiveActive(paymentMethodDto.isLiveActive());

        paymentMethod.setLivePublicKey(paymentMethodDto.getLivePublicKey());
        paymentMethod.setLiveSecret(paymentMethodDto.getLiveSecret());
        paymentMethod.setLiveVerifyUrl(paymentMethodDto.getLiveVerifyUrl());

        paymentMethod.setTestPublicKey(paymentMethodDto.getTestPublicKey());
        paymentMethod.setTestSecret(paymentMethodDto.getTestSecret());
        paymentMethod.setTestVerifyUrl(paymentMethodDto.getTestVerifyUrl());

        return this.paymentMethodsDao.save(paymentMethod);
    }

    @Override
    public List<PaymentMethodPojo> index() {

        List<PaymentMethodPojo> paymentMethodPojoList = new ArrayList<>();

        List<PaymentMethod> paymentMethodList = this.paymentMethodsDao.findAll();

        for(PaymentMethod paymentMethod: paymentMethodList) {

            PaymentMethodPojo paymentMethodPojo = new PaymentMethodPojo();

            paymentMethodPojo.setName(paymentMethod.getName());

            paymentMethodPojo.setEnabled(paymentMethod.isEnabled());
            paymentMethodPojo.setLiveActive(paymentMethod.isLiveActive());

            paymentMethodPojo.setLivePublicKey(paymentMethod.getLivePublicKey());
            paymentMethodPojo.setLiveSecret(paymentMethod.getLiveSecret());
            paymentMethodPojo.setLiveVerifyUrl(paymentMethod.getLiveVerifyUrl());

            paymentMethodPojo.setVerificationInLine(paymentMethod.isVerificationInLine());

            paymentMethodPojo.setTestSecret(paymentMethod.getTestSecret());
            paymentMethodPojo.setTestPublicKey(paymentMethod.getTestPublicKey());
            paymentMethodPojo.setTestVerifyUrl(paymentMethod.getTestVerifyUrl());

            paymentMethodPojoList.add(paymentMethodPojo);
        }

        return paymentMethodPojoList;

    }

    @Override
    public Optional<PaymentMethodPojo> findByName(String name) {

        Optional<PaymentMethod> optionalPaymentMethod = this.paymentMethodsDao.findByName(name.toLowerCase());

        if(optionalPaymentMethod.isPresent()) {

            PaymentMethod paymentMethod = optionalPaymentMethod.get();

            PaymentMethodPojo paymentMethodPojo = new PaymentMethodPojo();

            paymentMethodPojo.setName(paymentMethod.getName());

            paymentMethodPojo.setVerificationInLine(paymentMethod.isVerificationInLine());

            paymentMethodPojo.setEnabled(paymentMethod.isEnabled());
            paymentMethodPojo.setLiveActive(paymentMethod.isLiveActive());

            paymentMethodPojo.setLivePublicKey(paymentMethod.getLivePublicKey());
            paymentMethodPojo.setLiveSecret(paymentMethod.getLiveSecret());
            paymentMethodPojo.setLiveVerifyUrl(paymentMethod.getLiveVerifyUrl());


            paymentMethodPojo.setTestSecret(paymentMethod.getTestSecret());
            paymentMethodPojo.setTestPublicKey(paymentMethod.getTestPublicKey());
            paymentMethodPojo.setTestVerifyUrl(paymentMethod.getTestVerifyUrl());

            return Optional.of(paymentMethodPojo);
        }

        return Optional.empty();
    }


    @Override
    public Optional<PaymentMethod> findByNameForPaymentMethodObj(String name) {
        return this.paymentMethodsDao.findByName(name.toLowerCase());
    }

    @Override
    public void update(PaymentMethodDto paymentMethodDto, PaymentMethod paymentMethod) {

        paymentMethod.setName(paymentMethodDto.getName().toLowerCase());

        paymentMethod.setEnabled(paymentMethodDto.isEnabled());

        paymentMethod.setLiveActive(paymentMethodDto.isLiveActive());

        paymentMethod.setVerificationInLine(paymentMethodDto.isVerificationInLine());

        paymentMethod.setLivePublicKey(paymentMethodDto.getLivePublicKey());
        paymentMethod.setLiveSecret(paymentMethodDto.getLiveSecret());
        paymentMethod.setLiveVerifyUrl(paymentMethodDto.getLiveVerifyUrl());

        paymentMethod.setTestPublicKey(paymentMethodDto.getTestPublicKey());
        paymentMethod.setTestSecret(paymentMethodDto.getTestSecret());
        paymentMethod.setTestVerifyUrl(paymentMethodDto.getTestVerifyUrl());

        this.paymentMethodsDao.save(paymentMethod);

    }
}
