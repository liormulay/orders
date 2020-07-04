package com.first.hello.model;

import com.first.hello.entity.User;

import java.io.Serializable;

/**
 * Model for send to user the token
 */
public class JwtTokenResponse implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;

    private final String token;

    private final User user;

    public JwtTokenResponse(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}
