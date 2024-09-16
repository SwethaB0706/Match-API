package com.indium.service;

import com.indium.repository.DeliveryRepository;
import com.indium.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QueryService {
    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Cacheable(value = "matchesByPlayerName", key = "#playerName")
    public List<Object[]> getMatchesByPlayerName(String playerName) {
        return matchRepository.findMatchNumberAndPlayerNameByPlayerName(playerName);
    }

    @Cacheable(value = "cumulativeScore", key = "#playerName")
    public Long getCumulativeScore(String playerName) {
        Object[] result = deliveryRepository.getCumulativeScoreByPlayerName(playerName);

        if (result != null && result.length > 0) {
            if (result.length == 2 && result[1] instanceof Long) {
                return (Long) result[1];
            } else if (result.length == 2 && result[1] instanceof Integer) {
                return ((Integer) result[1]).longValue();
            } else if (result.length == 1 && result[0] instanceof Long) {
                return (Long) result[0];
            } else if (result.length == 1 && result[0] instanceof Integer) {
                return ((Integer) result[0]).longValue();
            }
        }

        return 0L;
    }

    @Cacheable(value = "scoresByDate", key = "#matchDate")
    public Map<Integer, Long> getScoresByDate(String matchDate) {
        List<Object[]> results = deliveryRepository.findScoresByMatchDate(LocalDate.parse(matchDate));
        Map<Integer, Long> matchScores = new HashMap<>();

        for (Object[] result : results) {
            Integer matchId = (Integer) result[0];
            Long totalRuns = (Long) result[1];
            matchScores.put(matchId, totalRuns);
        }

        return matchScores;
    }

    @Cacheable(value = "topBatsmen", key = "#page + '-' + #size")
    public Page<Object[]> getTopBatsmen(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return deliveryRepository.findTopBatsmenByRunsAsc(pageable);
    }

    @CacheEvict(value = {"matchesByPlayerName", "cumulativeScore", "scoresByDate", "topBatsmen"}, allEntries = true)
    public void clearCache(){

    }
}
