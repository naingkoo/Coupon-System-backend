package com.coupon.model;

import com.coupon.entity.ConfirmStatus;
import com.coupon.entity.PackageEntity;
import com.coupon.entity.PurchaseEntity;
import com.coupon.entity.QREntity;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class CouponDTO1 {
    private Integer id;

    private Date expired_date;
    private ConfirmStatus confirm ;
    private Boolean used_status = true;
    private Boolean transfer_status = true;
    private PurchaseDTO purchase;
    private PackageDTO packageDTO;
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

    public PurchaseDTO getPurchase() {
        return purchase;
    }

    public void setPurchase(PurchaseDTO purchase) {
        this.purchase = purchase;
    }

    public PackageDTO getPackageDTO() {
        return packageDTO;
    }

    public void setPackageDTO(PackageDTO packageDTO) {
        this.packageDTO = packageDTO;
    }

    public QREntity getQr() {
        return qr;
    }

    public void setQr(QREntity qr) {
        this.qr = qr;
    }
}
