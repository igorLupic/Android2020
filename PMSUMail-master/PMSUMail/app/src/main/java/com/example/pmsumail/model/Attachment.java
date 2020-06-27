package com.example.pmsumail.model;

import android.util.Base64;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Attachment {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("data")
    @Expose
    private Base64 data;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("messages")
    @Expose
    private ArrayList<Message> messages;

    public Attachment() {
    }

    public Attachment(int id, Base64 data, String type, String name, ArrayList<Message> messages) {
        this.id = id;
        this.data = data;
        this.type = type;
        this.name = name;
        this.messages = messages;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Base64 getData() {
        return data;
    }

    public void setData(Base64 data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
