package com.coupon.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "paid_coupon")
public class PaidCouponEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "coupon_id", nullable = false, unique = true)
    private CouponEntity coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "revenue_id", nullable = false)
    private RevenueEntity revenueEntity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CouponEntity getCoupon() {
        return coupon;
    }

    public void setCoupon(CouponEntity coupon) {
        this.coupon = coupon;
    }

    public RevenueEntity getRevenueEntity() {
        return revenueEntity;
    }

    public void setRevenueEntity(RevenueEntity revenueEntity) {
        this.revenueEntity = revenueEntity;
    }
}
