package com.coupon.model;


import java.util.Date;

public class CouponDTO {

    private Integer id;
    private Date expired_date;
    public  String confirm;
    private Boolean used_status;
    private Boolean transfer_status;
    private Integer purchase_id;
    private Integer package_id;
    private String packageName;
    private Date purchase_date;
    private Double unit_price;
    private String image;
    private String QR;

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

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
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

    public Integer getPurchase_id() {
        return purchase_id;
    }

    public void setPurchase_id(Integer purchase_id) {
        this.purchase_id = purchase_id;
    }

    public Integer getPackage_id() {
        return package_id;
    }

    public void setPackage_id(Integer package_id) {
        this.package_id = package_id;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Date getPurchase_date() {
        return purchase_date;
    }

    public void setPurchase_date(Date purchase_date) {
        this.purchase_date = purchase_date;
    }

    public Double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(Double unit_price) {
        this.unit_price = unit_price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getQR() {
        return QR;
    }

    public void setQR(String QR) {
        this.QR = QR;
    }
}
