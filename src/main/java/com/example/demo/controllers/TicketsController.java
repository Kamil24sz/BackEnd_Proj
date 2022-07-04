package com.example.demo.controllers;

import com.example.demo.models.TicketEntity;
import com.example.demo.requests.TicketCreationRequest;
import com.example.demo.requests.UserRegisterRequest;
import com.example.demo.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TicketsController {

    private final TicketService ticketService;

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
        if(this.ticketService.createTicket(ticket))
            return "Ticket created successfully!";

        return "Failed to create ticket!";
    }

    @RequestMapping(
            value = "/api/ticket/modify",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.PUT
    )
    @ResponseBody
    public String modifyTicket(
            HttpServletRequest httpServletRequest,
            @RequestBody TicketCreationRequest ticket
    ){
        if(this.ticketService.modifyTicket(ticket))
            return "Ticket modified successfully!";

        return "Failed to modify ticket!";
    }
}
