package com.ingaru.Java.golmania.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
//        @JsonProperty("response") List<Item> response
        List<Item> response
) {
    @Document(collection = "fixtures")
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static record Item(
            @Id
            @Field("_id")
            Long _id,
            Fix fixture,
            Teams teams,
            Goals goals,
            League league,

            Score score,
            List<Event> events,
            List<Lineup> lineups,
            List<TeamStatistics> statistics,
            List<PlayerStatisticsByTeam> players
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record Fix(
            // Upstream fixture id from the API payload
            @Field("id") @JsonProperty("id") long fixtureId,
            String referee,
            String timezone,
            String date,
            Long timestamp,
            Venue venue,
            Status status
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record Periods(Long first, Long second) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record Status(
            @JsonProperty("long")  String longText,
            @JsonProperty("short") String shortText,
            Integer elapsed,
            String extra) {}

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
            @Field("season") @JsonProperty("season") Integer season,
            String name,
            String country,
            String logo,
            String flag,
            String round,
            Boolean standings
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record Score(ScorePart halftime, ScorePart fulltime, ScorePart extratime, ScorePart penalty) {}
    public static record ScorePart(Integer home, Integer away) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record Event(
            Time time,
            SimpleTeam team,
            SimplePlayer player,
            SimplePlayer assist,
            String type,
            String detail,
            String comments
    ) {}

    public static record Time(Integer elapsed, Integer extra) {}
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record SimpleTeam(Integer id, String name, String logo) {}
    public static record SimplePlayer(Integer id, String name) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record Lineup(
            SimpleTeam team,
            Coach coach,
            String formation,
            List<LineupPlayerWrapper> startXI,
            List<LineupPlayerWrapper> substitutes
    ) {}

    public static record Coach(Integer id, String name, String photo) {}
    public static record LineupPlayerWrapper(LineupPlayer player) {}
    public static record LineupPlayer(Integer id, String name, Integer number, String pos, String grid) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record TeamStatistics(
            SimpleTeam team,
            List<StatKV> statistics
    ) {}
    public static record StatKV(String type, Object value) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record PlayerStatisticsByTeam(
            SimpleTeam team,
            @JsonProperty("players") List<PlayerStatisticsWrapper> players // (API regresa un array "players")
    ) {}
    public static record PlayerStatisticsWrapper(PlayerStats player, List<PlayerGameStatistics> statistics) {}
    public static record PlayerStats(Integer id, String name, String photo) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record PlayerGameStatistics(
            Games games,
            Integer offsides,
            Shots shots,
            PlayerGoals goals,
            Passes passes,
            Tackles tackles,
            Duels duels,
            Dribbles dribbles,
            Fouls fouls,
            Cards cards,
            Penalty penalty
    ) {}

    public static record Games(Integer minutes, Integer number, String position, String rating, Boolean captain, Boolean substitute) {}
    public static record Shots(Integer total, Integer on) {}
    public static record PlayerGoals(Integer total, Integer conceded, Integer assists, Integer saves) {}
    public static record Passes(Integer total, Integer key, String accuracy) {}
    public static record Tackles(Integer total, Integer blocks, Integer interceptions) {}
    public static record Duels(Integer total, Integer won) {}
    public static record Dribbles(Integer attempts, Integer success, Integer past) {}
    public static record Fouls(Integer drawn, Integer committed) {}
    public static record Cards(Integer yellow, Integer red) {}
    public static record Penalty(Integer won, Integer commited, Integer scored, Integer missed, Integer saved) {}


}
