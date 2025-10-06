package com.ingaru.Java.golmania.Services;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ingaru.Java.golmania.Repositories.FixtureRepository;
import com.ingaru.Java.golmania.models.ApiFootballDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
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

    // ---------- Public API ----------

    /** Returns all fixtures from MongoDB. */
    public List<ApiFootballDto.Item> listAll() {
        return repo.findAll();
    }


    /** Returns a fixture by its upstream id (which is also Mongo _id). */
    public ApiFootballDto.Item getFixtureById(long fixtureId) {
        return repo.findById(fixtureId).orElse(null);
    }

    /**
     * Ensure DB is populated and up-to-date:
     * - If empty: fetch (league=34, season=2026), upsert all, return all
     * - If not empty: fetch and upsert (insert new + update changed), return all
     */
    public List<ApiFootballDto.Item> ensureSeedAndReturnAll() {
        if (repo.count() == 0) {
            upsertFromApi(34, 2026);
        } else {
            upsertFromApi(34, 2026);
        }
        return repo.findAll();
    }

    /** On-demand: fetch from API by league & season (raw). */
    public ApiFootballDto getFixturesByLeagueAndSeason(long league, int season) {
        return callApiForFixtures(league, season);
    }
// ---------- Internal helpers ----------

    /** Fetch from API and upsert into Mongo with _id = fixtureId. */
    private void upsertFromApi(long league, int season) {
        ApiFootballDto dto = callApiForFixtures(league, season);

        // Map each Item to set its Mongo _id (top-level id) from fixture.fixtureId
        List<ApiFootballDto.Item> toSave = dto.response().stream()
                .map(it -> new ApiFootballDto.Item(
                        it.fixture().fixtureId(), // <-- Mongo _id
                        it.fixture(),
                        it.teams(),
                        it.goals(),
                        it.league()
                ))
                .toList();

        repo.saveAll(toSave); // saveAll does insert-or-replace by _id
    }

    /** Low-level HTTP call + parse into ApiFootballDto. */
    private ApiFootballDto callApiForFixtures(long league, int season) {
        URI uri = UriComponentsBuilder
                .fromUriString("https://" + apiHost + "/fixtures")
                .queryParam("league", league)
                .queryParam("season", season)
                .build(true)
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-rapidapi-key", apiKey);
        headers.set("x-rapidapi-host", apiHost);

        HttpEntity<Void> reqEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> resp = rest.exchange(uri, HttpMethod.GET, reqEntity, String.class);
            if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
                throw new IllegalStateException("Upstream error: " + resp.getStatusCode());
            }
            return mapper.readValue(resp.getBody(), ApiFootballDto.class);

        } catch (RestClientException e) {
            throw new IllegalStateException("HTTP error calling API-Football", e);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to parse API-Football response", e);
        }
    }

}
