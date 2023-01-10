package com.oasis.cac.vas.auth.transformation;

import com.google.gson.Gson;
import com.oasis.cac.vas.dto.PaymentMethodDto;
import com.oasis.cac.vas.pojo.PaymentMethodPojo;
import com.oasis.cac.vas.service.payment_methods.PaymentMethodsService;
import com.oasis.cac.vas.utils.ResourceUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import static com.oasis.cac.vas.auth.transformation.Transformer.*;

@Component
public class CreatePaymentMethodsJsonTransform {

    private static final Logger logger = Logger.getLogger(CreatePaymentMethodsJsonTransform.class.getSimpleName());

    @Autowired
    private PaymentMethodsService paymentMethodsService;

    private final Gson gson;

    @Autowired
    public CreatePaymentMethodsJsonTransform(){
        this.gson = new Gson();
    }

    public void create(String fileName) {

        BufferedReader bufferedReader = null;

        InputStream inputStream = ResourceUtil.getResourceAsStream(TRANSFORMATION_DATA_FOLDER + File.separator + JSON_FOLDER + File.separator + fileName);

        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        PaymentMethodDto[] paymentMethodDtos = gson.fromJson(bufferedReader, PaymentMethodDto[].class);

        logger.info("Adding payment methods");

        List<PaymentMethodDto> toSaveList = new ArrayList<>();

        for (PaymentMethodDto dto : paymentMethodDtos) {

            Optional<PaymentMethodPojo> optionalPaymentMethod;

            if (StringUtils.isNotBlank(dto.getName())) {

                optionalPaymentMethod = this.paymentMethodsService.findByName(dto.getName().toLowerCase());

                if (optionalPaymentMethod.isPresent()) {
                    logger.info("Payment Method " + dto.getName() + " already exist");
                } else {
                    toSaveList.add(dto);
                }
            }
        }

        if(toSaveList.size() > 0) {
            this.paymentMethodsService.massCreation(toSaveList);
            logger.info("Done adding payment methods");
        } else {
            logger.info("No new payment methods to add");
        }

    }
}
