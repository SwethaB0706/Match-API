package com.indium.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.indium.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Tag(name = "Analysing the previous cricket match data", description = "Endpoints to get info about the match")
@RestController
@RequestMapping("/api/matches")
public class Controller {

    private static Logger logger = org.slf4j.LoggerFactory.getLogger(Controller.class);

    @Autowired
    private InsertService insertService;

    @Autowired
    private WicketService wicketService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private OfficialService officialService;

    @Autowired
    private QueryService queryService;

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/insert")
    public ResponseEntity<String> insertMatchData(@RequestBody String jsonData) {
        try {
            insertService.insertMatchData(jsonData);
            queryService.clearCache();
            return new ResponseEntity<>("Match data inserted successfully!", HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to parse JSON data: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error inserting match data: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "All the matches played by a given player", parameters = {
            @Parameter(name = "playerName", description = "The player name", example = "AS Yadav")
    })
    @ApiResponse(description = "returns the matchId played by the given player", responseCode = "200")
    @GetMapping("/player/{playerName}")
    public ResponseEntity<List<Object[]>> getMatchesByPlayerName(@PathVariable String playerName) {
        List<Object[]> matches = queryService.getMatchesByPlayerName(playerName);

        try {
            // Convert matches list to JSON string
            String logMessage = objectMapper.writeValueAsString(matches);

            // Log the JSON string
            logger.info("Matches for player {}: {}", playerName, logMessage);

            // Send the log message to Kafka
            kafkaTemplate.send("match-logs-topic", logMessage);

        } catch (JsonProcessingException e) {
            // Handle JSON processing exceptions
            String errorMessage = "Error serializing matches data for player " + playerName + ": " + e.getMessage();
            logger.error(errorMessage);
            kafkaTemplate.send("match-logs-topic", errorMessage);
        }
        return ResponseEntity.ok(matches);
    }

    @Operation(summary = "All the cummulative score of a given player ", parameters = {
            @Parameter(name = "playerName", description = "The player name", example = "AS Yadav")
    })
    @ApiResponse(description = "returns the cumulative score of the given player", responseCode = "200")
    @GetMapping("/cumulative-score")
    public Long getCumulativeScore(@RequestParam("playerName") String playerName) {
        Long score = queryService.getCumulativeScore(playerName);
        try {
            String logMessage = objectMapper.writeValueAsString(score);
            logger.info("Cumulative score for player {}: {}", playerName, logMessage);
            kafkaTemplate.send("match-logs-topic", logMessage);
        } catch (JsonProcessingException e) {
            String errorMessage = "Error serializing cumulative score data for player " + playerName + ": " + e.getMessage();
            logger.error(errorMessage);
            kafkaTemplate.send("match-logs-topic", errorMessage);
        }
        return score;
    }

    @Operation(summary = "All the scores of the match(es) on given a date", parameters = {
            @Parameter(name = "matchDate", description = "Match date", example = "2008-04-20")
    })
    @ApiResponse(description = "returns the scores of the matches on the give date", responseCode = "200")
    @GetMapping("/score")
    public Map<Integer, Long> getScoresByDate(@RequestParam String matchDate) {
        Map<Integer, Long> scores = queryService.getScoresByDate(matchDate);
        try {
            String logMessage = objectMapper.writeValueAsString(scores);
            logger.info("Scores for match date {}: {}", matchDate, logMessage);
            kafkaTemplate.send("match-logs-topic", logMessage);
        } catch (JsonProcessingException e) {
            String errorMessage = "Error serializing scores data for match date " + matchDate + ": " + e.getMessage();
            logger.error(errorMessage);
            kafkaTemplate.send("match-logs-topic", errorMessage);
        }
        return scores;
    }

    @Operation(summary = "Display the list of all top batsmen in ascending order ", parameters = {
            @Parameter(name = "page", description = "page", example = "5"),
            @Parameter(name = "size", description = "size", example = "10")
    })
    @ApiResponse(description = "returns the list of all top batsmen", responseCode = "200")
    @GetMapping("/topbatsmen")
    public Page<Object[]> getTopBatsmen(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        Page<Object[]> batsmen = queryService.getTopBatsmen(page, size);
        try {
            String logMessage = objectMapper.writeValueAsString(batsmen.getContent());
            logger.info("Top batsmen page {} with size {}: {}", page, size, logMessage);
            kafkaTemplate.send("match-logs-topic", logMessage);
        } catch (JsonProcessingException e) {
            String errorMessage = "Error serializing top batsmen data: " + e.getMessage();
            logger.error(errorMessage);
            kafkaTemplate.send("match-logs-topic", errorMessage);
        }
        return batsmen;
    }


    @GetMapping("/wickets")
    public Long getWickets(@RequestParam String playerName) {
        return wicketService.getWicketsByPlayerName(playerName);
    }

    @GetMapping("/players")
    public List<String> getPlayers(@RequestParam String teamName, @RequestParam int matchNumber) {
        return playerService.getPlayersByTeamNameAndMatchNumber(teamName, matchNumber);
    }

    @GetMapping("/referees")
    public List<String> getReferees(@RequestParam int matchNumber) {
        return officialService.getRefereesByMatchNumber(matchNumber);
    }
}

//    public ResponseEntity<List<Object[]>> getMatchesByPlayerName(@PathVariable String playerName) {
//        List<Object[]> matches = queryService.getMatchesByPlayerName(playerName);
//       // String logMessage = objectMapper.writeValueAsString(matches);
//        //logger.info(logMessage);
//        try {
//            kafkaTemplate.send("match-logs-topic",objectMapper.writeValueAsString(matches));
//        } catch (IOException e) {
//            kafkaTemplate.send("match-logs-topic", "Error serializing matches data: " + e.getMessage());
//        }
//        return ResponseEntity.ok(matches);
//    }

//    @GetMapping("/cumulative-score")
//    public Long getCumulativeScore(@RequestParam("playerName") String playerName) {
//        Long score = queryService.getCumulativeScore(playerName);
//        try {
//            kafkaTemplate.send("match-logs-topic", objectMapper.writeValueAsString(score));
//        } catch (IOException e) {
//            kafkaTemplate.send("match-logs-topic", "Error serializing cumulative score data: " + e.getMessage());
//        }
//        return score;
//    }
//
//    @GetMapping("/score")
//    public Map<Integer, Long> getScoresByDate(@RequestParam String matchDate) {
//        Map<Integer, Long> scores = queryService.getScoresByDate(matchDate);
//        try {
//            kafkaTemplate.send("match-logs-topic", objectMapper.writeValueAsString(scores));
//        } catch (IOException e) {
//            kafkaTemplate.send("match-logs-topic", "Error serializing scores data: " + e.getMessage());
//        }
//        return scores;
//    }
//
//    @GetMapping("/topbatsmen")
//    public Page<Object[]> getTopBatsmen(@RequestParam(defaultValue = "0") int page,
//                                        @RequestParam(defaultValue = "10") int size) {
//        Page<Object[]> batsmen = queryService.getTopBatsmen(page, size);
//        try {
//            kafkaTemplate.send("match-logs-topic", objectMapper.writeValueAsString(batsmen.getContent()));
//        } catch (IOException e) {
//            kafkaTemplate.send("match-logs-topic", "Error serializing top batsmen data: " + e.getMessage());
//        }
//        return batsmen;
//    }

