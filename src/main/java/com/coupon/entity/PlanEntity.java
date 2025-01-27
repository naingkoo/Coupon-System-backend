package com.coupon.entity;

import jakarta.persistence.*;

@Entity
@Table(name="plan")
public class PlanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="name")
    private String name;

    @Column(name="max_packages")
    private Integer max_packages;

    @Column(name="price")
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
