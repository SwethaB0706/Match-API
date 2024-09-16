package com.indium.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "Delivery")
@Data
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private int deliveryId;

    @Column(name = "runs_batter")
    private int runsBatter;

    @Column(name = "runs_extras")
    private int runsExtras;

    @Column(name = "runs_total")
    private int runsTotal;

    @Column(name = "extras_type")
    private String extrasType;

    @Column(name = "wicket_kind")
    private String wicketKind;

    @ManyToOne
    @JoinColumn(name = "over_id")
    private Over over;

    @ManyToOne
    @JoinColumn(name = "batter_id")
    private Player batter;

    @ManyToOne
    @JoinColumn(name = "bowler_id")
    private Player bowler;

    @ManyToOne
    @JoinColumn(name = "non_striker_id")
    private Player nonStriker;

    @ManyToOne
    @JoinColumn(name = "player_out_id")
    private Player playerOut;

    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DeliveryFielder> deliveryFielders;

    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match match;



    // Constructors, getters and setters
}
