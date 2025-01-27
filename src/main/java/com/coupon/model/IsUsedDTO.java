package com.coupon.model;

import java.util.Date;

public class IsUsedDTO {

    private Integer id;

    private Date used_date;

    private CouponDTO coupon;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getUsed_date() {
        return used_date;
    }

    public void setUsed_date(Date used_date) {
        this.used_date = used_date;
    }

    public CouponDTO getCoupon() {
        return coupon;
    }

    public void setCoupon(CouponDTO coupon) {
        this.coupon = coupon;
    }
}
