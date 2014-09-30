package com.bitbucket.pelenthium.accountservice.interceptors;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.LockAcquisitionException;
import org.springframework.core.annotation.Order;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

/**
 * Advisor for @RetryingTransactional annotation
 * Created by cementovoz on 26.08.14.
 */
@Aspect
@Component
@Order(1500)
public class RetryingTransactionAdvisor {

    private static final Logger LOG = LogManager.getLogger(RetryingTransactionAdvisor.class);

    @Around(
            value = "@annotation(com.bitbucket.pelenthium.accountservice.annotations.RetryingTransactional)"
    )
    public void retryTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            joinPoint.proceed();
        } catch (DataIntegrityViolationException | CannotAcquireLockException exc) {
            LOG.error("Update account failed, retry transaction");
            joinPoint.proceed();
        }
    }
}
