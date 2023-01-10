package com.oasis.cac.vas.restfulapi.controllers.payment_methods;

import com.oasis.cac.vas.dto.PaymentMethodDto;
import com.oasis.cac.vas.models.PaymentMethod;
import com.oasis.cac.vas.pojo.PaymentMethodPojo;
import com.oasis.cac.vas.service.payment_methods.PaymentMethodsService;
import com.oasis.cac.vas.utils.MessageUtil;
import com.oasis.cac.vas.utils.controllers.ProtectedBaseApiController;
import com.oasis.cac.vas.utils.errors.ApiError;
import com.oasis.cac.vas.utils.errors.CustomBadRequestException;
import com.oasis.cac.vas.utils.errors.MyApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class ProtectedPaymentMethods extends ProtectedBaseApiController {

    private static final Logger logger = LoggerFactory.getLogger(ProtectedPaymentMethods.class.getSimpleName());

    @Autowired
    private MessageUtil messageUtil;

    @Autowired
    private MyApiResponse myApiResponse;

    @Autowired
    private PaymentMethodsService paymentMethodsService;


    @GetMapping("/payment_methods/{name}")
    public ResponseEntity<?> findByName(HttpServletResponse res,
                                        HttpServletRequest request,
                                        Authentication authentication,
                                        @PathVariable String name) {
        ApiError apiError = null;
        String lang = "en";

        try {

            Optional<PaymentMethodPojo> optionalPaymentMethodPojo = this.paymentMethodsService.findByName(name.toLowerCase());

            if(optionalPaymentMethodPojo.isPresent()) {

                PaymentMethodPojo paymentMethodPojo = optionalPaymentMethodPojo.get();

                apiError = new ApiError(
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        messageUtil.getMessage("fetch.payment.method.success",
                                lang), true, new ArrayList<>(),
                        paymentMethodPojo
                );

            } else  {
                apiError = new ApiError(
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        messageUtil.getMessage("fetch.payment.method.failed",
                                lang), false, new ArrayList<>(),
                        null
                );
            }

            return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());

        } catch (Exception e) {

            return myApiResponse.internalServerErrorResponse();
        }
    }


    @PostMapping("/payment_methods/edit")
    public ResponseEntity<?> edit(@Valid @RequestBody PaymentMethodDto paymentMethodDto,
                                  HttpServletResponse res,
                                  HttpServletRequest request,
                                  Authentication authentication,
                                  BindingResult bindingResult) {

        ApiError apiError = null;
        String lang = "en";

        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(objectError -> {
                logger.info(objectError.toString());
            });

            throw new CustomBadRequestException();
        }

        try {

            logger.info(paymentMethodDto.toString());

            Optional<PaymentMethod> optionalPaymentMethod = this.paymentMethodsService.findByNameForPaymentMethodObj(paymentMethodDto.getName().toLowerCase());

            if(optionalPaymentMethod.isPresent()) {

                this.paymentMethodsService.update(paymentMethodDto, optionalPaymentMethod.get());

                apiError = new ApiError(
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        messageUtil.getMessage("payment.method.update.successful",
                                lang), true, new ArrayList<>(),
                        null
                );

            } else {
                apiError = new ApiError(
                        HttpStatus.OK.value(),
                        HttpStatus.OK,
                        messageUtil.getMessage("payment.method.name.notfound",
                                lang), false, new ArrayList<>(),
                        null
                );
            }

            return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());

        } catch (Exception e) {

            logger.info(e.getMessage());

            return myApiResponse.internalServerErrorResponse();
        }
    }
}
