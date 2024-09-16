package com.indium.service;

import com.indium.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    public List<String> getPlayersByTeamNameAndMatchNumber(String teamName, int matchNumber) {
        return playerRepository.findPlayerNamesByTeamNameAndMatchNumber(teamName, matchNumber);
    }
}
