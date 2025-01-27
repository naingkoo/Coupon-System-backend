package com.coupon.model;

import jakarta.persistence.*;

public class PlanDTO {

    private Integer id;

    private String name;

    private Integer max_packages;

    private double price;

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

    public Integer getMax_packages() {
        return max_packages;
    }

    public void setMax_packages(Integer max_packages) {
        this.max_packages = max_packages;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
