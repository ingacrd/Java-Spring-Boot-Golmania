package com.ingaru.Java.golmania.controllers;

import com.ingaru.Java.golmania.Services.FixtureService;
import com.ingaru.Java.golmania.models.ApiFootballDto;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fixtures")
public class FixtureController {

    private final FixtureService fixtureService;

    public FixtureController(FixtureService fixtureService) {
        this.fixtureService = fixtureService;
    }

    // GET /api/fixtures -> Seed if empty, otherwise upsert, then return all from Mongo
    @GetMapping
    public List<ApiFootballDto.Item> getFixtures() {
        return fixtureService.ensureSeedAndReturnAll();
    }

    // GET /api/fixtures/{id} -> Load by upstream fixture id (also Mongo _id)
    @GetMapping("/{id}")
    public ResponseEntity<ApiFootballDto.Item> getFixtureById(@PathVariable("id") long fixtureId) {
        ApiFootballDto.Item doc = fixtureService.getFixtureById(fixtureId);
        return (doc == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok(doc);
    }

    // GET /api/fixtures/byLeagueSeason?league=34&season=2026 (raw API passthrough if you need it)
    @GetMapping("/byLeagueSeason")
    public ResponseEntity<ApiFootballDto> getFixturesByLeagueAndSeason(
            @RequestParam("league") long league,
            @RequestParam("season") int season) {
        return ResponseEntity.ok(fixtureService.getFixturesByLeagueAndSeason(league, season));
    }

    @GetMapping("/ping")
    public String ping() { return "pong"; }


}

