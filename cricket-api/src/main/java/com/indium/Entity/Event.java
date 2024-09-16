package com.indium.Entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Event")
@Data
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private int eventId;

    @Column(name = "name")
    private String name;

    @Column(name = "match_number")
    private int matchNumber;

    @OneToOne
    @JoinColumn(name = "match_id")
    private Match match;

    // Constructors, getters and setters
}

