package com.example.goalmeet.Class;

public class Team {
    private String name;
    private String city;
    private String nameSymbol;
    private String theManager;
    private String cadre;
    private String description;
    private boolean fullCadre;
    private String nameManager;

    public Team(String name, String city, String nameSymbol, String theManager, String description, String nameManager, boolean fullCadre, String cadre) {
        this.name = name;
        this.city = city;
        this.nameSymbol = nameSymbol;
        this.theManager = theManager;
        this.description = description;
        this.nameManager = nameManager;
        this.fullCadre = fullCadre;
        this.cadre = cadre;
    }

    public Team() {
    }

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

    public String getCadre() {
        return cadre;
    }

    public void setCadre(String cadre) {
        this.cadre = cadre;
    }

    public void setTheManager(String theManager) {
        this.theManager = theManager;
    }

    public boolean getFullCadre() {
        return fullCadre;
    }

    public void setFullCadre(boolean fullCadre) {
        this.fullCadre = fullCadre;
    }
}
