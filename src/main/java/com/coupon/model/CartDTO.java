package com.coupon.model;

public class CartDTO {

    private Integer id;

    private Integer user_id;

    private Integer package_id;

    private Integer unit_quantity;

    private Double unit_price;

    private PackageDTO packageDetails;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getPackage_id() {
        return package_id;
    }

    public void setPackage_id(Integer package_id) {
        this.package_id = package_id;
    }

    public Integer getUnit_quantity() {
        return unit_quantity;
    }

    public void setUnit_quantity(Integer unit_quantity) {
        this.unit_quantity = unit_quantity;
    }

    public Double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(Double unit_price) {
        this.unit_price = unit_price;
    }

    public PackageDTO getPackageDetails() {
        return packageDetails;
    }

    public void setPackageDetails(PackageDTO packageDetails) {
        this.packageDetails = packageDetails;
    }
}
