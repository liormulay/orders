package com.first.hello.rest;

import com.first.hello.configuration.JwtTokenUtil;
import com.first.hello.dao.UserDAO;
import com.first.hello.entity.User;
import com.first.hello.model.JwtTokenResponse;
import com.first.hello.service.UserDetailsServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Controller to register and authenticate the user
 */
@Controller
@CrossOrigin
public class JwtAuthenticationController {

    @Autowired
    UserDetailsServiceImp userDetailsService;

    @Autowired
    JwtTokenUtil tokenUtil;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserDAO userDAO;

    @RequestMapping(value = "/is-username-exist/{username}", method = RequestMethod.GET)
    public ResponseEntity<?> isUsernameExist(@PathVariable String username) {
        return ResponseEntity.ok(userDAO.findByUserName(username) != null);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody User user) {
        return ResponseEntity.ok(userDetailsService.save(user));
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> authenticate(@RequestBody User user) throws Exception {
        authenticate(user.getUserName(), user.getPassword());
        user = userDAO.findByUserName(user.getUserName());
        String token = tokenUtil.generateToken(user);
        user.setPassword(null);
        return ResponseEntity.ok(new JwtTokenResponse(token, user));

    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
