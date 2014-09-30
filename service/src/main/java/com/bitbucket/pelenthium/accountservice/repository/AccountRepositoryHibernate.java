package com.bitbucket.pelenthium.accountservice.repository;


import com.bitbucket.pelenthium.accountservice.domains.Account;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cementovoz on 19.08.14.
 */
@org.springframework.stereotype.Repository
public class AccountRepositoryHibernate implements AccountRepository {

    private static final Logger LOG = LogManager.getLogger(AccountRepositoryHibernate.class);
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Long getAmount(Integer accountId) {
        LOG.debug("getAmount call");
        Account account = getById(accountId);
        return account == null ? 0 : account.getAmount();
    }

    @Override
    public void addAmount(Integer accountId, Long amount) {
        Session currentSession = sessionFactory.getCurrentSession();
        Account account = (Account) currentSession.get(Account.class, accountId, new LockOptions(LockMode.PESSIMISTIC_WRITE));
        if (account != null) {
            account.setAmount(account.getAmount() + amount);
            currentSession.update(account);
        } else {
            account = new Account(accountId, amount);
            currentSession.save(account);
        }
    }

    @Override
    public Account getById(Integer id) {
        return (Account) sessionFactory.getCurrentSession().get(Account.class, id);
    }

    @Override
    public void save(Account object) {
        sessionFactory.getCurrentSession().save(object);
    }
}
