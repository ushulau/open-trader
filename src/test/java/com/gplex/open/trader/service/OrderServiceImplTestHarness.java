package com.gplex.open.trader.service;

import com.gplex.open.trader.domain.Account;
import com.gplex.open.trader.domain.TimeResponse;
import com.gplex.open.trader.utils.Security;
import com.gplex.open.trader.utils.Utils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.management.RuntimeErrorException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Vlad S. on 9/15/17.
 */
@SpringBootTest
@SpringBootConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource("classpath:gdax-keys.properties")
public class OrderServiceImplTestHarness {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImplTestHarness.class);
    RestTemplate rt = new RestTemplate();
    @Value("${gdax.secret}")
    private String privateKey;
    @Value("${gdax.key}")
    private String key;
    @Value("${gdax.passphrase}")
    private String passphrase;
    @Value("${gdax.api.baseUrl}")
    private String baseUrl;

    private Security sec;
    private AccountsServiceImpl os;
    public static Mac SHARED_MAC;

    static {
        try {
            SHARED_MAC = Mac.getInstance("HmacSHA256");
        } catch (NoSuchAlgorithmException nsaEx) {
            nsaEx.printStackTrace();
        }
    }

    @PostConstruct
    private void postConstruct() {
        sec = new Security(privateKey);
        os = new AccountsServiceImpl(rt, sec, key, passphrase, baseUrl);
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
    public void testConnection() {
        List<Account> accounts = os.listAccounts();
        logger.debug("{}", new Object[]{accounts});
    }


    @Test
    public void testAccountConnection() {
        List<Account> accounts = os.listAccounts();
        for(Account ac: accounts){
            Account account = os.getAnAccount(ac.getId());
            logger.debug("account -> \n{}",account);
            logger.debug("holds -> \n{}", os.getHolds(ac.getId()));
            logger.debug("history -> \n{}", os.getAccountHistory(ac.getId()));
        }

    }



    @Test
    public void testSecurity() {
        String ts = Utils.getTs();
        String sec1 = sec.signGET(ts, "/accounts");
        String sec2 = generateSignature("/accounts", "GET", "", ts);
        logger.debug("Checking: {}\n{}\n{}", ts, sec1, sec2);
        assertTrue(sec1.equals(sec2));
    }

    @Test
    public void testTs() {
        Double ct = new Date().getTime() / 1000.0;
        TimeResponse ts = os.getTime();
        logger.debug("{} ~ {}", String.format("%.3f", ct), String.format("%.3f", ts.getEpoch()));
    }

}