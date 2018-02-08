package com.hair.business.services.state;

import com.hair.business.beans.constants.StyleRequestState;
import com.hair.business.beans.entity.StyleRequest;

/**
 * Created by olukoredeaguda on 16/05/2017.
 *
 * Stylerequest state manager
 */
public interface StylerequestStateMgr {

    StyleRequest transition(Long id, StyleRequestState newState);

}
