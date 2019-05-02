package com.example.almas.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ChargeModel {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("isEnable")
    @Expose
    private boolean isEnable;
    @SerializedName("chargeDetails")
    @Expose
    private ArrayList<ChargeDetailModel> chargeDetails = null;

    public Integer getId() {
        return id;
    }
    public boolean getIsEnable() {
        return isEnable;
    }
    public void setId(boolean isEnable) {
        this.isEnable = isEnable;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ArrayList<ChargeDetailModel> getChargeDetails() {
        return chargeDetails;
    }

    public void setChargeDetails(ArrayList<ChargeDetailModel> chargeDetails) {
        this.chargeDetails = chargeDetails;
    }

}
