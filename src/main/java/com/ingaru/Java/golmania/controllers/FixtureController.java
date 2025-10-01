package com.ingaru.Java.golmania.controllers;

import com.ingaru.Java.golmania.Services.FixtureService;
import com.ingaru.Java.golmania.models.Fixture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class FixtureController {

    private final FixtureService fixtureService;


    public FixtureController(FixtureService fixtureService) {
        this.fixtureService = fixtureService;
    }

    @GetMapping("/fixtures/{id}")
    public ResponseEntity<?> getFixtureById(@PathVariable("id") long fixtureId) {
        var body = fixtureService.getFixtureById(fixtureId);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/fixtures")
    public ResponseEntity<?> getFixturesByLeagueAndSeason(
            @RequestParam("league") long league,
            @RequestParam("season") int season) {

        Map<String, Object>  body = fixtureService.getFixturesByLeagueAndSeason(league, season);

        // 2) Extraer la lista "response"
        List<Map<String, Object>> response = (List<Map<String, Object>>) body.get("response");

        List<Fixture> fixtures = new ArrayList<>();
        if (response != null) {
            for (Map<String, Object> fixtureData : response) {
                Map<String, Object> fixtureNode = (Map<String, Object>) fixtureData.get("fixture");
                Map<String, Object> venueNode   = fixtureNode != null ? (Map<String, Object>) fixtureNode.get("venue") : null;

                Map<String, Object> teamsNode = (Map<String, Object>) fixtureData.get("teams");
                Map<String, Object> homeNode  = teamsNode != null ? (Map<String, Object>) teamsNode.get("home") : null;
                Map<String, Object> awayNode  = teamsNode != null ? (Map<String, Object>) teamsNode.get("away") : null;

                Map<String, Object> goalsNode = (Map<String, Object>) fixtureData.get("goals");

                // 3) Obtener date/time por substring (sin formateadores)
                String dateTime = str(fixtureNode != null ? fixtureNode.get("date") : null);
                String date = null;
                String time = null;
                if (dateTime != null && dateTime.length() >= 16) { // ej: 2024-08-17T19:00:00+00:00
                    date = dateTime.substring(0, 10);   // "YYYY-MM-DD"
                    time = dateTime.substring(11, 16);  // "HH:mm"
                }

                Fixture f = new Fixture();
                f.setFixtureId(str(fixtureNode != null ? fixtureNode.get("id") : null));
                f.setDate(date);
                f.setTime(time);
                f.setPlace(str(venueNode != null ? venueNode.get("name") : null));
                f.setCity(str(venueNode != null ? venueNode.get("city") : null));

                f.setTeams_home_name(str(homeNode != null ? homeNode.get("name") : null));
                f.setTeams_home_logo(str(homeNode != null ? homeNode.get("logo") : null));
                f.setTeams_home_goals(str(goalsNode != null ? goalsNode.get("home") : null));

                f.setTeams_away_name(str(awayNode != null ? awayNode.get("name") : null));
                f.setTeams_away_logo(str(awayNode != null ? awayNode.get("logo") : null));
                f.setTeams_away_goals(str(goalsNode != null ? goalsNode.get("away") : null));

                fixtures.add(f);
            }
        }



        return ResponseEntity.ok(fixtures);
    }

    private static String str(Object o) {
        return o == null ? null : String.valueOf(o);
    }
}
