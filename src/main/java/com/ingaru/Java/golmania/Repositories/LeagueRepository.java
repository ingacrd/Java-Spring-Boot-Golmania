package com.ingaru.Java.golmania.Repositories;

import com.ingaru.Java.golmania.models.LeagueDoc;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface LeagueRepository extends MongoRepository<LeagueDoc, Long> {
    List<LeagueDoc> findBySeason(Integer season);
}

