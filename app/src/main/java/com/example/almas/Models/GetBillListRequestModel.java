package com.example.almas.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetBillListRequestModel {
    @SerializedName("billType")
    @Expose
    private Integer billType;
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("fromDateFa")
    @Expose
    private String fromDateFa;
    @SerializedName("toDateFa")
    @Expose
    private String toDateFa;

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getFromDateFa() {
        return fromDateFa;
    }

    public void setFromDateFa(String fromDateFa) {
        this.fromDateFa = fromDateFa;
    }

    public String getToDateFa() {
        return toDateFa;
    }

    public void setToDateFa(String toDateFa) {
        this.toDateFa = toDateFa;
    }
}
