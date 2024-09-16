package com.indium.service;

import com.indium.repository.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WicketService {

    @Autowired
    private DeliveryRepository deliveryRepository;

    public Long getWicketsByPlayerName(String playerName) {
        return deliveryRepository.findWicketsByPlayerName(playerName);
    }
}
