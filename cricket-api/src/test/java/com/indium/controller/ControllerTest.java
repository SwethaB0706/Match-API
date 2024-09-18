package com.indium.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.indium.service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(Controller.class)
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InsertService myService;

    @MockBean
    private WicketService wicketService;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private OfficialService officialService;

    @MockBean
    private QueryService queryService;

    @MockBean
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testInsertMatchData() throws Exception {
        String jsonData = "{\"matchId\": 1, \"team\": \"Team A\"}";

        mockMvc.perform(post("/api/matches/insert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonData))
                .andExpect(status().isCreated());

        verify(myService, times(1)).insertMatchData(jsonData);
        verify(queryService, times(1)).clearCache();
        verify(kafkaTemplate, times(0)).send(anyString(), anyString());
    }

    @Test
    public void testGetMatchesByPlayerName() throws Exception {
        String playerName = "AS Yadav";
        List<Object[]> matches = new ArrayList<>();
        matches.add(new Object[]{1, "Match 1"});

        when(queryService.getMatchesByPlayerName(playerName)).thenReturn(matches);

        mockMvc.perform(get("/api/matches/player/{playerName}", playerName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0][0]").value(1));

        verify(queryService, times(1)).getMatchesByPlayerName(playerName);
        verify(kafkaTemplate, times(1)).send(anyString(), anyString());
    }

    @Test
    public void testGetCumulativeScore() throws Exception {
        String playerName = "AS Yadav";
        Long cumulativeScore = 250L;

        when(queryService.getCumulativeScore(playerName)).thenReturn(cumulativeScore);

        mockMvc.perform(get("/api/matches/cumulative-score")
                        .param("playerName", playerName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(cumulativeScore));

        verify(queryService, times(1)).getCumulativeScore(playerName);
        verify(kafkaTemplate, times(1)).send(anyString(), anyString());
    }

    @Test
    public void testGetScoresByDate() throws Exception {
        String matchDate = "2008-04-20";
        Map<Integer, Long> scores = new HashMap<>();
        scores.put(1, 200L);

        when(queryService.getScoresByDate(matchDate)).thenReturn(scores);

        mockMvc.perform(get("/api/matches/score")
                        .param("matchDate", matchDate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.1").value(200));

        verify(queryService, times(1)).getScoresByDate(matchDate);
        verify(kafkaTemplate, times(1)).send(anyString(), anyString());
    }

    @Test
    public void testGetTopBatsmen() throws Exception {
        int page = 0;
        int size = 10;

        // Create a list of Object arrays for top batsmen
        List<Object[]> topBatsmen = new ArrayList<>();
        topBatsmen.add(new Object[]{1, "Player 1"});

        // Create a real PageImpl object instead of mocking Page
        Page<Object[]> pageResult = new PageImpl<>(topBatsmen);

        // Mock the queryService to return the real Page object
        when(queryService.getTopBatsmen(page, size)).thenReturn(pageResult);

        // Perform the GET request and validate the response using jsonPath
        mockMvc.perform(get("/api/matches/topbatsmen")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                // Adjust jsonPath to check within "content"
                .andExpect(jsonPath("$.content[0][0]").value(1))  // Verifies the first field (ID) of the first item
                .andExpect(jsonPath("$.content[0][1]").value("Player 1"));  // Verifies the second field (Name) of the first item

        // Verify that the service method was called
        verify(queryService, times(1)).getTopBatsmen(page, size);

        // Verify that a message was sent to Kafka
        verify(kafkaTemplate, times(1)).send(eq("match-logs-topic"), anyString());
    }


    @Test
    public void testGetWickets() throws Exception {
        String playerName = "AS Yadav";
        Long wickets = 10L;

        when(wicketService.getWicketsByPlayerName(playerName)).thenReturn(wickets);

        mockMvc.perform(get("/api/matches/wickets")
                        .param("playerName", playerName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(wickets));

        verify(wicketService, times(1)).getWicketsByPlayerName(playerName);
    }

    @Test
    public void testGetPlayers() throws Exception {
        String teamName = "Team A";
        int matchNumber = 1;
        List<String> players = Arrays.asList("Player 1", "Player 2");

        when(playerService.getPlayersByTeamNameAndMatchNumber(teamName, matchNumber)).thenReturn(players);

        mockMvc.perform(get("/api/matches/players")
                        .param("teamName", teamName)
                        .param("matchNumber", String.valueOf(matchNumber)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Player 1"));

        verify(playerService, times(1)).getPlayersByTeamNameAndMatchNumber(teamName, matchNumber);
    }

    @Test
    public void testGetReferees() throws Exception {
        int matchNumber = 1;
        List<String> referees = Arrays.asList("Referee 1", "Referee 2");

        when(officialService.getRefereesByMatchNumber(matchNumber)).thenReturn(referees);

        mockMvc.perform(get("/api/matches/referees")
                        .param("matchNumber", String.valueOf(matchNumber)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Referee 1"));

        verify(officialService, times(1)).getRefereesByMatchNumber(matchNumber);
    }
}
