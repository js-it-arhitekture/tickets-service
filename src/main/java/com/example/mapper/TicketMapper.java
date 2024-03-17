package com.example.mapper;
import com.example.TicketRequest;
import com.example.TicketResponse;
import com.example.domain.TicketEntity;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "jakarta")
public interface TicketMapper {
    TicketEntity ticketRequestToEntity(TicketRequest ticketRequest);
    TicketEntity ticketResponseToEntity(TicketResponse ticketResponse);
    TicketResponse ticketEntityToResponse(TicketEntity ticketEntity);
    List<TicketResponse> entityListToTicketResponseList(List<TicketEntity> entityList);

}
