package com.bitbucket.pelenthium.accountservice.service;

import com.bitbucket.pelenthium.accountservice.JavaTestConfig;
import com.bitbucket.pelenthium.accountservice.domains.Account;
import com.bitbucket.pelenthium.accountservice.services.AccountService;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * Created by cementovoz on 20.08.14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = JavaTestConfig.class)
public class AccountServiceTests extends TestCase {

    @Autowired
    private AccountService accountService;

    @Test
    public void testSave () {
        Account account = new Account();
        accountService.save(account);
        assertNotNull(account.getId());
    }

    @Test
    public void testGetAmount () {
        assertEquals(0L, accountService.getAmount(125).longValue());
        Account account = new Account(65, 0L);
        account.setAmount(3500L);
        accountService.save(account);
        assertNotNull(accountService.getAmount(account.getId()));
        assertEquals(3500L, accountService.getAmount(account.getId()).longValue());
    }

    @Test
    public void testAddAmount () {
        Account account = new Account(56, 0L);
        accountService.save(account);
        assertNotNull(accountService.getAmount(account.getId()));
        assertEquals(0L, accountService.getAmount(account.getId()).longValue());
        accountService.addAmount(account.getId(), 1500L);
        assertEquals(1500L, accountService.getAmount(account.getId()).longValue());
        accountService.addAmount(account.getId(), 500L);
        assertEquals(2000L, accountService.getAmount(account.getId()).longValue());
    }

}
