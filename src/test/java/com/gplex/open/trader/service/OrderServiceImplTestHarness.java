package com.gplex.open.trader.service;

import com.gplex.open.trader.domain.TimeResponse;
import com.gplex.open.trader.utils.Security;
import com.gplex.open.trader.utils.Utils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.management.RuntimeErrorException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

import static org.junit.Assert.assertTrue;

/**
 * Created by Vlad S. on 9/15/17.
 */
public class OrderServiceImplTestHarness {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImplTestHarness.class);
    RestTemplate rt = new RestTemplate();
    private static final String  privateKey = "jsxvksOKUfwk1nhiC3q7GoGyokCcg52YZ8YH/x6JKLKSYeJJi6IC6wNNztf2wo/fM9XoVei0ClANpLV191oW2w==";
    Security sec = new Security(privateKey);
    OrderServiceImpl os = new OrderServiceImpl(rt, sec);
    public static Mac SHARED_MAC;

    static {
        try {
            SHARED_MAC = Mac.getInstance("HmacSHA256");
        } catch (NoSuchAlgorithmException nsaEx) {
            nsaEx.printStackTrace();
        }
    }
    public String generateSignature(String requestPath, String method, String body, String timestamp) {
        try {
            String prehash = timestamp + method.toUpperCase() + requestPath + body;
            byte[] secretDecoded = Base64.getDecoder().decode(privateKey);
            SecretKeySpec keyspec = new SecretKeySpec(secretDecoded, "HmacSHA256");
            Mac sha256 = (Mac) SHARED_MAC.clone();
            sha256.init(keyspec);
            return Base64.getEncoder().encodeToString(sha256.doFinal(prehash.getBytes()));
        } catch (CloneNotSupportedException | InvalidKeyException e) {
            e.printStackTrace();
            throw new RuntimeErrorException(new Error("Cannot set up authentication headers."));
        }
    }



    @Test
    public void testConnection(){

      os.getAccountInfo();


    }


    @Test
    public void testSecurity(){
        String ts = Utils.getTs();
        String sec1 = sec.signGET(ts, "/accounts");
        String sec2 = generateSignature("/accounts", "GET", "", ts);
        logger.debug("Checking: {}\n{}\n{}", ts,sec1, sec2);
        assertTrue(sec1.equals(sec2));

    }


    @Test
    public void testTs(){
        Double ct = new Date().getTime() / 1000.0;
        TimeResponse ts = os.getTime();

        logger.debug("{} ~ {}",String.format("%.3f",ct), String.format("%.3f",ts.getEpoch()));



    }



}