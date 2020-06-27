package com.example.pmsumail.model.requestbody;

public class MessageReadRequestBody {
    private int id;

    public MessageReadRequestBody(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
