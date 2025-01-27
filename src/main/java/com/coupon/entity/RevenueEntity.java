package com.coupon.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

@Entity
@Table(name = "revenue")
public class RevenueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "pay_date")
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private Date pay_date;

    @Column(name = "from_date")
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private Date from_date;

    @Column(name = "to_date")
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private Date to_date;

    @Column(name="total_amount")
    private Double total_amount;

    @Column(name="pay_amount")
    private Double pay_amount;

    @Column(name="percentage")
    private Integer percentage;

    @Column(name="quantity")
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private BusinessEntity businessEntity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getPay_date() {
        return pay_date;
    }

    public void setPay_date(Date pay_date) {
        this.pay_date = pay_date;
    }

    public Date getFrom_date() {
        return from_date;
    }

    public void setFrom_date(Date from_date) {
        this.from_date = from_date;
    }

    public Date getTo_date() {
        return to_date;
    }

    public void setTo_date(Date to_date) {
        this.to_date = to_date;
    }

    public Double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(Double total_amount) {
        this.total_amount = total_amount;
    }

    public Double getPay_amount() {
        return pay_amount;
    }

    public void setPay_amount(Double pay_amount) {
        this.pay_amount = pay_amount;
    }

    public Integer getPercentage() {
        return percentage;
    }

    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BusinessEntity getBusinessEntity() {
        return businessEntity;
    }

    public void setBusinessEntity(BusinessEntity businessEntity) {
        this.businessEntity = businessEntity;
    }
}
