package com.first.hello.dao;

import com.first.hello.entity.Order;
import com.first.hello.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDAO extends JpaRepository<Order, Integer> {
    List<Order> findByUser(User user);
}
