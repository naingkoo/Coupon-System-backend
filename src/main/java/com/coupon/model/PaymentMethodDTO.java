package com.coupon.model;

public class PaymentMethodDTO {

    private Integer id;

    private String paymentType;

    private String image;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image= image;
    }
}