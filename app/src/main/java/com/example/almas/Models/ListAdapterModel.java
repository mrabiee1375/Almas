package com.example.almas.Models;

public class ListAdapterModel {
    int Id;
    String Text;

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
