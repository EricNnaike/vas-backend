package com.oasis.cac.vas.service.payment_transaction_ref;


import com.google.gson.Gson;
import com.oasis.cac.vas.dao.psql.PaymentTransactionHistoryDao;
import com.oasis.cac.vas.dto.PaymentVerificationDto;
import com.oasis.cac.vas.models.PaymentMethod;
import com.oasis.cac.vas.models.PaymentTransactionHistory;
import com.oasis.cac.vas.service.payment_methods.PaymentMethodsService;
import com.oasis.cac.vas.service.sequence.payment_transaction_ref.PaymentTransactionRefSequenceService;
import com.oasis.cac.vas.utils.MessageUtil;
import com.oasis.cac.vas.utils.OkHttpUtil;
import com.oasis.cac.vas.utils.errors.ApiError;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.*;

@Service
public class PaymentTransactionRefServiceImpl implements PaymentTransactionRefService {

    @Autowired
    private PaymentTransactionHistoryDao paymentTransactionHistoryDao;

    @Autowired
    private PaymentTransactionRefSequenceService transRef;


    @Autowired
    private PaymentMethodsService paymentMethodsService;


    @Autowired
    private OkHttpUtil okHttpUtil;


    @Autowired
    private MessageUtil messageUtil;

    @Override
    public String generateAndSave(String amountInKobi) {

         String ref  = String.format("TRANS_%04d%02d%02d%05d",
                LocalDate.now().getYear(),
                LocalDate.now().getMonthValue(),
                LocalDate.now().getDayOfMonth(),
                 this.transRef.getNextId()
        );

        PaymentTransactionHistory paymentTransactionHistory = new PaymentTransactionHistory();
        paymentTransactionHistory.setTransactionRef(ref);
        paymentTransactionHistory.setAmountInKobo(Long.valueOf(amountInKobi));
        this.paymentTransactionHistoryDao.save(paymentTransactionHistory);
        return ref;
    }

    @Override
    public Optional<PaymentTransactionHistory> findByRef(String ref) {
       return this.paymentTransactionHistoryDao.findByTransactionRef(ref.toLowerCase());

    }

    @Override
    public void update(PaymentVerificationDto paymentVerificationDto,
                       PaymentTransactionHistory paymentTransactionHistory,
                       PaymentMethod paymentMethod,
                       boolean isVerified) {

        paymentTransactionHistory.setPaymentMethod(paymentMethod);
        paymentTransactionHistory.setVerified(isVerified);
        Gson gson = new Gson();
        String json = gson.toJson(paymentVerificationDto.getQueries());
        paymentTransactionHistory.setUserQuery(json);
        paymentTransactionHistory.setCompanyName(paymentVerificationDto.getCompanyName());
        paymentTransactionHistory.setRcNumber(paymentVerificationDto.getRcNumber());
        this.paymentTransactionHistoryDao.save(paymentTransactionHistory);

    }

    public String getVerificationUrl(PaymentMethod paymentMethod, PaymentVerificationDto paymentVerificationDto) {

        String url = "";

        String ref = paymentVerificationDto.getTransactionRef();

        System.out.println(paymentMethod.isVerificationInLine());

        System.out.println(ref);

        if(paymentMethod.isVerificationInLine()) {

            System.out.println("inline verification...");

            // String stringToMutate = paymentMethod.getLiveVerifyUrl();

            if(paymentMethod.isLiveActive()) {
                url = paymentMethod.getLiveVerifyUrl();
            } else {
                url = paymentMethod.getTestVerifyUrl();
            }

            Map<String, String> urlParams = new HashMap<>();

            urlParams.put("transactionRef", ref);

            UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromUriString(url);

            URI result = urlBuilder.buildAndExpand(urlParams).toUri();

            url = result.toString();

            System.out.println(url);

        } else {

            if(paymentMethod.isLiveActive()) {
                url = paymentMethod.getLiveVerifyUrl() + paymentVerificationDto.getTransactionRef();
            } else {
                url = paymentMethod.getTestVerifyUrl() + paymentVerificationDto.getTransactionRef();
            }
        }

        return url;
    }

    @Override
    public ApiError doVerification(PaymentVerificationDto paymentVerificationDto,
                                   String lang,
                                   PaymentTransactionHistory paymentTransactionHistory) throws IOException, URISyntaxException {


        ApiError apiError = null;

        if(paymentVerificationDto.getPaymentMethodName() != null && !paymentVerificationDto.getPaymentMethodName().isEmpty()) {

            Optional<PaymentMethod> optionalPaymentMethod = this.paymentMethodsService.findByNameForPaymentMethodObj(paymentVerificationDto.getPaymentMethodName());

            if(optionalPaymentMethod.isPresent()) {

                PaymentMethod paymentMethod = optionalPaymentMethod.get();

                String url = getVerificationUrl(paymentMethod, paymentVerificationDto);

                StringBuilder sb = new StringBuilder();

                sb.append("Bearer ");

                if(paymentMethod.isLiveActive()) {
                    sb.append(paymentMethod.getLiveSecret());
                } else {
                    sb.append(paymentMethod.getTestSecret());
                }

                boolean verificationSuccessful = doApiVerification(url, sb.toString(), paymentMethod.getName());

                if(verificationSuccessful) {

                    this.update(paymentVerificationDto, paymentTransactionHistory, paymentMethod, true);

                    apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK,
                            this.messageUtil.getMessage("payment.verification.successful", lang),
                            true, new ArrayList<>(), null);

                } else {

                    apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK,
                            this.messageUtil.getMessage("payment.verification.failed", lang),
                            false, new ArrayList<>(), null);
                }
            } else {
                apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK,
                        this.messageUtil.getMessage("payment.method.required", lang),
                        false, new ArrayList<>(), null);
            }
        } else {
            apiError = new ApiError(HttpStatus.OK.value(), HttpStatus.OK,
                    this.messageUtil.getMessage("payment.method.required", lang),
                    false, new ArrayList<>(), null);
        }
        return apiError;
    }


    public Boolean doApiVerification(String url, String authValue, String paymentType) throws IOException {

        System.out.println(authValue);

        JSONObject response = this.okHttpUtil.getWithJsonResponseWithAuthorizationHeader(url, authValue);

        System.out.println(response.toString());

        if(paymentType.equalsIgnoreCase("flutter_wave")) {
            return response.get("status").equals("success");
        } else if(paymentType.equalsIgnoreCase("pay_stack")) {
            return response.get("message").equals("Verification successful");
        }

        return response.get("status").equals("success");
    }
}
