package com.indium.Entity;



import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Overs")
@Data
public class Over {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "over_id")
    private int overId;

    @ManyToOne
    @JoinColumn(name = "innings_id")
    private Innings innings;

    @Column(name = "over_number")
    private int overNumber;

}
