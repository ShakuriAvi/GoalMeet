package com.example.goalmeet.Class;

public class User {
    private String id;
    private String city;
    private String nameClub;
    private String description;
    private String userName;
    private String imageURL;
    private String status;
    private boolean isManager;

    public User(){}
    public User(String id, String userName, String imageURL, String status, String city, String nameClub, String description, boolean isManager) {
        this.id = id;
        this.userName = userName;
        this.imageURL = imageURL;
        this.status = status;
        this.city =city;
        this.nameClub = nameClub;
        this.description = description;
        this.isManager = isManager;
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImageURL() {
        return imageURL;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNameClub() {
        return nameClub;
    }

    public void setNameClub(String nameClub) {
        this.nameClub = nameClub;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getIsManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }
}
