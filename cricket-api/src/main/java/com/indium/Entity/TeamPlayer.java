package com.indium.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Teamplayer")
@Data
public class TeamPlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_player_id")
    private int teamPlayerId;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match match;

    // Constructors, getters and setters
}
