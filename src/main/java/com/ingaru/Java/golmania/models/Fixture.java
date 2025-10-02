package com.ingaru.Java.golmania.models;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Fixture {
    private long fixtureId;
    private OffsetDateTime utcDate;
    private String date;
    private String time;
    private String place;
    private String city;
    private String teamsHomeName;
    private String teamsHomeLogo;
    private Integer teamsHomeGoals;

    private String teamsAwayName;
    private String teamsAwayLogo;
    private Integer teamsAwayGoals;

    public Fixture() {
    }

    // --- convenience accessors if you still want date/time strings like before:
    public String getDate() { // e.g., 2023-09-08
        return utcDate == null ? null : utcDate.atZoneSameInstant(ZoneId.of("UTC"))
                .toLocalDate().toString();
    }

    public String getTime() { // e.g., 23:00
        return utcDate == null ? null : utcDate.atZoneSameInstant(ZoneId.of("UTC"))
                .toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public long getFixtureId() {
        return fixtureId;
    }

    public void setFixtureId(long fixtureId) {
        this.fixtureId = fixtureId;
    }

    public OffsetDateTime getUtcDate() {
        return utcDate;
    }

    public void setUtcDate(OffsetDateTime utcDate) {
        this.utcDate = utcDate;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getTeamsHomeName() {
        return teamsHomeName;
    }

    public void setTeamsHomeName(String teamsHomeName) {
        this.teamsHomeName = teamsHomeName;
    }

    public String getTeamsHomeLogo() {
        return teamsHomeLogo;
    }

    public void setTeamsHomeLogo(String teamsHomeLogo) {
        this.teamsHomeLogo = teamsHomeLogo;
    }

    public Integer getTeamsHomeGoals() {
        return teamsHomeGoals;
    }

    public void setTeamsHomeGoals(Integer teamsHomeGoals) {
        this.teamsHomeGoals = teamsHomeGoals;
    }

    public String getTeamsAwayName() {
        return teamsAwayName;
    }

    public void setTeamsAwayName(String teamsAwayName) {
        this.teamsAwayName = teamsAwayName;
    }

    public String getTeamsAwayLogo() {
        return teamsAwayLogo;
    }

    public void setTeamsAwayLogo(String teamsAwayLogo) {
        this.teamsAwayLogo = teamsAwayLogo;
    }

    public Integer getTeamsAwayGoals() {
        return teamsAwayGoals;
    }

    public void setTeamsAwayGoals(Integer teamsAwayGoals) {
        this.teamsAwayGoals = teamsAwayGoals;
    }

    @Override
    public String toString() {
        return "Fixture{" +
                "fixtureId=" + fixtureId +
                ", utcDate=" + utcDate +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", place='" + place + '\'' +
                ", city='" + city + '\'' +
                ", teamsHomeName='" + teamsHomeName + '\'' +
                ", teamsHomeLogo='" + teamsHomeLogo + '\'' +
                ", teamsHomeGoals=" + teamsHomeGoals +
                ", teamsAwayName='" + teamsAwayName + '\'' +
                ", teamsAwayLogo='" + teamsAwayLogo + '\'' +
                ", teamsAwayGoals=" + teamsAwayGoals +
                '}';
    }
}


