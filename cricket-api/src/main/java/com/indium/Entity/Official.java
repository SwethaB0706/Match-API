package com.indium.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Official")
@Data
public class Official {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "official_id")
    private int officialId;

    @Column(name = "name")
    private String name;

    @Column(name = "official_type")
    private String officialType;

    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match match;

    // Constructors, getters and setters
}