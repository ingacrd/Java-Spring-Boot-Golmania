package com.ingaru.Java.golmania.models;

import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.mapping.Field;



@JsonIgnoreProperties(ignoreUnknown = true)
public record ApiFootballDto(
        @JsonProperty("response") List<Item> response
) {
    // ESTE es el documento que guardarás en Mongo
    @Document(collection = "fixtures")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record Item(
            Fix fixture,
            Teams teams,
            Goals goals,
            League league
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record Fix(
            // <-- Evita el nombre 'id' en el record; mapea a 'id' en JSON/Mongo
            @Field("id") @JsonProperty("id") Long fixtureId,
            String date,
            Venue venue
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record Venue(
            @Field("id") @JsonProperty("id") Integer venueId,
            String name,
            String city
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record Teams(Team home, Team away) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record Team(
            @Field("id") @JsonProperty("id") Integer teamId,
            String name,
            String logo,
            Boolean winner
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record Goals(Integer home, Integer away) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record League(
            @Field("id") @JsonProperty("id") Integer leagueId,
            @Field("season") @JsonProperty("season") Integer season
    ) {}
}
