package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Table(name = "TICKET_ENTITY")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String status;
    private String priority;
    private String resolution;

    @JsonIgnore
    @OneToOne
    private UserEntity creator;

    @JsonIgnore
    @OneToOne
    private UserEntity resolver;
}
