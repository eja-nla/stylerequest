package com.hair.business.beans.entity.nonPersist;

/**
 * Created by Olukorede Aguda on 12/06/2019.
 *
 * Holds token info for firebase login. Not stored.
 */
public class LoginRequest {

    private String idToken;

    public LoginRequest(){}

    public LoginRequest(String idToken) {
        this.idToken = idToken;
    }

    public String getIdToken() {
        return idToken;
    }
}
