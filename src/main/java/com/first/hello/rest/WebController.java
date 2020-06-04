package com.first.hello.rest;

import com.first.hello.dao.ProductDAO;
import com.first.hello.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WebController {

    @Autowired
    private ProductDAO productDAO;

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

    @RequestMapping(value = "/products",method = RequestMethod.GET)
    public List<Product> getAllProducts(){
        return productDAO.findAll();
    }


}
