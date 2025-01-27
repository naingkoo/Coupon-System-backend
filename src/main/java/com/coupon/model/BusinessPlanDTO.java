package com.coupon.model;

import com.coupon.entity.BusinessEntity;
import com.coupon.entity.PlanEntity;

import java.util.Date;

public class BusinessPlanDTO {

    private Integer id;

    private Double total_amount;

    private String paymentType;

    private Integer transaction_id;

    private Date payment_date;

    private BusinessEntity business;

    private PlanEntity plan;
}
