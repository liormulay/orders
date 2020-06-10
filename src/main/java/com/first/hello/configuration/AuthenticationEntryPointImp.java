package com.first.hello.configuration;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * Take care of the error in authorization
 */
@Component
public class AuthenticationEntryPointImp implements AuthenticationEntryPoint , Serializable {

    private static final long serialVersionUID = -7858869558953243875L;

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e)
            throws IOException {
        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Unauthorized");
    }
}
