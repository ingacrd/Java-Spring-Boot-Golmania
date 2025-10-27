package com.ingaru.Java.golmania.controllers;

import com.ingaru.Java.golmania.Services.LeagueService;
import com.ingaru.Java.golmania.models.LeagueDoc;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leagues")
public class LeagueController {

    private final LeagueService service;

    public LeagueController(LeagueService service) {
        this.service = service;
    }

    @GetMapping("/world-cup")
    public ResponseEntity<List<LeagueDoc>> syncAndGetWorldCupLeagues(
            @RequestParam(name = "season", defaultValue = "2026") int season) {
        return ResponseEntity.ok(service.syncWorldCupLeagues(season));
    }
}
