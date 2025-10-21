package com.ingaru.Java.golmania.Repositories;

import com.ingaru.Java.golmania.models.ApiFootballDto;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FixtureRepository extends MongoRepository<ApiFootballDto.Item, Long> {

    ApiFootballDto.Item findByFixture_FixtureId(Long fixtureId);

    List<ApiFootballDto.Item> findByLeague_LeagueIdAndLeague_Season(Integer leagueId, Integer season);

    List<ApiFootballDto.Item> findByGoals_HomeIsNull();

    default boolean hasDetails(ApiFootballDto.Item it) {
        return it != null && (it.events() != null || it.lineups() != null || it.statistics() != null || it.players() != null || it.score() != null);
    }
    default ApiFootballDto.Item upsert(ApiFootballDto.Item item) {
        return save(item);
    }
}