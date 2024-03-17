package com.example;

import com.example.domain.TicketEntity;
import com.example.repository.TicketRepository;
import io.quarkus.grpc.GrpcClient;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
class TicketServiceImplTest {

    @GrpcClient
    TicketService ticketService;

    @Test
    void testCreate() {
        // Given
        TicketRequest request = TicketRequest.newBuilder()
                .setEventID("12345")
                .setType("Concert")
                .setPrice("50.00")
                .build();

        // When
        Uni<TicketResponse> responseUni = ticketService.create(request);

        // Then
        responseUni.subscribe().with(response -> {
            assertNotNull(response);
            assertEquals(1L, response.getId()); // Assuming the generated ID is 1L
            assertEquals(request.getEventID(), response.getEventID());
            assertEquals(request.getType(), response.getType());
            assertEquals(new BigDecimal(request.getPrice()), response.getPrice());
        });
    }
}
