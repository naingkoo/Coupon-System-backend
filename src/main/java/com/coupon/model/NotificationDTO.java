package com.coupon.model;

import java.util.Date;

public class NotificationDTO {

    private Integer id;

    private Date noti_date;

    private Integer user_id;

    private Integer isUsed_id;

    private Boolean status;

    private Integer transfer_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getNoti_date() {
        return noti_date;
    }

    public void setNoti_date(Date noti_date) {
        this.noti_date = noti_date;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getIsUsed_id() {
        return isUsed_id;
    }

    public void setIsUsed_id(Integer isUsed_id) {
        this.isUsed_id = isUsed_id;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getTransfer_id() {
        return transfer_id;
    }

    public void setTransfer_id(Integer transfer_id) {
        this.transfer_id = transfer_id;
    }
}
