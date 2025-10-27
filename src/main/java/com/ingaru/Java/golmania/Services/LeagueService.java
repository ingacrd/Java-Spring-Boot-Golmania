package com.ingaru.Java.golmania.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ingaru.Java.golmania.Repositories.LeagueRepository;
import com.ingaru.Java.golmania.models.ApiLeaguesDto;
import com.ingaru.Java.golmania.models.LeagueDoc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Locale;

@Service
public class LeagueService {

    private final LeagueRepository repo;
    private final RestTemplate rest;
    private final ObjectMapper mapper;

    @Value("${football.api.key}")
    private String apiKey;

    @Value("${football.api.host:v3.football.api-sports.io}")
    private String apiHost;

    public LeagueService(LeagueRepository repo, ObjectMapper mapper) {
        this.repo = repo;
        this.rest = new RestTemplate();
        this.mapper = mapper;
    }

    public List<LeagueDoc> syncWorldCupLeagues(int season) {

        URI uri = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(apiHost)
                .path("/leagues")
                .queryParam("season", season)
                .build(true)
                .toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-rapidapi-host", apiHost);
        headers.set("x-rapidapi-key", apiKey);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> resp = rest.exchange(uri, HttpMethod.GET, entity, String.class);
        String json = resp.getBody();

        ApiLeaguesDto dto;
        try {
            dto = mapper.readValue(json, ApiLeaguesDto.class);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to parse leagues response", ex);
        }

       var filtered = dto.response().stream()
                .filter(it -> it.country() != null && "World".equals(it.country().name()))
                .filter(it -> it.league() != null && "Cup".equals(it.league().type()))
                .filter(it -> it.league().name() != null &&
                        it.league().name().toLowerCase(Locale.ROOT).contains("world cup"))
                .map(it -> {
                    // Find this league's season entry matching the requested season
                    var seasonEntry = it.seasons().stream()
                            .filter(s -> s.year() == season)
                            .findFirst()
                            .orElse(null);

                    String start = seasonEntry != null ? seasonEntry.start() : null;
                    String end   = seasonEntry != null ? seasonEntry.end()   : null;
                    boolean current = seasonEntry != null && seasonEntry.current();

                    return new LeagueDoc(
                            it.league().id(),
                            it.league().name(),
                            it.league().type(),
                            it.league().logo(),
                            it.country().name(),
                            season,
                            start,
                            end,
                            current
                    );
                })
                .toList();

        repo.saveAll(filtered);

        return repo.findBySeason(season);
    }
}

