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

    @Enumerated(EnumType.STRING) // Store as a string in the database
    @Column(name = "confirm", nullable = false)
    private ConfirmStatus confirm = ConfirmStatus.PENDING;

    @Column(name = "used_status", nullable = false)
    private Boolean used_status = true;

    @Column(name = "transfer_status", nullable = false)
    private Boolean transfer_status = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id", nullable = false)
    private PurchaseEntity purchase;

    @ManyToOne(fetch = FetchType.LAZY)
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

    public ConfirmStatus getConfirm() {
        return confirm;
    }

    public void setConfirm(ConfirmStatus confirm) {
        this.confirm = confirm;
    }

    public Boolean getUsed_status() {
        return used_status;
    }

    public void setUsed_status(Boolean used_status) {
        this.used_status = used_status;
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
