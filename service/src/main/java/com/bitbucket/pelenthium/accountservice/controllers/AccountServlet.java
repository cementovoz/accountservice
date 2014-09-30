package com.bitbucket.pelenthium.accountservice.controllers;

import com.bitbucket.pelenthium.accountservice.services.AccountService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by cementovoz on 19.08.14.
 */
@Controller
@RequestMapping(value = "/json")
public class AccountServlet {
    private static final Logger LOG = LogManager.getLogger(AccountServlet.class);

    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/{accountId}", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public Long getAccount(@PathVariable Integer accountId) {
        return accountService.getAmount(accountId);
    }

    @RequestMapping(value = "/{accountId}", method = RequestMethod.PUT, produces = "application/json")
    @ResponseBody
    public boolean addAmount(@PathVariable Integer accountId, HttpServletRequest request) {
        try {
            Long amount = Long.valueOf(request.getParameter("amount"));
            accountService.addAmount(accountId, amount);
        } catch (Exception exc) {
            LOG.error("Failed add amount.", exc);
            return false;
        }
        return true;
    }

}
