package com.coupon.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name="Business_Plan")
public class BusinessPlanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "total_amount")
    private Double total_amount;

    @Column(name = "paymentType")
    private String paymentType;

    @Column(name = "transaction_id")
    private Integer transaction_id;

    @Column(name = "payment_date")
    @DateTimeFormat(pattern = "MM/dd/yy")
    private Date payment_date;

    @ManyToOne
    @JoinColumn(name = "business_id", nullable = false)
    private BusinessEntity business;

    @ManyToOne
    @JoinColumn(name = "Plan_id", nullable = false)
    private PlanEntity plan;

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

    public Integer getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(Integer transaction_id) {
        this.transaction_id = transaction_id;
    }

    public Date getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(Date payment_date) {
        this.payment_date = payment_date;
    }

    public BusinessEntity getBusiness() {
        return business;
    }

    public void setBusiness(BusinessEntity business) {
        this.business = business;
    }

    public PlanEntity getPlan() {
        return plan;
    }

    public void setPlan(PlanEntity plan) {
        this.plan = plan;
    }
}
