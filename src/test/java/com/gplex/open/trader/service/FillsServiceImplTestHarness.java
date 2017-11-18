package com.gplex.open.trader.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gplex.open.trader.domain.FillResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Vlad S. on 10/09/17.
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource("classpath:gdax-keys.properties")
public class FillsServiceImplTestHarness {
    private static final Logger logger = LoggerFactory.getLogger(FillsServiceImplTestHarness.class);

    @Autowired
    private AccountsServiceImpl os;
    @Autowired
    private FillsServiceImpl fillsService;



    @Test
    public void testListOrder() throws JsonProcessingException {
        List<FillResponse> response = fillsService.listFills();
        logger.debug("\n{}", response);
    }

    @Test
    public void testListOrderById() throws JsonProcessingException {
        List<FillResponse> response = fillsService.listFills("054770e7-f440-48fa-af0c-2178d5723f40", "529d3695-7cf6-447e-9d68-9f61fb212f40");
        logger.debug("\n{}", response);

    }


    @Test
    public void testListOrderByIdAsSet() throws JsonProcessingException {
        List<FillResponse> response = fillsService.listFills( new HashSet<>(Arrays.asList(new String[]{"054770e7-f440-48fa-af0c-2178d5723f40", "529d3695-7cf6-447e-9d68-9f61fb212f40"})));
        logger.debug("\n{}", response);

    }


    @Test
    public void testListOrderByIdUsingList() throws JsonProcessingException {
        List<FillResponse> response = fillsService.listFills(Arrays.asList(new String[]{"054770e7-f440-48fa-af0c-2178d5723f40", "529d3695-7cf6-447e-9d68-9f61fb212f40"}));
        logger.debug("\n{}", response);
    }




}