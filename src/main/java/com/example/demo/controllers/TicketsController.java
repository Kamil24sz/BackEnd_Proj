package com.example.demo.controllers;

import com.example.demo.models.Role;
import com.example.demo.models.TicketEntity;
import com.example.demo.models.repos.RoleRepository;
import com.example.demo.models.repos.UserRepository;
import com.example.demo.requests.TicketCreationRequest;
import com.example.demo.requests.UserRegisterRequest;
import com.example.demo.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TicketsController {

    private final TicketService ticketService;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @RequestMapping(
            value = "/api/ticket/create",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST
    )
    @ResponseBody
    public String createTicket(
            HttpServletRequest httpServletRequest,
            @RequestBody TicketCreationRequest ticket
    ){
        if (httpServletRequest.getSession().getAttributeNames().hasMoreElements() == false)
            return "User not logged in";

        String email =  httpServletRequest.getSession().getAttributeNames().nextElement();

        if(this.ticketService.createTicket(ticket, email))
            return "Ticket created successfully!";

        return "Failed to create ticket!";
    }

    @RequestMapping(
            value = "/api/ticket/{id}/modify",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.PUT
    )
    @ResponseBody
    public String modifyTicket(
            HttpServletRequest httpServletRequest,
            @RequestBody TicketCreationRequest ticket, @PathVariable long id
    ){
        if (httpServletRequest.getSession().getAttributeNames().hasMoreElements() == false)
            return "User not logged in";

        String email =  httpServletRequest.getSession().getAttributeNames().nextElement();

        if(this.ticketService.modifyTicket(ticket, id, email))
            return "Ticket modified successfully!";

        return "Failed to modify ticket!";
    }

    @RequestMapping(
            value = "/api/ticket/show/mine"
    )
    @ResponseBody
    public List<TicketEntity> showMineTickets(
            HttpServletRequest httpServletRequest
    ){
        if (httpServletRequest.getSession().getAttributeNames().hasMoreElements() == false)
            return null;

        String email =  httpServletRequest.getSession().getAttributeNames().nextElement();

            return ticketService.showMyTickets(email);
    }

    @RequestMapping(
            value = "/api/admin/ticket/show/all"
    )
    @ResponseBody
    public List<TicketEntity> showAllTickets(
            HttpServletRequest httpServletRequest
    ){
        if (httpServletRequest.getSession().getAttributeNames().hasMoreElements() == false)
            return null;

        String email =  httpServletRequest.getSession().getAttributeNames().nextElement();

        if(!CheckIfAdmin(email)){
            return null;
        }

        return ticketService.showAllTickets(email);
    }

    @RequestMapping(
            value = "/api/admin/ticket/show/open"
    )
    @ResponseBody
    public List<TicketEntity> showOpenTickets(
            HttpServletRequest httpServletRequest
    ){
        if (httpServletRequest.getSession().getAttributeNames().hasMoreElements() == false)
            return null;

        String email =  httpServletRequest.getSession().getAttributeNames().nextElement();


        if(!CheckIfAdmin(email)){
            return null;
        }

        return ticketService.showOpenTickets(email);
    }

    @RequestMapping(
            value = "/api/admin/ticket/show/closed"
    )
    @ResponseBody
    public List<TicketEntity> showClosedTickets(
            HttpServletRequest httpServletRequest
    ){
        if (httpServletRequest.getSession().getAttributeNames().hasMoreElements() == false)
            return null;

        String email =  httpServletRequest.getSession().getAttributeNames().nextElement();


        if(!CheckIfAdmin(email)){
            return null;
        }
        return ticketService.showClosedTickets(email);
    }

    @RequestMapping(
            value = "/api/admin/ticket/show/resolved/ByMe"
    )
    @ResponseBody
    public List<TicketEntity> showResolvedByMeTickets(
            HttpServletRequest httpServletRequest
    ){
        if (httpServletRequest.getSession().getAttributeNames().hasMoreElements() == false)
            return null;

        String email =  httpServletRequest.getSession().getAttributeNames().nextElement();

        if(!CheckIfAdmin(email)){
            return null;
        }
        return ticketService.showResolvedByMeTickets(email);
    }

    @RequestMapping(
            value = "/api/admin/ticket/{id}/resolve",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.PUT
    )
    @ResponseBody
    public String resolveTicket(
            HttpServletRequest httpServletRequest,
            @RequestBody TicketEntity ticket, @PathVariable long id
    ){
        if (httpServletRequest.getSession().getAttributeNames().hasMoreElements() == false)
            return "User not login in!";

        String email =  httpServletRequest.getSession().getAttributeNames().nextElement();

        if(!CheckIfAdmin(email)){
            return "User has no privilege";
        }
        return ticketService.resolveTicket(ticket, id, email);
    }

    @RequestMapping(
            value = "/api/admin/ticket/{priority}"
    )
    @ResponseBody
    public String changeTicketPriority(
            HttpServletRequest httpServletRequest,
            @PathVariable String priority, @RequestParam long id
            ){
        if (httpServletRequest.getSession().getAttributeNames().hasMoreElements() == false)
            return "User not login in!";
        String email =  httpServletRequest.getSession().getAttributeNames().nextElement();
        if(!CheckIfAdmin(email)){
            return "User has no privilege";
        }
        return ticketService.changeTicketPriority(id, email, priority);
    }

    public boolean CheckIfAdmin(String email){
        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        if(userRepository.findFirstByEmail(email).getRoles().contains(adminRole))
            return true;
        return false;
    }
}
