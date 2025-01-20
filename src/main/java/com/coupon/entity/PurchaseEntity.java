package com.coupon.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

@Entity
@Table(name="Purchase")
public class PurchaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="total_amount")
    private Double total_amount;

    @Column(name="total_quantity")
    private Integer total_quantity;

    @Column(name="payment_type")
    private String payment_type;

    @Column(name = "transaction_id", unique = true)
    private String transaction_id;

    @Column(name="purchase_date")
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private Date purchase_date;

    @Enumerated(EnumType.STRING) // Store as a string in the database
    @Column(name = "confirm", nullable = false)
    private ConfirmStatus confirm = ConfirmStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private UserEntity user;

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

    public Integer getTotal_quantity() {
        return total_quantity;
    }

    public void setTotal_quantity(Integer total_quantity) {
        this.total_quantity = total_quantity;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public Date getPurchase_date() {
        return purchase_date;
    }

    public void setPurchase_date(Date purchase_date) {
        this.purchase_date = purchase_date;
    }

    public ConfirmStatus getConfirm() {
        return confirm;
    }

    public void setConfirm(ConfirmStatus confirm) {
        this.confirm = confirm;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
