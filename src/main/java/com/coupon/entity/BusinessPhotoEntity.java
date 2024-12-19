package com.coupon.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "business_photo")
public class BusinessPhotoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "photo_name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "business_id",nullable = false)
    private BusinessEntity business;

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

    public BusinessEntity getBusiness() {
        return business;
    }
    public void setBusiness(BusinessEntity business) {
        this.business = business;
    }
}