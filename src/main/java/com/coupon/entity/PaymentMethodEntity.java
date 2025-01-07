package com.coupon.entity;

import jakarta.persistence.*;

@Entity
@Table(name="payment_method")
public class PaymentMethodEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "payment_name",nullable = false)
    private String paymentType;



    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
    public String getPaymentType() {
        return paymentType;
    }

    @Column(name="image",nullable = false,columnDefinition = "LONGTEXT")
    private String image;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}