package com.hair.business.services.state;

import static com.hair.business.beans.constants.StyleRequestState.ACCEPTED;
import static com.hair.business.beans.constants.StyleRequestState.CANCELLED;
import static com.hair.business.beans.constants.StyleRequestState.PENDING;

import com.hair.business.beans.constants.StyleRequestState;
import com.hair.business.beans.entity.StyleRequest;
import com.hair.business.beans.entity.TransactionResult;
import com.hair.business.beans.helper.PaymentOperation;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.x.business.utilities.Assert;

import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

import javax.inject.Inject;

/**
 * Created by olukoredeaguda on 16/05/2017.
 *
 * Style request state manager impl
 */
public class StylerequestStateMgrImpl implements StylerequestStateMgr {

    private final Repository repository;
    private static final ReentrantLock lock = new ReentrantLock();

    @Inject
    public StylerequestStateMgrImpl(Repository repository) {
        this.repository = repository;
    }


    /**
     * enforce state rules
     * 1. If current and new states are the same, fail loudly
     * 2. PENDING can only state to ACCEPTED
     * 3. ACCEPTED can state anywhere
     * 4. COMPLETED or CANCELLED are immutable
     *
     * Considerations/todo:
     *  we may want to enforce a max time in which the request must be accepted by merchant
     *      should we do that here or on the client side?
     *
     * */
    @Override
    public StyleRequest transition(final Long id, final StyleRequestState newState) {
        Assert.validId(id);
        final StyleRequest styleRequest = repository.findOne(id, StyleRequest.class);
        Assert.notNull(styleRequest, String.format("Transition failure. Style request with ID %s not found", styleRequest));

        Optional<TransactionResult> authorizedPayment = styleRequest.getTransactionResults().stream().filter(t -> t.getOperation().equals(PaymentOperation.AUTHORIZE)).findFirst();
        if (!authorizedPayment.isPresent()){
            throw new IllegalStateException(String.format("Transition failure. Style request {ID=%s} being accepted must have an authorized payment", styleRequest.getId()));
        }

        final StyleRequestState currentState = styleRequest.getState();

        //1
        Assert.isTrue(!newState.equals(currentState), "Transition failure. Style request state and new state are equal.");

        //2
        if (currentState.equals(PENDING)) {
            Assert.isTrue(newState.equals(ACCEPTED), "Transition failure. Pending requests can only be accepted.");
        }

        lock.lock();

        try{
            //if we just check 4 above, the rest will fall into 3

            //4
            Assert.isTrue(!currentState.equals(CANCELLED) && !currentState.equals(StyleRequestState.COMPLETED), "Transition failure. Request state is already terminal.");

            styleRequest.setState(newState);
        } finally {
            lock.unlock();
        }

        return styleRequest;
    }
}
