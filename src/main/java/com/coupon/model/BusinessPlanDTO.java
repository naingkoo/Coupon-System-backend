package com.coupon.model;

import com.coupon.entity.BusinessEntity;
import com.coupon.entity.PlanEntity;

import java.util.Date;

public class BusinessPlanDTO {

    private Integer id;

    private Double total_amount;

    private String paymentType;

    private String transaction_id;

    private Date payment_date;

    private Integer businessId;

    private Integer planId;

    private Integer max_package;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(Double total_amount) {
        this.total_amount = total_amount;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public Date getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(Date payment_date) {
        this.payment_date = payment_date;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public Integer getMax_package() {
        return max_package;
    }

    public void setMax_package(Integer max_package) {
        this.max_package = max_package;
    }
}
