package com.ingaru.Java.golmania.Services;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ingaru.Java.golmania.models.ApiFootballDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;


@Service
public class FixtureService {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    @Value("${football.api.key}")
    private String apiKey;

    @Value("${football.api.host:v3.football.api-sports.io}")
    private String apiHost;

    public FixtureService(RestTemplate restTemplate,ObjectMapper mapper) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
    }

    public ApiFootballDto.Item getFixtureById(long fixtureId) {
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

            ResponseEntity<String> resp = restTemplate.exchange(
                    uri, HttpMethod.GET, entity, String.class);
            if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
                throw new IllegalStateException("Upstream error: " + resp.getStatusCode());
            }

            ApiFootballDto api = mapper.readValue(resp.getBody(), ApiFootballDto.class);

            if (api.response() == null || api.response().isEmpty()) {
                throw new IllegalArgumentException("Fixture " + fixtureId + " not found");
            }

            return api.response().getFirst();

        } catch (RestClientException ex) {
            throw new IllegalStateException("HTTP error calling API-Football", ex);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to parse API-Football response", ex);
        }
    }

    public ApiFootballDto getFixturesByLeagueAndSeason (long league, int season){
        String baseUrl = "https://" + apiHost + "/fixtures";

        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("league", league)
                .queryParam("season", season)
                .build(true)
                .toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-rapidapi-key", apiKey);
        headers.set("x-rapidapi-host", apiHost);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {

            ResponseEntity<String> resp = restTemplate.exchange(
                    uri, HttpMethod.GET, entity, String.class);

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
