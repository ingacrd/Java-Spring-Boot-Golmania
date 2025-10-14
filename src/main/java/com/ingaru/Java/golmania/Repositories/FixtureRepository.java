package com.ingaru.Java.golmania.Repositories;

import com.ingaru.Java.golmania.models.ApiFootballDto;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FixtureRepository extends MongoRepository<ApiFootballDto.Item, Long> {

    ApiFootballDto.Item findByFixture_FixtureId(Long fixtureId);

    List<ApiFootballDto.Item> findByLeague_LeagueIdAndLeague_Season(Integer leagueId, Integer season);

    List<ApiFootballDto.Item> findByGoals_HomeIsNull();
}