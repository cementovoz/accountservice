package com.bitbucket.pelenthium.accountservice.domains;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import java.io.Serializable;

/**
 * Created by cementovoz on 19.08.14.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Account implements Domain, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "UseIdOrGenerate")
    @GenericGenerator(name = "UseIdOrGenerate",
            strategy = "com.bitbucket.pelenthium.accountservice.repository.hibernate.UseIdOrGenerate")
    private Integer id;

    @Column
    private Long amount = 0L;
    @Column
    @Version
    private Integer version;


    public Account() {
    }

    public Account(int id, long amount) {
        setId(id);
        setAmount(amount);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Account account = (Account) o;

        if (id != null ? !id.equals(account.id) : account.id != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
