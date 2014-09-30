package com.bitbucket.pelenthium.accountservice.controllers;

import com.bitbucket.pelenthium.accountservice.JavaTestConfig;
import com.bitbucket.pelenthium.accountservice.domains.Account;
import com.bitbucket.pelenthium.accountservice.services.AccountService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by cementovoz on 21.08.14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = JavaTestConfig.class)
@WebAppConfiguration
@EnableTransactionManagement
public class AccountControllerTests{

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype()
    );

    private MockMvc mockMvc;
    private Account account;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountServlet accountServlet;
    
    @Before
    public void setup () {
        account = new Account();
        account.setAmount(5000L);
        accountService.save(account);
        mockMvc = MockMvcBuilders.standaloneSetup(accountServlet).build();
    }

    @Test
    public void testGetAmountZero () throws Exception {
        mockMvc.perform(get("/json/12"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string("0"));
    }

    @Test
    public void testGetAmount () throws Exception {
        mockMvc.perform(get("/json/"  + account.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(account.getAmount().toString()));
    }

    @Test
    public void testAddMount () throws Exception  {
        mockMvc.perform(put("/json/"  + account.getId()).param("amount", "2500"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string("true"));

        mockMvc.perform(get("/json/"  + account.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string("7500"));
    }

    @Test
    public void testGetAmountMultiThreads () throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Collection<Callable<Long>> taskLists  = new ArrayList<>(10);
        for (int i=0; i<100; i++) {
            taskLists.add(getGetMountTask(account.getId()));
        }
        List<Future<Long>> futures = executorService.invokeAll(taskLists);
        for (Future future : futures) {
            Assert.assertEquals(future.get(), 5000L);
        }
    }

    @Test
    public void testAddAmountMultiThreadsNotExistsAccount () throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Collection<Callable<Void>> taskLists  = new ArrayList<>(10);
        int accountId = 568;
        for (int i=0; i<100; i++) {
            taskLists.add(getAddMountTask(accountId, 500L));
        }
        executorService.invokeAll(taskLists);
        mockMvc.perform(get("/json/"  + accountId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(String.valueOf(100 * 500)));
    }

    @Test
    public void testAddAmountMultiThreads () throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Collection<Callable<Void>> taskLists  = new ArrayList<>(10);
        for (int i=0; i<100; i++) {
            taskLists.add(getAddMountTask(account.getId(), 500L));
        }
        executorService.invokeAll(taskLists);
        mockMvc.perform(get("/json/"  + account.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(String.valueOf(account.getAmount() + 100 * 500)));
    }

    private Callable<Long> getGetMountTask(final Integer id) {
        return new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                MvcResult mvcResult = mockMvc.perform(get("/json/" + id)).andReturn();
                return Long.valueOf(mvcResult.getResponse().getContentAsString());
            }
        };
    }

    private Callable<Void> getAddMountTask(final Integer id, final Long amount) {
        return new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                    mockMvc.perform(put("/json/" + id).param("amount", amount.toString()));
                    return null;
                }
        };
    }
}
