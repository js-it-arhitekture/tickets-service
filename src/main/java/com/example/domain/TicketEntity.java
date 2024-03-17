package com.example.domain;
import jakarta.persistence.*;
import lombok.*;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;


@Getter
@Setter
@ToString
@Entity
@Table(name = "TICKETS")
public class TicketEntity {
    @Id
    @SequenceGenerator(name = "tickets_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "tickets_id_seq")
    private Long id;
    private Long eventID;
    private String type;
    private BigDecimal price;
    private String boughtAt;
    public TicketEntity() {
        this.boughtAt = new Timestamp(System.currentTimeMillis()).toString();
    }
}

