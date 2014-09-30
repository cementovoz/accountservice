package com.bitbucket.pelenthium.accountservice.repository.hibernate;

import com.bitbucket.pelenthium.accountservice.domains.Domain;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IncrementGenerator;

import java.io.Serializable;

/**
 * Custom ID generator, if id is set to Domain object, we would like to use it
 */
public class UseIdOrGenerate extends IncrementGenerator {

    @Override
    public Serializable generate(SessionImplementor s, Object obj) {
        if (((Domain) obj).getId() == null) {
            return super.generate(s, obj);
        } else {
            return ((Domain) obj).getId();
        }
    }
}
