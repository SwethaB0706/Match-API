package com.indium.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "Innings")
@Data
public class Innings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "innings_id")
    private int inningsId;

    @Column(name = "target_runs")
    private Integer targetRuns;

    @Column(name = "target_overs")
    private Integer targetOvers;

    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match match;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @OneToMany(mappedBy = "innings", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Over> overs;

    @OneToMany(mappedBy = "innings", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Powerplay> powerplays;

    // Constructors, getters and setters
}