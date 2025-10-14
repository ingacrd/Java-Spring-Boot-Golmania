package com.ingaru.Java.golmania.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 * Wire DTO for API-Football AND Mongo entity for a fixture item.
 * We persist each Item with _id = fixture.id (from upstream).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ApiFootballDto(
        @JsonProperty("response") List<Item> response
) {
    @Document(collection = "fixtures")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record Item(
            // MongoDB _id, we set this from fixture.id before saving.
            @Id Long id,
            Fix fixture,
            Teams teams,
            Goals goals,
            League league
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record Fix(
            // Upstream fixture id from the API payload
            @Field("id") @JsonProperty("id") long fixtureId,
            String date,
            Venue venue,
            Status status
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record Status(
            String longName, // "Match Finished" from API may arrive as "long"
            @JsonProperty("long") String longText // keep alias “long” to be safe
    ) {
        public String resolvedLong() {
            // prefer the official "long" key if present
            return longText != null ? longText : longName;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record Venue(Integer id, String name, String city) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record Teams(Team home, Team away) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record Team(Integer id, String name, String logo, Boolean winner) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record Goals(Integer home, Integer away) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record League(
            @Field("id") @JsonProperty("id") Integer leagueId,
            @Field("season") @JsonProperty("season") Integer season
    ) {}
}
