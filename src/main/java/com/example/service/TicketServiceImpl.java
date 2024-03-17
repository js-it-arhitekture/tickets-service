package com.example.service;

import com.example.TicketList;
import com.example.TicketRequest;
import com.example.TicketResponse;
import com.example.TicketService;
import com.example.domain.TicketEntity;
import com.example.mapper.TicketMapper;
import com.example.repository.TicketRepository;
import com.google.protobuf.Empty;
import io.quarkus.grpc.GrpcService;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@GrpcService
public class TicketServiceImpl implements TicketService {
    private final TicketRepository repository;
    private final TicketMapper mapper;

    @Inject
    public TicketServiceImpl(TicketRepository repository, TicketMapper mapper){
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @WithTransaction
    public Uni<TicketResponse> create(TicketRequest request) {
        log.info("Creating a new ticket");

        // Validate request data
        if (request.getEventID() == null || request.getEventID().isEmpty() ||
                request.getType() == null || request.getType().isEmpty() ||
                request.getPrice() == null || request.getPrice().isEmpty()) {
            // Handle invalid request
            return Uni.createFrom().failure(new IllegalArgumentException("Invalid ticket request data"));
        }

        // Data is valid, proceed with creating the entity
        TicketEntity entity = mapper.ticketRequestToEntity(request);
        log.info("Mapping completed");
        entity.setId(null);
        return repository.persistAndFlush(entity).map(mapper::ticketEntityToResponse);
    }

    @Override
    @WithTransaction
    public Uni<TicketResponse> get(TicketRequest request) {
        log.info("Getting the ticket. id: " + request.getId());
        return repository.findById(request.getId()).map(mapper::ticketEntityToResponse);
    }

    @Override
    @WithTransaction
    public Uni<TicketResponse> remove(TicketRequest request) {
        log.info("Removing the ticket. id: " + request.getId());
        return repository.deleteById(request.getId()).map(deleted -> TicketResponse.newBuilder().setId(request.getId()).build());
    }

    @Override
    @WithTransaction
    public Uni<TicketResponse> update(TicketRequest request) {
        log.info("Updating the ticket. id: " + request.getId());
        TicketEntity entity = mapper.ticketRequestToEntity(request);
        return repository.findById(request.getId())
                .onItem().ifNull().fail()
                .onItem().ifNotNull().transformToUni(saved ->
                {
                    saved.setEventID(Long.valueOf(request.getEventID()));
                    saved.setType(request.getType());
                    saved.setPrice(new BigDecimal(request.getPrice()));
                    return repository.persistAndFlush(saved).onItem().transform(mapper::ticketEntityToResponse);
                });
    }

    @Override
    @WithTransaction
    public Uni<TicketList> list(Empty request) {
        log.info("Listing all tickets.");
        Uni<List<TicketEntity>> entityList = repository.listAll();
        return entityList.onItem().transform(list -> TicketList.newBuilder()
                .addAllTickets(mapper.entityListToTicketResponseList(list))
                .build());
    }
}
