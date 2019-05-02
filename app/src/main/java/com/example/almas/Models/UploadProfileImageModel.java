package com.example.almas.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadProfileImageModel {
    @SerializedName("base64Image")
    @Expose
    private String base64Image;
    @SerializedName("userName")
    @Expose
    private String userName;

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
