package com.example.almas.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BillModel {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("endDateFa")
    @Expose
    private String endDateFa;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("imageAddress")
    @Expose
    private String imageAddress;
    @SerializedName("billType")
    @Expose
    private Integer billType;
    @SerializedName("createdOn")
    @Expose
    private String createdOn;
    @SerializedName("createdOnFa")
    @Expose
    private String createdOnFa;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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

    public String getImageAddress() {
        return imageAddress;
    }

    public void setImageAddress(String imageAddress) {
        this.imageAddress = imageAddress;
    }

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedOnFa() {
        return createdOnFa;
    }

    public void setCreatedOnFa(String createdOnFa) {
        this.createdOnFa = createdOnFa;
    }
}
