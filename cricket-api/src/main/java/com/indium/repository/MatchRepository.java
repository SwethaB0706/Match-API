package com.indium.repository;

import com.indium.Entity.Match;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatchRepository extends CrudRepository<Match,Integer> {
    @Query("SELECT DISTINCT tp.match.matchId, p.name FROM TeamPlayer tp " +
            "JOIN tp.match m " +
            "JOIN tp.player p " +
            "WHERE p.name = :playerName")
    List<Object[]> findMatchNumberAndPlayerNameByPlayerName(@Param("playerName") String playerName);

}
