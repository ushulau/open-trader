package com.gplex.open.trader.service;

import com.gplex.open.trader.domain.Account;
import com.gplex.open.trader.domain.AccountActivity;
import com.gplex.open.trader.domain.Hold;
import com.gplex.open.trader.domain.TimeResponse;
import com.gplex.open.trader.rest.BaseSecureClient;
import com.gplex.open.trader.utils.Security;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Vlad S. on 9/14/17.
 */
public class AccountsServiceImpl extends BaseSecureClient{
    private static final Logger logger = LoggerFactory.getLogger(AccountsServiceImpl.class);

    private final String baseUrl;


    public AccountsServiceImpl(RestTemplate restTemplate, Security sec, String key, String passphrase, String baseUrl) {
        super(restTemplate, sec, key, passphrase);
        this.baseUrl = baseUrl;
    }



    public TimeResponse getTime(){
        String requestPath = this.baseUrl +"/time";
        TimeResponse tr = new TimeResponse();
        tr.setEpoch(new Date().getTime()/1000.0);
        try {
            ResponseEntity<TimeResponse> result = executeNotSignedGET(requestPath, TimeResponse.class);
            tr = result.getBody();
        } catch (HttpStatusCodeException e) {
            logger.error("Unable to get response due to [" + e.getResponseBodyAsString() + "]");
        } catch (Exception ex) {
            logger.error("Unable to get response due to [" + ex.getMessage() + "]", ex);
        }
        return tr;
    }

    public List<Account> listAccounts(){
        String requestPath = this.baseUrl +"/accounts";
        try {
            ResponseEntity<List<Account>> result = executeGET(requestPath,  new ParameterizedTypeReference<List<Account>>(){});
            return result.getBody();
        } catch (HttpStatusCodeException e) {
            logger.error("Unable to get response due to [" + e.getResponseBodyAsString() + "]");
        }        catch (Exception ex) {
            logger.error("Unable to get response due to [" + ex.getMessage() + "]", ex);
        }
        return new ArrayList<>();
    }

    /**
     * Information for a single account. Use this endpoint when you know the accountId.
     * @param accountId
     */
    public Account getAnAccount(String accountId){
        String requestPath = this.baseUrl + "/accounts/" + accountId;
        try {
            ResponseEntity<Account> result = executeGET(requestPath, Account.class);
            return result.getBody();

        } catch (HttpStatusCodeException e) {
            logger.error("Unable to get response due to [" + e.getResponseBodyAsString() + "]");
        }
        catch (Exception ex) {
            logger.error("Unable to get response due to [" + ex.getMessage() + "]", ex);
        }
        return null;
    }

    /**
     * List account activity. Account activity either increases or decreases your account balance.
     * Items are paginated and sorted latest first. See the Pagination section for retrieving additional entries after the first page.
     * TODO: Check pagination
     * @param accountId
     * @return list of account activities
     */
    public List<AccountActivity> getAccountHistory(String accountId){
        String requestPath = this.baseUrl + "/accounts/" + accountId + "/ledger";
        try {
            ResponseEntity<List<AccountActivity>> result = executeGET(requestPath,  new ParameterizedTypeReference<List<AccountActivity>>(){});
            return result.getBody();

        } catch (HttpStatusCodeException e) {
            logger.error("Unable to get response due to [" + e.getResponseBodyAsString() + "]");
        }
        catch (Exception ex) {
            logger.error("Unable to get response due to [" + ex.getMessage() + "]", ex);
        }
        return new ArrayList<>();
    }

    /**
     * Holds are placed on an account for any active orders or pending withdraw requests.
     * As an order is filled, the hold amount is updated. If an order is canceled, any remaining hold is removed.
     * For a withdraw, once it is completed, the hold is removed.
     *
     * @param accountId
     * @return
     */
    public List<Hold> getHolds(String accountId){
        String requestPath = this.baseUrl + "/accounts/" + accountId + "/holds";
        try {
            ResponseEntity<List<Hold>> result = executeGET(requestPath,  new ParameterizedTypeReference<List<Hold>>(){});
            return result.getBody();

        } catch (HttpStatusCodeException e) {
            logger.error("Unable to get response due to [" + e.getResponseBodyAsString() + "]");
        }
        catch (Exception ex) {
            logger.error("Unable to get response due to [" + ex.getMessage() + "]", ex);
        }
        return new ArrayList<>();
    }





}
