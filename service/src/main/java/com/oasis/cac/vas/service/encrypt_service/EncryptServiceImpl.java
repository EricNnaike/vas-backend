package com.oasis.cac.vas.service.encrypt_service;

import com.oasis.cac.vas.utils.EncryptAndDecryptoUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EncryptServiceImpl implements EncryptService {

    @Override
    public List<Object> encryptDataList(List<Object> input) {
        JSONArray jsonArray = EncryptAndDecryptoUtil.encodeObjectList(input);
        System.out.println(jsonArray.toString());
        return new ArrayList<>();
    }


    @Override
    public Object encryptDataObject(Object input) {
        JSONObject jsonObject = EncryptAndDecryptoUtil.encodeObject(input);
        return new ArrayList<>();
    }
}
