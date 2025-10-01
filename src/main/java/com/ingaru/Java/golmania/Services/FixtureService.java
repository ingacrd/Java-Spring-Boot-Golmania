package com.ingaru.Java.golmania.Services;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@Service
public class FixtureService {

    private final RestTemplate restTemplate;

    @Value("${football.api.key}")
    private String apiKey;

    // Puedes cambiar el host por config si algún día usas otro
    @Value("${football.api.host:v3.football.api-sports.io}")
    private String apiHost;

    public FixtureService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> getFixturesByLeagueAndSeason (long league, int season){
        String baseUrl = "https://" + apiHost + "/fixtures";

        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("league", league)
                .queryParam("season", season)
                .build(true)
                .toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-rapidapi-key", apiKey);
        headers.set("x-rapidapi-host", apiHost);
        headers.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    uri, HttpMethod.GET, entity, Map.class);

            return (Map<String, Object>) response.getBody();
        } catch (HttpStatusCodeException ex) {
            // Igual que tu try/catch en PHP, pero lanzando Runtime para que el Controller saque 5xx limpio
            throw new RuntimeException("Error fetching fixture data: " + ex.getStatusCode() + " " + ex.getResponseBodyAsString(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to fetch fixture data", ex);
        }
    }

    public Map<String, Object> getFixtureById(long fixtureId) {
        String baseUrl = "https://" + apiHost + "/fixtures";

        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("id", fixtureId)
                .build(true)
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-rapidapi-key", apiKey);
        headers.set("x-rapidapi-host", apiHost);
        headers.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    uri, HttpMethod.GET, entity, Map.class);

            return (Map<String, Object>) response.getBody();
        } catch (HttpStatusCodeException ex) {
            // Igual que tu try/catch en PHP, pero lanzando Runtime para que el Controller saque 5xx limpio
            throw new RuntimeException("Error fetching fixture data: " + ex.getStatusCode() + " " + ex.getResponseBodyAsString(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to fetch fixture data", ex);
        }
    }
}
