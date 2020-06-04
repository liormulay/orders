package com.first.hello.configuration;

import com.first.hello.service.UserDetailsServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    UserDetailsServiceImp userDetailsServiceImp;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse
            , FilterChain filterChain) throws ServletException, IOException {
        String authenticateHeader = httpServletRequest.getHeader("Authenticate");
        String token = null;
        String userName = null;
        if (authenticateHeader != null && authenticateHeader.startsWith("Bearer ")) {
            token = authenticateHeader.substring(7);
            try {
                userName = jwtTokenUtil.getUserNameFromToken(token);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }

        if (userName != null && SecurityContextHolder.getContext().getAuthentication() != null) {
            UserDetails userDetails = userDetailsServiceImp.loadUserByUsername(userName);
            if (jwtTokenUtil.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                        = new UsernamePasswordAuthenticationToken(userDetails, null, new ArrayList<>());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
