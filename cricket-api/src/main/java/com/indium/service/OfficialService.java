package com.indium.service;

import com.indium.repository.OfficialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfficialService {

    @Autowired
    private OfficialRepository officialRepository;

    public List<String> getRefereesByMatchNumber(int matchNumber) {
        return officialRepository.findRefereesByMatchNumber(matchNumber);
    }
}

