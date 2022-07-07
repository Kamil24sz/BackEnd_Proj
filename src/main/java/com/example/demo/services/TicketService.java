package com.example.demo.services;

import com.example.demo.models.TicketEntity;
import com.example.demo.models.UserEntity;
import com.example.demo.models.repos.TicketRepository;
import com.example.demo.models.repos.UserRepository;
import com.example.demo.requests.TicketCreationRequest;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bytecode.Throw;
import org.apache.http.HttpInetConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TicketService {

    private final TicketRepository ticketRepository;

    private final HttpServletRequest httpServletRequest;

    private final UserRepository userRepository;

    private final AuthenticationService authenticationService;

    public boolean createTicket(TicketCreationRequest ticket, String  email){
        try{
            if (!authenticationService.checkUser(email))
                return false;
            TicketEntity ticketEntity = new TicketEntity();
            ticketEntity.setTitle(ticket.getTitle());
            ticketEntity.setDescription(ticket.getDescription());
            ticketEntity.setCreator(userRepository.findFirstByEmail(email));
            ticketEntity.setStatus("open");
            ticketEntity.setPriority("medium");
            ticketRepository.saveAndFlush(ticketEntity);
            return true;
        }
        catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean modifyTicket(TicketCreationRequest ticket, long id, String email){
        if (!authenticationService.checkUser(email))
            return false;
        if ((Long)id == null)
            return false;
        TicketEntity originalTicket = ticketRepository.findById(id).orElse(null);

        if(originalTicket == null)
            return false;

        if (!originalTicket.getCreator().getEmail().equals(email))
            return false;


        boolean cond1 = ticket.getDescription() != null && !ticket.getDescription().equals(originalTicket.getDescription());
        if (cond1)
            originalTicket.setDescription(ticket.getDescription());
        boolean cond2 = ticket.getTitle() != null && !ticket.getTitle().equals(originalTicket.getTitle());
        if (cond2)
            originalTicket.setTitle(ticket.getTitle());
        if (cond1 || cond2) {
            ticketRepository.saveAndFlush(originalTicket);
            return true;
        }

        return false;
    }


    public List<TicketEntity> showMyTickets(String email) {
        if (!authenticationService.checkUser(email))
            return new ArrayList<>();

        return ticketRepository.getAllByCreator(userRepository.findFirstByEmail(email));

    }

    public List<TicketEntity> showAllTickets(String email) {
        if (!authenticationService.checkUser(email))
            return new ArrayList<>();

        return ticketRepository.findAllBy();
    }

    public List<TicketEntity> showOpenTickets(String email) {
        if (!authenticationService.checkUser(email))
            return new ArrayList<>();

        return ticketRepository.findAllByStatus("open");
    }

    public List<TicketEntity> showClosedTickets(String email) {
        if (!authenticationService.checkUser(email))
            return new ArrayList<>();

        return ticketRepository.findAllByStatus("closed");
    }

    public List<TicketEntity> showResolvedByMeTickets(String email) {
        if (!authenticationService.checkUser(email))
            return new ArrayList<>();

        return ticketRepository.findAllByResolver(userRepository.findFirstByEmail(email));
    }

    public String resolveTicket(TicketEntity ticket, long id, String email) {
        TicketEntity originalTicket = ticketRepository.findById(id).orElse(null);
        if(originalTicket == null)
            return "Failed to add resolution note";

        if (ticket.getResolution() == null)
            return "Resolution note cannot be null";

        originalTicket.setResolution(ticket.getResolution());
        originalTicket.setResolver(userRepository.findFirstByEmail(email));
        originalTicket.setStatus("closed");
        ticketRepository.saveAndFlush(originalTicket);
        return "Resolution note added successfully, ticket changed status to closed";

    }

    public String changeTicketPriority(long id, String email, String priority) {

        if (email == null)
            return "User is not log in!";

        if (!authenticationService.checkUser(email))
            return "Failed to authorise";

        TicketEntity originalTicket = ticketRepository.findById(id).orElse(null);

        if(originalTicket == null)
            return "Failed to change ticket priority, ticket is null!";

        if (priority == null)
            return "Priority cannot be null!";

        if ((priority.equals(originalTicket.getPriority())))
            return "Priority is already set to provided value";

        originalTicket.setPriority(priority);

        ticketRepository.saveAndFlush(originalTicket);
        return "Ticket priority changed successful!";
    }
}
