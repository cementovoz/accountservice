package com.bitbucket.pelenthium.accountservice.repository;

import com.bitbucket.pelenthium.accountservice.domains.Account;

/**
 * Created by cementovoz on 19.08.14.
 */
public interface AccountRepository extends Repository<Account> {

    /**
     * Get amount from object with id, if object with id isn't represent in database, method return zero.
     * @param accountId
     * @return
     */
    Long getAmount(Integer accountId);

    /**
     * Add amount to object with id, if object isn't represent in database, method create object with id and set amount.
     * @param accountId
     * @param amount
     */
    void addAmount(Integer accountId, Long amount);
}
