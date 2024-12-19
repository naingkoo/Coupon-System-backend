package com.coupon.responObject;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
public class HttpResponse<T>{
    private Date date;
    private HttpStatus httpStatus;
    private int httpStatusCode;
    private T data;
    private String message;

    public Date getDate() {
        return  this.date=new Date();
    }


    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
