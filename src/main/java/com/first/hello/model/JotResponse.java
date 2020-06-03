package com.first.hello.model;

import java.io.Serializable;

public class JotResponse implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;

    private final String token;

    public JotResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}