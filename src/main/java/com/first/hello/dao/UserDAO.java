package com.first.hello.dao;

import com.first.hello.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDAO extends JpaRepository<User, String> {
    User findByUserName(String userName);
}
