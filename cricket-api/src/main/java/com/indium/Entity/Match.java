package com.indium.Entity;

import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Matches")
@Data
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_id")
    private int matchId;

    @Column(name = "data_version")
    private String dataVersion;

    @Column(name = "created")
    private LocalDate created;

    @Column(name = "revision")
    private int revision;

    @Column(name = "city")
    private String city;

    @Column(name = "match_date")
    private LocalDate matchDate;

    @Column(name = "gender")
    private String gender;

    @Column(name = "match_type")
    private String matchType;

    @Column(name = "overs")
    private int overs;

    @Column(name = "season")
    private String season;

    @Column(name = "team_type")
    private String teamType;

    @Column(name = "venue")
    private String venue;

    @Column(name = "toss_winner")
    private String tossWinner;

    @Column(name = "toss_decision")
    private String tossDecision;

    @Column(name = "match_winner")
    private String matchWinner;

    @Column(name = "win_by_runs")
    private Integer winByRuns;

    @Column(name = "win_by_wickets")
    private Integer winByWickets;

    @OneToOne(mappedBy = "match", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Event event;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MatchTeam> matchTeams;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Official> officials;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Innings> innings;

    // Constructors, getters and setters
}

