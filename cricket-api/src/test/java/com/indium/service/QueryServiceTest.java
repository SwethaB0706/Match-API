package com.indium.service;

import com.indium.repository.DeliveryRepository;
import com.indium.repository.MatchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class QueryServiceTest {

    @InjectMocks
    private QueryService queryService;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache cache;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(cacheManager.getCache(any(String.class))).thenReturn(cache);
    }

    @Test
    public void testGetMatchesByPlayerName() {
        String playerName = "Player 1";
        List<Object[]> expectedMatches = Collections.singletonList(new Object[]{1, "Player 1"});

        when(matchRepository.findMatchNumberAndPlayerNameByPlayerName(playerName)).thenReturn(expectedMatches);

        List<Object[]> matches = queryService.getMatchesByPlayerName(playerName);
        assertNotNull(matches, "Matches should not be null");
        assertEquals(expectedMatches, matches, "Matches should match the expected values");

    }

    @Test
    public void testGetCumulativeScore() {
        String playerName = "Player 1";
        Object[] result = {1L};

        when(deliveryRepository.getCumulativeScoreByPlayerName(playerName)).thenReturn(result);

        Long score = queryService.getCumulativeScore(playerName);
        assertNotNull(score, "Cumulative score should not be null");
        assertEquals(1L, score, "Cumulative score should match the expected value");


    }

    @Test
    public void testGetScoresByDate() {
        String matchDate = "2024-09-14";
        List<Object[]> results = Collections.singletonList(new Object[]{1, 150L});
        Map<Integer, Long> expectedScores = new HashMap<>();
        expectedScores.put(1, 150L);

        when(deliveryRepository.findScoresByMatchDate(LocalDate.parse(matchDate))).thenReturn(results);

        Map<Integer, Long> scores = queryService.getScoresByDate(matchDate);
        assertNotNull(scores, "Scores should not be null");
        assertEquals(expectedScores, scores, "Scores should match the expected values");

    }

    @Test
    public void testGetTopBatsmen() {
        int page = 0;
        int size = 10;
        Page<Object[]> topBatsmen = mock(Page.class);

        when(deliveryRepository.findTopBatsmenByRunsAsc(PageRequest.of(page, size))).thenReturn(topBatsmen);

        Page<Object[]> result = queryService.getTopBatsmen(page, size);
        assertNotNull(result, "Top batsmen page should not be null");
        assertEquals(topBatsmen, result, "Top batsmen page should match the expected value");


    }

    @Test
    public void testClearCache() {
        Cache cache = mock(Cache.class);
        when(cacheManager.getCache("matchesByPlayerName")).thenReturn(cache);
        when(cacheManager.getCache("cumulativeScore")).thenReturn(cache);
        when(cacheManager.getCache("scoresByDate")).thenReturn(cache);
        when(cacheManager.getCache("topBatsmen")).thenReturn(cache);

        queryService.clearCache();

    }

}
