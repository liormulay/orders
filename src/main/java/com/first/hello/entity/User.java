package com.first.hello.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
public class User implements Serializable {

    private static final long serialVersionUID = 5823099038943060217L;

    @Id
    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;


    @Column(name = "role")
    private String role;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Order> orders;

    public static final String ROLE_CUSTOMER = "ROLE_CUSTOMER";

    public User(String username, String password) {
        this.userName = username;
        this.password = password;
        role = ROLE_CUSTOMER;
    }

    public User() {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public void addOrder(Order order) {
        if (orders == null) {
            orders = new ArrayList<>();
        }
        orders.add(order);
        order.setUser(this);
    }
}
