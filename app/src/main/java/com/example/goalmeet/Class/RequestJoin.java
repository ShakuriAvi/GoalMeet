package com.example.goalmeet.Class;

public class RequestJoin {
    private String sender;
    private String receiver;
    private String nameSender;
    private String nameTeam;
    private boolean seen;
    private boolean confirmApplication;
    public RequestJoin(){}
    public RequestJoin( String sender, String receiver,boolean seen,boolean confirmApplication,String nameSender,String nameTeam){
        this.sender = sender;
        this.receiver = receiver;
        this.seen = seen;
        this.confirmApplication = confirmApplication;
        this.nameSender = nameSender;
        this.nameTeam = nameTeam;
    }
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public boolean getSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }



    public boolean getConfirmApplication() {
        return confirmApplication;
    }

    public void setConfirmApplication(boolean confirmApplication) {
        this.confirmApplication = confirmApplication;
    }

    public String getNameSender() {
        return nameSender;
    }

    public void setNameSender(String nameSender) {
        this.nameSender = nameSender;
    }

    public String getNameTeam() {
        return nameTeam;
    }

    public void setNameTeam(String nameTeam) {
        this.nameTeam = nameTeam;
    }
}
