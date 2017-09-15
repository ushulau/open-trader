package com.gplex.open.trader.utils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
/**
 * Created by Vlad S. on 9/11/17.
 */
public class Security {

    public static String hmacMd5Encode(String key, String message) throws Exception {


        byte[] decodedKey = Base64.decodeBase64(key);
        SecretKeySpec keySpec = new SecretKeySpec(decodedKey, "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");


        mac.init(keySpec);
        byte[] rawHmac = mac.doFinal(message.getBytes());

        return Hex.encodeHexString(rawHmac);
    }

    public static String hmacMd5EncodeBase64(String key, String message) throws Exception {
         String res = hmacMd5Encode(key, message);
        return new String(Base64.encodeBase64String(res.getBytes()));
    }
}
