package com.ingaru.Java.golmania.models;

import java.util.List;

public record ApiFootballDto(List<Item> response) {
    public record Item(Fix fixture, Teams teams, Goals goals, League league) {}
    public record Fix(long id, String date, Venue venue) {}
    public record Venue(Integer id, String name, String city) {}
    public record Teams(Team home, Team away) {}
    public record Team(Integer id, String name, String logo, Boolean winner) {}
    public record Goals(Integer home, Integer away) {}
    public record League(Integer id, Integer season) {}
}