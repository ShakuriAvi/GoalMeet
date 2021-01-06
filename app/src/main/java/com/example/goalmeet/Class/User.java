package com.example.goalmeet.Class;

public class User {
    private String id;
    private String userName;
    private String imageURL;
    private String keyTeam;
    private String status;
    public User(){}

    public User(String id, String userName,String imageURL,String status) {
        this.id = id;
        this.userName = userName;
        this.imageURL = imageURL;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getImageURL() {
        return imageURL;
    }


    public String getkeyTeam() {
        return keyTeam;
    }

    public void setkeyTeam(String keyTeam) {
        this.keyTeam = keyTeam;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
