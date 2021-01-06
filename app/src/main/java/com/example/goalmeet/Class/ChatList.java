package com.example.goalmeet.Class;

public class ChatList {
    private String id;
    private String idSender;
    public ChatList(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ChatList(String id) {
        this.id = id;
    }

    public String getIdSender() {
        return idSender;
    }

    public void setIdSender(String idSender) {
        this.idSender = idSender;
    }
}
