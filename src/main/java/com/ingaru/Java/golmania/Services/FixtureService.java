package com.ingaru.Java.golmania.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ingaru.Java.golmania.Repositories.FixtureRepository;
import com.ingaru.Java.golmania.models.ApiFootballDto;
import com.ingaru.Java.golmania.models.ApiFootballDto.Item;
import com.ingaru.Java.golmania.models.ApiFootballDto.Goals;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.Console;
import java.net.URI;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FixtureService {

    private final FixtureRepository repo;
    private final RestTemplate rest;
    private final ObjectMapper mapper;

    @Value("${football.api.key}")
    private String apiKey;

    @Value("${football.api.host:v3.football.api-sports.io}")
    private String apiHost;

    public FixtureService(FixtureRepository repo) {
        this.repo = repo;
        this.rest = new RestTemplate();
        this.mapper = new ObjectMapper();
    }

    /* ---------- Public API used by the controller ---------- */

    public List<Item> listAll() {
        return repo.findAll();
    }

    /**
     * If DB is empty: fetch league 34 season 2026, save all (with _id=fixture.id), return all.
     * Else: update only past fixtures with missing scores, then return all.
     */
    public List<Item> initOrUpdateThenListAll() {
        if (repo.count() == 0) {
            ApiFootballDto dto = getFixturesByLeagueAndSeason(34L, 2026);
            saveAllAssigningMongoId(dto.response());
        } else {
            updatePastFixturesWithMissingScore();
        }
        return repo.findAll();
    }

    /**
     * Requirement: "Update in MongoDB only the fixtures that have teams_home_goals = null
     * or empty AND the match date/time has passed; when API says 'Match Finished'."
     */
    public List<Item> updatePastFixturesWithMissingScore() {
        List<Item> candidates = repo.findByGoals_HomeIsNull();
        if (candidates.isEmpty()) return List.of();


        Instant now = Instant.now(); // UTC instant

        List<Item> updated = new ArrayList<>();
        for (Item it : candidates) {
            System.out.println(it.fixture().fixtureId());
            System.out.println(it.fixture());
            System.out.println(it);


            // fixture.date must be in the past
            if (!isPast(it)) continue;

            // fetch fresh data by fixtureId
            long fixtureId = it.fixture().fixtureId();
            ApiFootballDto live = getFixtureById(fixtureId);

            if (live.response() == null || live.response().isEmpty()) continue;

            Item fresh = live.response().get(0);

            // status.long == "Match Finished" (handle both "long" and alias)
            String status = fresh.fixture().status() != null ? fresh.fixture().status().resolvedLong() : null;
            if (!"Match Finished".equalsIgnoreCase(status)) continue;

            // build an updated copy of our existing entity: keep _id, replace goals
            Goals g = fresh.goals() != null ? fresh.goals() : new Goals(null, null);
            Item updatedItem = new Item(it.id(), it.fixture(), it.teams(), g, it.league());

            repo.save(updatedItem);
            updated.add(updatedItem);
        }
        return updated;
    }

    /**
     * Raw fetch; also used by controller for /byLeagueSeason endpoint.
     */
    public ApiFootballDto getFixturesByLeagueAndSeason(long league, int season) {
        URI uri = UriComponentsBuilder
                .fromUriString("https://" + apiHost + "/fixtures")
                .queryParam("league", league)
                .queryParam("season", season)
                .build(true)
                .toUri();
        return callApi(uri);
    }

    public ApiFootballDto getFixtureById(long fixtureId) {
        URI uri = UriComponentsBuilder
                .fromUriString("https://" + apiHost + "/fixtures")
                .queryParam("id", fixtureId)
                .build(true)
                .toUri();
        return callApi(uri);
    }

    /* ---------- Helpers ---------- */

    private boolean isPast(Item it) {
        try {
            String iso = it.fixture().date(); // e.g., 2026-04-15T19:00:00+00:00
            if (iso == null || iso.isBlank()) return false;
            Instant fixtureInstant = OffsetDateTime.parse(iso).toInstant();
            return fixtureInstant.isBefore(Instant.now());
        } catch (Exception e) {
            // If parsing fails, be conservative and do not update.
            return false;
        }
    }

    private void saveAllAssigningMongoId(List<Item> items) {
        if (items == null || items.isEmpty()) return;
        List<Item> toSave = new ArrayList<>(items.size());
        for (Item it : items) {
            Long mongoId = it.fixture() != null ? it.fixture().fixtureId() : null;
            if (mongoId == null) continue; // skip broken rows
            Item withId = new Item(mongoId, it.fixture(), it.teams(), it.goals(), it.league());
            toSave.add(withId);
        }
        if (!toSave.isEmpty()) repo.saveAll(toSave);
    }

    private ApiFootballDto callApi(URI uri) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("x-rapidapi-key", apiKey);
            headers.set("x-rapidapi-host", apiHost);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            HttpEntity<Void> reqEntity = new HttpEntity<>(headers);

            ResponseEntity<String> resp = rest.exchange(uri, HttpMethod.GET, reqEntity, String.class);
            if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
                throw new IllegalStateException("API-Football error: " + resp.getStatusCode());
            }
            return mapper.readValue(resp.getBody(), ApiFootballDto.class);
        } catch (RestClientException e) {
            throw new IllegalStateException("HTTP error calling API-Football", e);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to parse API-Football response", e);
        }
    }
}
