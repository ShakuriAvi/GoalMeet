package com.example.goalmeet.Class;

public class Game {
    private String homeTeam;
    private String awayTeam;
    private String result;
    private String awayClubManager;
    private String homeClubManager;
    public Game(){}
    public Game(String homeTeam, String awayTeam, String result, String awayClubManager, String homeClubManager) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.result = result;
        this.awayClubManager = awayClubManager;
        this.homeClubManager = homeClubManager;
    }
    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }



    public String getAwayClubManager() {
        return awayClubManager;
    }

    public void setAwayClubManager(String awayClubManager) {
        this.awayClubManager = awayClubManager;
    }

    public String getHomeClubManager() {
        return homeClubManager;
    }

    public void setHomeClubManager(String homeClubManager) {
        this.homeClubManager = homeClubManager;
    }
}
