package com.ingaru.Java.golmania.controllers;

import com.ingaru.Java.golmania.Services.FixtureService;
import com.ingaru.Java.golmania.models.ApiFootballDto;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/fixtures")
public class FixtureController {

    private final FixtureService fixtureService;


    public FixtureController(FixtureService fixtureService) {
        this.fixtureService = fixtureService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiFootballDto.Item> getFixtureById(@PathVariable("id") long fixtureId) {
        return ResponseEntity.ok(fixtureService.getFixtureById(fixtureId));
    }

    @GetMapping("/fixtures")
    public ResponseEntity<ApiFootballDto> getFixturesByLeagueAndSeason(
            @RequestParam("league") long league,
            @RequestParam("season") int season) {

        ApiFootballDto dto = fixtureService.getFixturesByLeagueAndSeason(league, season);
        return ResponseEntity.ok(dto);

        }

    }

