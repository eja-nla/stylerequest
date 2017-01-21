package com.hair.business.dao.datastore.repository;

import static com.hair.business.dao.datastore.ofy.OfyService.ofy;

import com.googlecode.objectify.TxnType;
import com.hair.business.dao.datastore.stereotype.Transact;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Created by olukoredeaguda on 21/01/2017.
 *
 * Transaction interceptor
 *
 * Lifted from https://github.com/objectify/objectify/wiki/Transactions
 */
public class TransactInterceptor implements MethodInterceptor {

    /** Work around java's annoying checked exceptions */
    private static class ExceptionWrapper extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public ExceptionWrapper(Throwable cause) {
            super(cause);
        }

        /** This makes the cost of using the ExceptionWrapper negligible */
        @Override
        public synchronized Throwable fillInStackTrace() {
            return this;
        }
    }

    /** The only trick here is that we need to wrap & unwrap checked exceptions that go through the Work interface */
    @Override
    public Object invoke(final MethodInvocation inv) throws Throwable {
        Transact attr = inv.getStaticPart().getAnnotation(Transact.class);
        TxnType type = attr.value();

        try {
            return ofy().execute(type, () -> {
                try {
                    return inv.proceed();
                }
                catch (RuntimeException ex) { throw ex; }
                catch (Throwable th) { throw new ExceptionWrapper(th); }
            });
        } catch (ExceptionWrapper ex) { throw ex.getCause(); }
    }
}
