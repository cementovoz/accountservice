package com.bitbucket.pelenthium.accountservice.services;

import com.bitbucket.pelenthium.accountservice.domains.Account;

/**
 * Created by cementovoz on 19.08.14.
 */

public interface AccountService {
    /**
     * Retrieves current balance or zero if addAmount() method was not called before for specified id
     *
     * @param id balance identifier
     * @return
     */
    Long getAmount(Integer id);

    /**
     * Increases balance or set if addAmount() method was called first time
     *
     * @param id    balance identifier
     * @param value positive or negative value, which must be added to current balance
     */
    void addAmount(Integer id, Long value);

    /**
     * Save object to database
     * @param account
     */
    void save(Account account);
}
