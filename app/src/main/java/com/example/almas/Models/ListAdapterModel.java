package com.example.almas.Models;

public class ListAdapterModel {
    int Id;
    String Text;
    String Status;

    public void setStatus(String status) {
        Status = status;
    }

    public String  getStatus() {
        return Status;
    }

    public void setText(String text) {
        Text = text;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getText() {
        return Text;
    }

    public int getId() {
        return Id;
    }
}
