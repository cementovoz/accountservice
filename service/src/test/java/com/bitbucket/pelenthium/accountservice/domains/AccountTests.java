package com.bitbucket.pelenthium.accountservice.domains;

import junit.framework.TestCase;

/**
 * Created by cementovoz on 19.08.14.
 */
public class AccountTests extends TestCase {

    public void testAccountConstructor () {
        Account account = new Account(42, 53L);
        assertEquals(53L, account.getAmount().longValue());
        assertEquals(42, account.getId().intValue());
    }

    public void testAccountSetters () {
        Account account = new Account();
        assertEquals(0L, account.getAmount().longValue());
        assertNull(account.getId());

        account.setAmount(500L);
        assertEquals(500L, account.getAmount().longValue());

        account.setId(42);
        assertEquals(42, account.getId().intValue());
    }
}
