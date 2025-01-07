package com.coupon.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

@Entity
@Table(name = "Coupon")
public class CouponEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "expired_date")
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private Date expired_date;

    @Column(name = "confirm", nullable = false)
    private Boolean confirm = true;

    @Column(name = "used_status", nullable = false)
    private Boolean status = true;

    @Column(name = "transfer_status", nullable = false)
    private Boolean transfer_status = true;

    @ManyToOne
    @JoinColumn(name = "purchase_id", nullable = false)
    private PurchaseEntity purchase;

    @ManyToOne
    @JoinColumn(name = "package_id", nullable = false)
    private PackageEntity packageEntity;

    @OneToOne(mappedBy = "coupon", cascade = CascadeType.ALL, orphanRemoval = true)
    private QREntity qr;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getExpired_date() {
        return expired_date;
    }

    public void setExpired_date(Date expired_date) {
        this.expired_date = expired_date;
    }

    public Boolean getConfirm() {
        return confirm;
    }

    public void setConfirm(Boolean confirm) {
        this.confirm = confirm;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getTransfer_status() {
        return transfer_status;
    }

    public void setTransfer_status(Boolean transfer_status) {
        this.transfer_status = transfer_status;
    }

    public PurchaseEntity getPurchase() {
        return purchase;
    }

    public void setPurchase(PurchaseEntity purchase) {
        this.purchase = purchase;
    }

    public PackageEntity getPackageEntity() {
        return packageEntity;
    }

    public void setPackageEntity(PackageEntity packageEntity) {
        this.packageEntity = packageEntity;
    }

    public QREntity getQr() {
        return qr;
    }

    public void setQr(QREntity qr) {
        this.qr = qr;
        if (qr != null) {
            qr.setCoupon(this);
        }
    }
}
