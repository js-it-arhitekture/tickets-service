package com.example.repository;

import com.example.domain.TicketEntity;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Blocking
public class TicketRepository implements PanacheRepository<TicketEntity> {
}
