package com.oasis.cac.vas.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.tomcat.util.json.JSONParser;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EncryptAndDecryptoUtil {


    public static <T> T fromGson(String jsonString, Class<T> tClass) {
        return new Gson().fromJson(jsonString, tClass);
    }

    public static String encode(Object value) {
        try {
//            String input = String.valueOf(value);
//            SHA3.DigestSHA3 digestSHA3 = new SHA3.Digest512();
//            byte[] digest = digestSHA3.digest(input.getBytes());
//
//            String result = Hex.toHexString(digest);
            // System.out.println("SHA3-512 = " + result);
            //return result;
//            byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
//            String decodedString = new String(decodedBytes);
            String encodedString = Base64.getEncoder().encodeToString(value.toString().getBytes());
            return encodedString;
        } catch (Exception e) {
            //   e.printStackTrace();
            return "Error encoding String";
        }

    }


//    public SealedObject doSealObjectEncryption(Serializable object) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, InvalidKeyException {
//        SecretKey key = SecretKey.serialVersionUID;
//        IvParameterSpec iv = new IvParameterSpec().getIV();
//        String algorithm = "AES/CBC/PKCS5Padding";
//        return encryptObject(algorithm, object, key, iv);
//    }


    public static SealedObject encryptObject(String algorithm, Serializable object,
                                             SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, IOException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        SealedObject sealedObject = new SealedObject(object, cipher);
        return sealedObject;
    }

    public static SealedObject doEncrypt(Serializable object) {
        try {

            SecretKey key = KeyGenerator.getInstance("AES").generateKey();
            // get base64 encoded version of the key
            String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
            System.out.println(encodedKey);
            // IvParameterSpec ivParameterSpec = AESUtil.generateIv();
            String algorithm = "AES/CBC/PKCS5Padding";
            return encryptObject(algorithm, object, key, null);
        } catch (Exception ex) {
            return null;
        }
    }


    public static String encrypt(String algorithm, String input, SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] cipherText = cipher.doFinal(input.getBytes());
        return Base64.getEncoder()
                .encodeToString(cipherText);
    }

    public static String decode(String value) {
        try {
            return new String(Base64.getDecoder().decode(value.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);

        } catch (Exception e) {
            return "Error decoding String";
        }

    }

    public static JSONObject encodeObject(Object obj) {
        Gson gson = new Gson();
        String result = gson.toJson(obj);

        System.out.println(result);
        // JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;


        Pattern pattern = Pattern.compile("^[\\[]");
        Matcher matcher = pattern.matcher(result);


        System.out.println("matches: " + matcher.matches());
        if(matcher.find()) {
            JSONArray jsonArray = new JSONArray(result);
            JSONArray newJsonArray = new JSONArray(result);

            for(Object object: jsonArray) {
                JSONObject jsonObject1 = (JSONObject) object;
                System.out.println(jsonObject1.toString());
                JSONObject encryptedJsonObject = encryptAndReturnJson(jsonObject1);
                System.out.println(encryptedJsonObject.toString());
                newJsonArray.put(encryptedJsonObject);
            }


        } else {

        }



        return jsonObject;
    }



    public static JSONObject encryptAndReturnJson(JSONObject jsonObject) {
        try {
                Iterator<String> keys = jsonObject.keys();

                while (keys.hasNext()) {
                    String key = keys.next();
                    Object obj = jsonObject.get(key);
                    if (obj instanceof JSONArray) {
                        System.out.println("json array");
                    } else if(obj instanceof JSONObject)  {
                        System.out.println("json object");
                    } else {
                        jsonObject.put(key, encode(obj));
                    }

                    System.out.println("yes");
                }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }


    public static JSONArray encodeObjectList(List<Object> objectList) {

        JSONArray jsonArray = new JSONArray();
        for(Object object: objectList) {
            JSONObject jsonObject = encodeObject(object);
            jsonArray.put(jsonObject);
        }

        return jsonArray;

    }

    public static JSONObject encodeObject2(Object obj) throws JSONException {
        Gson gson = new Gson();
        String result = gson.toJson(obj);

        // JSONParser parser = new JSONParser();
        JSONObject jsonObject = new JSONObject(result.trim());

        Iterator<String> keys = jsonObject.keys();

        while (keys.hasNext()) {
            String key = keys.next();
            String value = jsonObject.getString(key);
            jsonObject.put(key, decode(value));
        }
        return jsonObject;
    }
}
