package com.indium.Entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Matchteam")
@Data
public class MatchTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_team_id")
    private int matchTeamId;

    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match match;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    // Constructors, getters and setters
}