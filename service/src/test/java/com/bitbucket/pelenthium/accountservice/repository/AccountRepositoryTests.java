package com.bitbucket.pelenthium.accountservice.repository;

import com.bitbucket.pelenthium.accountservice.JavaTestConfig;
import com.bitbucket.pelenthium.accountservice.domains.Account;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by cementovoz on 20.08.14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = JavaTestConfig.class)
@Transactional(readOnly = false)
public class AccountRepositoryTests extends TestCase {


    @Autowired
    private AccountRepository accountRepository;


    @Test
    public void testSetId () {
        Account account = new Account(100, 233);
        accountRepository.save(account);
        assertEquals(100, account.getId().intValue());
        Account load = accountRepository.getById(100);
        assertNotNull(load);
        assertEquals(100, load.getId().intValue());
        assertEquals(233, load.getAmount().intValue());
    }

    @Test
    public void testSaveAccount () {
        Account account = new Account();
        account.setAmount(500L);
        accountRepository.save(account);
        assertNotNull(account.getId());
        assertTrue(account.getId() > 0);
    }

    @Test
    public void testSaveAndLoadAccount () {
        Account account = new Account();
        account.setAmount(3500L);
        accountRepository.save(account);
        Account load = accountRepository.getById(account.getId());
        assertNotNull(account.getId());
        assertNotNull(load.getId());
        assertEquals(account.getId(), load.getId());
        assertEquals(account.getAmount(), load.getAmount());
    }

    @Test
    public void testGetAmount () {
        Account account = new Account();
        account.setAmount(3500L);
        accountRepository.save(account);
        Long amount = accountRepository.getAmount(account.getId());
        assertNotNull(amount);
        assertEquals(3500L, amount.longValue());
    }

    @Test
    public void testGetNotExistAccount() {
        Account account = accountRepository.getById(1568);
        assertNull(account);
        Long amont = accountRepository.getAmount(1568);
        assertEquals(0L, amont.longValue());
    }


    @Test
    public void testAddAmount () {
        accountRepository.addAmount(1000, 500L);
        assertEquals(500L, accountRepository.getAmount(1000).longValue());
    }
}
