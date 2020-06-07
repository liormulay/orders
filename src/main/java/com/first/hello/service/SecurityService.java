package com.first.hello.service;

import org.springframework.stereotype.Service;

@Service
public interface SecurityService {
    String findLoggedInUserName();
}
