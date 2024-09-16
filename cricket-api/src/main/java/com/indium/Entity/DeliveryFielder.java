package com.indium.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Deliveryfielder")
@Data
public class DeliveryFielder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_fielder_id")
    private int deliveryFielderId;

    @ManyToOne
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @ManyToOne
    @JoinColumn(name = "fielder_id")
    private Player fielder;


    // Constructors, getters and setters
}