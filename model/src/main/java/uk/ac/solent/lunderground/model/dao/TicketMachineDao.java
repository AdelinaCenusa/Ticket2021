package uk.ac.solent.lunderground.model.dao;

import uk.ac.solent.lunderground.model.dto.TicketMachine;

import java.util.List;

public interface TicketMachineDao
{
    void addTicketMachine(TicketMachine ticketMachine);
    List<TicketMachine> retrieveAll();
    TicketMachine getTicketMachine(String uuid);
    void deleteAll();
    void updateTicketMachine(TicketMachine ticketMachine);
}
