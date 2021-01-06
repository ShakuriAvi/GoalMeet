package com.example.goalmeet.Class;

import java.util.ArrayList;

public class Team {
    private String name;
    private String city;
    private String nameSymbol;
    private String theManager;
    private ArrayList<String> cadre;
    private String description;
    private boolean fullCadre;
    private String nameManager;
    public Team(String name, String city, String nameSymbol, String theManager, String description) {
        this.name = name;
        this.city = city;
        this.nameSymbol = nameSymbol;
        this.theManager = theManager;
        this.description = description;

    }
    public Team(){}

    public String getNameSymbol() {
        return nameSymbol;
    }

    public void setNameSymbol(String nameSymbol) {
        this.nameSymbol = nameSymbol;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getTheManager() {
        return theManager;
    }

    public String getNameManager() {
        return nameManager;
    }

    public void setNameManager(String nameManager) {
        this.nameManager = nameManager;
    }

    public ArrayList<String> getCadre() {
        return cadre;
    }

    public void addCadre(String namePlayer) {
        if(cadre == null){
            cadre = new ArrayList<>();
        }else{
            cadre.add(namePlayer);
        }

    }

    public void setTheManager(String theManager) {
        this.theManager = theManager;
    }
}
