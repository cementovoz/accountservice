package com.bitbucket.pelenthium.accountservice.services;

import com.bitbucket.pelenthium.accountservice.annotations.RetryingTransactional;
import com.bitbucket.pelenthium.accountservice.domains.Account;
import com.bitbucket.pelenthium.accountservice.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by cementovoz on 19.08.14.
 */

@Service
@Transactional(readOnly = true)
public class AccountServiceTransactional implements AccountService {
    @Autowired
    private AccountRepository accountRepositoryHibernate;
    @Autowired
    private StatisticService statisticService;


    @Override
    public Long getAmount(Integer accountId) {
        statisticService.incrementGet();
        return accountRepositoryHibernate.getAmount(accountId);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    @RetryingTransactional
    @Override
    public void addAmount(Integer accountId, Long amount) {
        statisticService.incrementAdd();
        accountRepositoryHibernate.addAmount(accountId, amount);
    }

    @Transactional(readOnly = false)
    @Override
    public void save(Account account) {
        accountRepositoryHibernate.save(account);
    }
}
