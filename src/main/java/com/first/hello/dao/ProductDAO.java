package com.first.hello.dao;

import com.first.hello.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDAO extends JpaRepository<Product, Integer> {
}
