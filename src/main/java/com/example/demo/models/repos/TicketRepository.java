package com.example.demo.models.repos;


import com.example.demo.models.TicketEntity;
import com.example.demo.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository   extends JpaRepository<TicketEntity, Long> {
    TicketEntity findByCreator(Long creatorID);

    UserEntity getByCreator(UserEntity firstByEmail);
}
