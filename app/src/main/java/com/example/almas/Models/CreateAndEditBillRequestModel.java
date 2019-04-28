package com.example.almas.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateAndEditBillRequestModel {
    @SerializedName("billId")
    @Expose
    private Integer billId;
    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("endDateFa")
    @Expose
    private String endDateFa;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("billType")
    @Expose
    private Integer billType;
    @SerializedName("base64Image")
    @Expose
    private String base64Image;

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getEndDateFa() {
        return endDateFa;
    }

    public void setEndDateFa(String endDateFa) {
        this.endDateFa = endDateFa;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }
}
