package com.oasis.cac.vas.service.paystack;

import com.google.gson.Gson;
import com.oasis.cac.vas.dto.paystack.PayStackDto;
import com.oasis.cac.vas.utils.OkHttpUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PayStackServiceImpl implements PayStackService {

    @Autowired
    private OkHttpUtil okHttpUtil;

    @Autowired
    private Gson gson;

    @Value("${payStackToken}")
    private String payStackToken;

    @Override
    public boolean verifyTransaction(String reference) throws IOException {

            String url = "https://api.paystack.co/transaction/verify/" + reference;
            String authValue = payStackToken;

            JSONObject response = this.okHttpUtil.getWithJsonResponseWithAuthorizationHeader(url, authValue);

            JSONObject jsonObjectForData = (JSONObject) response.get("data");
            Boolean success = (Boolean) jsonObjectForData.get("success");

            return success;
    }

    @Override
    public boolean initPayment(PayStackDto payStackDto) throws IOException {


            String url = "https://api.paystack.co/transaction/initialize";
            String json = this.gson.toJson(payStackDto);
            String headerKey = "Authorization";
            String headerValue = payStackToken;

            this.okHttpUtil.postWithASingleHeader(url, json, headerKey, headerValue);

            return false;
    }
}
