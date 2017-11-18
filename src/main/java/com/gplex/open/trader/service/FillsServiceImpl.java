package com.gplex.open.trader.service;

import com.gplex.open.trader.domain.FillResponse;
import com.gplex.open.trader.rest.BaseSecureClient;
import com.gplex.open.trader.utils.Security;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * Created by Vlad S. on 9/14/17.
 */
public class FillsServiceImpl extends BaseSecureClient {

    private static final Logger logger = LoggerFactory.getLogger(FillsServiceImpl.class);

    private final String baseUrl;

    public FillsServiceImpl(RestTemplate restTemplate, Security sec, String key, String passphrase, String baseUrl) {
        super(restTemplate, sec, key, passphrase);
        this.baseUrl = baseUrl;
    }

    public List<FillResponse> listFills() {
        return listFills(new String[0], new String[0]);
    }



    public List<FillResponse> listFills(Collection<String> orderIds) {
        List<FillResponse> fills = listFills();
        Set<String> set = new HashSet<>(orderIds);
        return  fills.stream().filter(f -> f != null && set.contains(f.getOrderId())).collect(toList());

    }

    public List<FillResponse> listFills(String... orderIds) {
        List<FillResponse> fills = listFills();
        Set<String> set = new HashSet<>(Arrays.asList(orderIds));
        return  fills.stream().filter(f -> f != null && set.contains(f.getOrderId())).collect(toList());

    }

    public FillResponse listFillsByOrderId(String orderId) {
        List<FillResponse> fills = listFills();
        if(fills.size() == 0){
            return null;
        }
        return fills.get(0);
    }


    public List<FillResponse> listFills(String[] orderIds, String[] productIds) {
        String requestPath = this.baseUrl + "/fills";
        if (productIds == null) {
            productIds = new String[0];
        }
        if (orderIds == null) {
            orderIds = new String[0];
        }
        try {
            URIBuilder builder = new URIBuilder(requestPath);
            for (String productId : productIds) {
                builder.addParameter("product_id", productId);
            }
            for (String orderId : orderIds) {
                builder.addParameter("order_id", orderId);
            }
            requestPath = builder.build().toString();
        } catch (Exception e) {
            logger.error("", e);
        }

        try {
            ResponseEntity<List<FillResponse>> result = executeGET(requestPath, new ParameterizedTypeReference<List<FillResponse>>() {
            });
            return result.getBody();
        } catch (HttpStatusCodeException e) {
            logger.error("Unable to get response due to [" + e.getResponseBodyAsString() + "]");
        } catch (Exception ex) {
            logger.error("Unable to get response due to [" + ex.getMessage() + "]", ex);
        }
        return new ArrayList<>();

    }

}
