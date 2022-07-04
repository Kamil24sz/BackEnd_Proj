package com.example.demo.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketCreationRequest {

    long id;
    private String title;
    private String description;
    private String creator;
}
