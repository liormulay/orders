package com.first.hello.service;

import org.springframework.stereotype.Service;

@Service
public interface SecurityService {
    /**
     *
     * @return the username of current logged in user
     */
    String findLoggedInUserName();
}
