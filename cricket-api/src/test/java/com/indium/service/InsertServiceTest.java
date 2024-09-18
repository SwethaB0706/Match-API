package com.indium.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.indium.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


import static org.mockito.Mockito.*;

@SpringBootTest
public class InsertServiceTest {

    @InjectMocks
    private InsertService myService;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private MatchTeamRepository matchTeamRepository;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private TeamPlayerRepository teamPlayerRepository;

    @Mock
    private OfficialRepository officialRepository;

    @Mock
    private InningsRepository inningsRepository;

    @Mock
    private OverRepository overRepository;

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private DeliveryFielderRepository deliveryFielderRepository;

    @Mock
    private PowerplayRepository powerplayRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Transactional
    public void testInsertMatchData() throws IOException {
        String jsonData = new String(Files.readAllBytes(Paths.get("src/test/java/com/indium/resources/SampleDataMatch.json")));

        // Mock repository methods
        when(matchRepository.save(any())).thenReturn(null);
        when(eventRepository.save(any())).thenReturn(null);
        when(teamRepository.save(any())).thenReturn(null);
        when(matchTeamRepository.save(any())).thenReturn(null);
        when(playerRepository.save(any())).thenReturn(null);
        when(teamPlayerRepository.save(any())).thenReturn(null);
        when(officialRepository.save(any())).thenReturn(null);
        when(inningsRepository.save(any())).thenReturn(null);
        when(overRepository.save(any())).thenReturn(null);
        when(deliveryRepository.save(any())).thenReturn(null);
        when(deliveryFielderRepository.save(any())).thenReturn(null);
        when(powerplayRepository.save(any())).thenReturn(null);

        // Call the method to be tested
        myService.insertMatchData(jsonData);

        // Verify interactions
        verify(matchRepository).save(any());
        verify(eventRepository).save(any());
        verify(teamRepository, times(2)).save(any()); // Two teams
        verify(matchTeamRepository, times(2)).save(any()); // Two match teams
        verify(playerRepository, atLeast(4)).save(any()); // At least 4 players (adjust as needed)
        verify(teamPlayerRepository, atLeast(4)).save(any()); // At least 4 team players
        verify(officialRepository, atLeast(4)).save(any()); // At least 4 officials
        verify(inningsRepository, atLeast(1)).save(any()); // At least 1 call
        verify(overRepository, atLeast(38)).save(any());
        verify(deliveryRepository,atLeast(229)).save(any());
        verify(powerplayRepository,atLeast(4)).save(any());
    }


    // Additional test cases can be added here for edge cases or error scenarios
}
