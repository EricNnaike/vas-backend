package com.oasis.cac.vas.restfulapi.controllers.payment_methods;

import com.oasis.cac.vas.pojo.PaymentMethodPojo;
import com.oasis.cac.vas.service.payment_methods.PaymentMethodsService;
import com.oasis.cac.vas.utils.MessageUtil;
import com.oasis.cac.vas.utils.controllers.PublicBaseApiController;
import com.oasis.cac.vas.utils.errors.ApiError;
import com.oasis.cac.vas.utils.errors.MyApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PublicPaymentMethods extends PublicBaseApiController {

    private static final Logger logger = LoggerFactory.getLogger(PublicPaymentMethods.class.getSimpleName());

    @Autowired
    private MessageUtil messageUtil;

    @Autowired
    private MyApiResponse myApiResponse;

    @Autowired
    private PaymentMethodsService paymentMethodsService;


    @GetMapping("/payment_methods")
    public ResponseEntity<?> index(HttpServletResponse res,
                                   HttpServletRequest request,
                                   Authentication authentication) {
        ApiError apiError = null;
        String lang = "en";

        try {

            List<PaymentMethodPojo> paymentMethodList = this.paymentMethodsService.index();

            apiError = new ApiError(
                    HttpStatus.OK.value(),
                    HttpStatus.OK,
                    messageUtil.getMessage("fetch.payment.methods",
                    lang), true, new ArrayList<>(),
                    paymentMethodList
            );

            return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());

        } catch (Exception e) {

            return this.myApiResponse.internalServerErrorResponse();
        }
    }
}
