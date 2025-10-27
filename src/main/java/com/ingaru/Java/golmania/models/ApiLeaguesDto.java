package com.ingaru.Java.golmania.models;


import java.util.List;

public record ApiLeaguesDto(List<Item> response) {

    public record Item(League league, Country country, List<Season> seasons) {}

    public record League(long id, String name, String type, String logo) {}

    public record Country(String name, String code, String flag) {}

    public record Season(int year, String start, String end, boolean current) {}
}
