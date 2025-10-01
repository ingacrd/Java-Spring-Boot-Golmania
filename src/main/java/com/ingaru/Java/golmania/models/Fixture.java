package com.ingaru.Java.golmania.models;

public class Fixture {
    private String fixtureId;
    private String date;
    private String time;
    private String place;
    private String city;
    private String teams_home_name;
    private String teams_home_logo;
    private String teams_home_goals;
    private String teams_away_name;
    private String teams_away_logo;
    private String teams_away_goals;

    public Fixture() {
    }

    public String getFixtureId() {
        return fixtureId;
    }

    public void setFixtureId(String fixtureId) {
        this.fixtureId = fixtureId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTeams_home_name() {
        return teams_home_name;
    }

    public void setTeams_home_name(String teams_home_name) {
        this.teams_home_name = teams_home_name;
    }

    public String getTeams_home_logo() {
        return teams_home_logo;
    }

    public void setTeams_home_logo(String teams_home_logo) {
        this.teams_home_logo = teams_home_logo;
    }

    public String getTeams_home_goals() {
        return teams_home_goals;
    }

    public void setTeams_home_goals(String teams_home_goals) {
        this.teams_home_goals = teams_home_goals;
    }

    public String getTeams_away_name() {
        return teams_away_name;
    }

    public void setTeams_away_name(String teams_away_name) {
        this.teams_away_name = teams_away_name;
    }

    public String getTeams_away_logo() {
        return teams_away_logo;
    }

    public void setTeams_away_logo(String teams_away_logo) {
        this.teams_away_logo = teams_away_logo;
    }

    public String getTeams_away_goals() {
        return teams_away_goals;
    }

    public void setTeams_away_goals(String teams_away_goals) {
        this.teams_away_goals = teams_away_goals;
    }

    @Override
    public String toString() {
        return "Fixture{" +
                "fixtureId='" + fixtureId + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", place='" + place + '\'' +
                ", city='" + city + '\'' +
                ", teams_home_name='" + teams_home_name + '\'' +
                ", teams_home_logo='" + teams_home_logo + '\'' +
                ", teams_home_goals='" + teams_home_goals + '\'' +
                ", teams_away_name='" + teams_away_name + '\'' +
                ", teams_away_logo='" + teams_away_logo + '\'' +
                ", teams_away_goals='" + teams_away_goals + '\'' +
                '}';
    }
}
