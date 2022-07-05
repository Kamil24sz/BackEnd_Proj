package com.example.demo.models.repos;


import com.example.demo.models.TicketEntity;
import com.example.demo.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository   extends JpaRepository<TicketEntity, Long> {
    TicketEntity findByCreator(Long creatorID);

    UserEntity getByCreator(UserEntity firstByEmail);

    List<TicketEntity> getAllByCreator(UserEntity firstByEmail);

    List<TicketEntity> findAllBy();

    List<TicketEntity> findAllByStatus(String status);

    List<TicketEntity> findAllByResolver(UserEntity user);
}
