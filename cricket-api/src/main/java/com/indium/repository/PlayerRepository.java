package com.indium.repository;

import com.indium.Entity.Player;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlayerRepository extends CrudRepository<Player,Integer> {
    Player findByName(String name);

    @Query("SELECT p.name FROM Player p " +
            "JOIN p.teamPlayers tp " +
            "JOIN tp.team t " +
            "JOIN t.matchTeams mt " +
            "JOIN mt.match m " +
            "JOIN m.event e " +
            "WHERE t.name = :teamName " +
            "AND e.matchNumber = :matchNumber")
    List<String> findPlayerNamesByTeamNameAndMatchNumber(@Param("teamName") String teamName, @Param("matchNumber") int matchNumber);
    }




