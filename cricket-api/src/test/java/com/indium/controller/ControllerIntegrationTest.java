package com.indium.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testInsertMatchData() throws Exception {
        String url = "http://localhost:" + port + "/api/matches/insert";

        // Load JSON data from file
        String jsonData = new String(Files.readAllBytes(Paths.get("src/test/java/com/indium/resources/SampleDataMatch.json")));

        ResponseEntity<String> response = testRestTemplate.postForEntity(url, jsonData, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo("Match data inserted successfully!");
    }


    @Test
    public void testGetMatchesByPlayerName() {
        String playerName = "AS Yadav";
        String url = "http://localhost:" + port + "/api/matches/player/" + playerName;

        ResponseEntity<List> response = testRestTemplate.getForEntity(url, List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();  // Assuming data for player AS Yadav exists
    }

    @Test
    public void testGetCumulativeScore() {
        String playerName = "AS Yadav";
        String url = "http://localhost:" + port + "/api/matches/cumulative-score?playerName=" + playerName;

        ResponseEntity<Long> response = testRestTemplate.getForEntity(url, Long.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isGreaterThan(0);  // Assuming player has a cumulative score
    }

    @Test
    public void testGetScoresByDate() {
        String matchDate = "2008-04-20";
        String url = "http://localhost:" + port + "/api/matches/score?matchDate=" + matchDate;

        ResponseEntity<Map> response = testRestTemplate.getForEntity(url, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();  // Assuming matches exist on the given date
    }

    @Test
    public void testGetTopBatsmen() {
        String url = "http://localhost:" + port + "/api/matches/topbatsmen?page=0&size=10";

        ResponseEntity<Map> response = testRestTemplate.getForEntity(url, Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsKey("content");  // Verify that content is returned
    }

    @Test
    public void testGetWickets() {
        String playerName = "AS Yadav";
        String url = "http://localhost:" + port + "/api/matches/wickets?playerName=" + playerName;

        ResponseEntity<Long> response = testRestTemplate.getForEntity(url, Long.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isGreaterThanOrEqualTo(0);  // Assuming wickets data exists
    }

    @Test
    public void testGetPlayers() {
        String teamName = "Royal Challengers Bangalore";
        int matchNumber = 12;
        String url = "http://localhost:" + port + "/api/matches/players?teamName=" + teamName + "&matchNumber=" + matchNumber;

        ResponseEntity<List> response = testRestTemplate.getForEntity(url, List.class);

        // Log response status and body
        System.out.println("Response Status: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();  // Ensure that data exists
    }


    @Test
    public void testGetReferees() {
        int matchNumber = 1;
        String url = "http://localhost:" + port + "/api/matches/referees?matchNumber=" + matchNumber;

        ResponseEntity<List> response = testRestTemplate.getForEntity(url, List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();  // Assuming referee data exists for the given match
    }
}

