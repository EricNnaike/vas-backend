package com.oasis.cac.vas.restfulapi.controllers.payment;

import com.oasis.cac.vas.dto.GeneratePaymentRefDto;
import com.oasis.cac.vas.dto.PaymentVerificationDto;
import com.oasis.cac.vas.dto.paystack.PayStackDto;
import com.oasis.cac.vas.models.PaymentTransactionHistory;
import com.oasis.cac.vas.service.payment_transaction_ref.PaymentTransactionRefService;
import com.oasis.cac.vas.service.paystack.PayStackService;
import com.oasis.cac.vas.utils.MessageUtil;
import com.oasis.cac.vas.utils.controllers.PublicBaseApiController;
import com.oasis.cac.vas.utils.errors.ApiError;
import com.oasis.cac.vas.utils.errors.CustomBadRequestException;
import com.oasis.cac.vas.utils.errors.MyApiResponse;
import org.apache.http.util.TextUtils;
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
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Optional;

@RestController
public class PublicPaymentController extends PublicBaseApiController {

    private static final Logger logger = LoggerFactory.getLogger(PublicPaymentController.class.getSimpleName());

    @Autowired
    private MessageUtil messageUtil;

    @Autowired
    private PaymentTransactionRefService paymentTransactionRefService;


    @Autowired
    private PayStackService payStackService;

    @Autowired
    private MyApiResponse myApiResponse;


//    @PostMapping("/payment/pay-stack/pay")
//    public ResponseEntity<?> initializePayment(@Valid @RequestBody PayStackDto payStackDto,
//                                               HttpServletResponse res,
//                                               HttpServletRequest request,
//                                               Authentication authentication,
//                                               BindingResult bindingResult) {
//        ApiError apiError = null;
//        String lang = "en";
//
//        if (bindingResult.hasErrors()) {
//
//            bindingResult.getAllErrors().forEach(objectError -> {
//                logger.info(objectError.toString());
//            });
//
//            throw new CustomBadRequestException();
//        }
//
//        try {
//
//
//            boolean status = this.payStackService.initPayment(payStackDto);
//
//
//            apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK, messageUtil.getMessage("payment.transaction.successful", lang),
//                    status, new ArrayList<>(), null);
//
//            return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
//
//
//
//        } catch (Exception e) {
//
//            return new MyApiResponse().internalServerErrorResponse();
//        }
//    }
//
//
//    @GetMapping("/payment/pay-stack/verify/{reference}")
//    public ResponseEntity<?> verifyPayment(@PathVariable String reference,
//                                           HttpServletResponse res,
//                                           HttpServletRequest request) {
//        ApiError apiError = null;
//        String lang = "en";
//
//        if (TextUtils.isBlank(reference)) {
//
//            apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK, messageUtil.getMessage("payment.reference.required", lang),
//                    true, new ArrayList<>(), null);
//
//            return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
//        }
//
//        try {
//
//            boolean status = this.payStackService.verifyTransaction(reference);
//
//            apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK, messageUtil.getMessage("payment.verification.successful", lang),
//                    status, new ArrayList<>(), null);
//
//            return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
//
//
//        } catch (NullPointerException | IOException e) {
//
//            return new MyApiResponse().internalServerErrorResponse();
//        }
//    }
//


    //Send email with attachment
    @PostMapping("/payment/generate/ref")
    public ResponseEntity<?> generatePaymentTransactionRef(@Valid @RequestBody GeneratePaymentRefDto generatePaymentRefDto,
                                                           HttpServletResponse res,
                                                           HttpServletRequest request,
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

            String ref = this.paymentTransactionRefService.generateAndSave(""+generatePaymentRefDto.getAmountInKobo());

            apiError = new ApiError(HttpStatus.OK.value(),
                    HttpStatus.OK,
                    this.messageUtil.getMessage("transaction.ref.generated.successfully",
                            lang),
                    true, new ArrayList<>(), ref);

            return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());

        } catch (NullPointerException e) {
            return new MyApiResponse().internalServerErrorResponse();
        }
    }


    //Send email with attachment
    @PostMapping("/payment/verification")
    public ResponseEntity<?> verifyPaymentAndSendEmailWithAttachment(@Valid @RequestBody PaymentVerificationDto paymentVerificationDto,
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

            logger.info(paymentVerificationDto.getTransactionRef());

            Optional<PaymentTransactionHistory> paymentTransactionHistoryOptional =

            this.paymentTransactionRefService.findByRef(paymentVerificationDto.getTransactionRef());

            if(paymentTransactionHistoryOptional.isPresent()) {

                PaymentTransactionHistory paymentTransactionHistory = paymentTransactionHistoryOptional.get();

                apiError = this.paymentTransactionRefService.doVerification(paymentVerificationDto, lang, paymentTransactionHistory);

            } else {
                apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK,
                        this.messageUtil.getMessage("payment.transaction.ref.notfound", lang),
                        false, new ArrayList<>(), null);
            }
            return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());

        } catch (NullPointerException | IOException | URISyntaxException e) {
            logger.info(e.getMessage());
            return myApiResponse.internalServerErrorResponse();
        }
    }


}
