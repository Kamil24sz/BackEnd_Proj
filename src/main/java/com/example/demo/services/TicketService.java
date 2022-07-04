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

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TicketService {

    private final TicketRepository ticketRepository;

    private final HttpServletRequest httpServletRequest;

    private final UserRepository userRepository;

    private final AuthenticationService authenticationService;

    public boolean createTicket(TicketCreationRequest ticket){
        try{
            if (!authenticationService.checkUser(ticket.getCreator()))
                return false;
            TicketEntity ticketEntity = new TicketEntity();
            ticketEntity.setTitle(ticket.getTitle());
            ticketEntity.setDescription(ticket.getDescription());
            ticketEntity.setCreator(userRepository.findFirstByEmail(ticket.getCreator()));
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
    public boolean modifyTicket(TicketCreationRequest ticket){
        if (!authenticationService.checkUser(ticket.getCreator()))
            return false;
        if ((Long)ticket.getId() == null)
            return false;
        TicketEntity originalTicket = ticketRepository.findById(ticket.getId()).orElse(null);

        if(originalTicket == null)
            return false;

        if (!originalTicket.getCreator().getEmail().equals((ticket.getCreator())))
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


}
