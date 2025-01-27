package com.coupon.model;

import java.util.Date;
import java.util.List;

public class RevenueDTO {
    private Integer id;
    private Date payDate;
    private Date fromDate;
    private Date toDate;
    private Double totalAmount;
    private Double payAmount;
    private Integer percentage;
    private Integer quantity;
    private Integer businessId;
    private String businessName;
    private List<PaidCouponDTO> paidCoupons;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Double payAmount) {
        this.payAmount = payAmount;
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

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public List<PaidCouponDTO> getPaidCoupons() {
        return paidCoupons;
    }

    public void setPaidCoupons(List<PaidCouponDTO> paidCoupons) {
        this.paidCoupons = paidCoupons;
    }
}
