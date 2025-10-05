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

    @GetMapping("/{id}")
    public ResponseEntity<ApiFootballDto.Item> getFixtureById(@PathVariable("id") long fixtureId) {
        return ResponseEntity.ok(fixtureService.getFixtureById(fixtureId));
    }

    @GetMapping
    public List<ApiFootballDto.Item> getFixtures() {
        return fixtureService.listAll();
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }



}

