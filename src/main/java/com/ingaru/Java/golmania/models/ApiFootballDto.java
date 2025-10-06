package com.ingaru.Java.golmania.models;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ApiFootballDto(
        @JsonProperty("response") List<Item> response
) {

    // Mongo document for "fixtures"
    @Document(collection = "fixtures")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record Item(
            // This will be MongoDB’s _id
            // We don’t expect it from the API payload, we set it ourselves before saving.
            @Id @JsonProperty(value = "_id", access = JsonProperty.Access.READ_ONLY) Long id,

            Fix fixture,
            Teams teams,
            Goals goals,
            League league
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record Fix(
            @Field("id") @JsonProperty("id") long fixtureId,  // Upstream ID (source of truth)
            String date,
            Venue venue
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record Venue(
            Integer id,
            String name,
            String city
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record Teams(Team home, Team away) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record Team(
            Integer id,
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
