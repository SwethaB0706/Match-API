package com.indium.repository;

import com.indium.Entity.Official;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OfficialRepository extends CrudRepository<Official,Integer> {
    @Query("SELECT o.name FROM Official o " +
            "JOIN o.match m " +
            "JOIN m.event e " +
            "WHERE e.matchNumber = :matchNumber " +
            "AND o.officialType = 'Referee'")
    List<String> findRefereesByMatchNumber(@Param("matchNumber") int matchNumber);
}
