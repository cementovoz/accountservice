package com.bitbucket.pelenthium.accountservice.service;

import com.bitbucket.pelenthium.accountservice.JavaTestConfig;
import com.bitbucket.pelenthium.accountservice.services.AccountService;
import com.bitbucket.pelenthium.accountservice.services.StatisticService;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Random;

/**
 * Created by cementovoz on 20.08.14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = JavaTestConfig.class)
public class StatisticServiceTests extends TestCase {

    @Autowired
    private AccountService accountService;
    @Autowired
    private StatisticService statisticService;

    @Before
    public void startTest () {
        statisticService.clearCounter();
    }

    @Test
    public void testCountGet () {
        assertEquals(0, statisticService.getCountGet());
        accountService.getAmount(154);
        assertEquals(1, statisticService.getCountGet());
    }

    @Test
    public void testCountGetMany () {
        for (int i=0; i < 15; i++) {
            accountService.getAmount(i);
        }
        assertEquals(15, statisticService.getCountGet());
    }

    //@Test
    public void testCountAdd () {
        assertEquals(0, statisticService.getCountAdd());
        accountService.addAmount(250, 5265L);
        assertEquals(1, statisticService.getCountAdd());
    }

    //@Test
    public void testCountAddMany () {
        for (int i=0; i < 15; i++) {
            accountService.addAmount(i + 100, 5468L);
        }
        assertEquals(15, statisticService.getCountAdd());
    }

    //@Test
    public void testClearCounter () {
        accountService.getAmount(34);
        assertEquals(1, statisticService.getCountGet());
        accountService.addAmount(34, 5463L);
        assertEquals(1, statisticService.getCountAdd());
        statisticService.clearCounter();
        assertEquals(0, statisticService.getCountAdd());
        assertEquals(0, statisticService.getCountGet());
    }
}
