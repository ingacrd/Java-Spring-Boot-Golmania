package com.ingaru.Java.golmania.controllers;

import com.ingaru.Java.golmania.Services.FixtureService;
import com.ingaru.Java.golmania.models.ApiFootballDto;
import com.ingaru.Java.golmania.models.ApiFootballDto.Item;
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

    /**
     * - If DB has no fixtures: fetch league=34, season=2026 and save all.
     * - Else: update only past fixtures with missing scores.
     * - Finally: return all fixtures from MongoDB.
     */
    @GetMapping
    public ResponseEntity<List<Item>> getAllFromMongoInitOrUpdate() {
        return ResponseEntity.ok(fixtureService.initOrUpdateThenListAll());
    }

    /**
     * Optional: raw passthrough to inspect upstream data without persisting.
     * GET /api/fixtures/byLeagueSeason?league=34&season=2026
     */
    @GetMapping("/byLeagueSeason")
    public ResponseEntity<ApiFootballDto> getFixturesByLeagueAndSeason(
            @RequestParam("league") long league,
            @RequestParam("season") int season) {
        return ResponseEntity.ok(fixtureService.getFixturesByLeagueAndSeason(league, season));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getFixtureById(@PathVariable("id") long fixtureId) {
        ApiFootballDto.Item item = fixtureService.getFixtureByIdWithDetails(fixtureId);
        return ResponseEntity.ok(item);
    }

    @GetMapping("/ping")
    public String ping() { return "pong"; }
}
