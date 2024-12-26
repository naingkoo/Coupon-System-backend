package com.coupon.model;

import java.sql.Date;
import java.time.LocalDate;

import org.springframework.stereotype.Component;

@Component
public class UserDTO {

    private Integer id;

    private String name;

    private String email;

    private  String password;

    private String phone;

    private String role;

    private LocalDate register_date;

    private String token;

    public UserDTO() {

    }

    public UserDTO(String name, String email, String password, String phone, String role, LocalDate register_date, String token) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.register_date = register_date;
        this.token = token;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDate getRegister_date() {
        return register_date;
    }

    public void setRegister_date(LocalDate register_date) {
        this.register_date = register_date;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}