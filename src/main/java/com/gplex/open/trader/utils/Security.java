package com.gplex.open.trader.utils;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Vlad S. on 9/11/17.
 */
public class Security {
    private static final Logger logger = LoggerFactory.getLogger(Security.class);
    private final byte[] decodedSecret;
    private Mac mac = null;

    public Security(String secret) {
        this.decodedSecret = Base64.decodeBase64(secret);
    }

    public String sign(String ts, String method, String path, String body) {
        try {


            if (this.mac == null) {
                SecretKeySpec keySpec = new SecretKeySpec(decodedSecret, "HmacSHA256");
                this.mac = Mac.getInstance("HmacSHA256");
                this.mac.init(keySpec);
            }
            Mac mc = (Mac) this.mac.clone();
            String raw = ts + method.toUpperCase() + path + body;
            byte[] rawHmac = mc.doFinal(raw.getBytes());
            return Base64.encodeBase64String(rawHmac);
        } catch (Exception e) {
            logger.error("", e);
        }
        return null;
    }

    public String signGET(String ts, String path) {
        return sign(ts, "GET", path, "");
    }

    public String signPOST(String ts, String path, String body) {
        return sign(ts, "POST", path, body);
    }

}
