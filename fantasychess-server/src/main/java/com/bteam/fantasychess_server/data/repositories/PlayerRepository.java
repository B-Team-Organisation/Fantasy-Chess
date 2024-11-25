package com.bteam.fantasychess_server.data.repositories;

import com.bteam.fantasychess_server.data.entities.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlayerRepository extends JpaRepository<PlayerEntity, UUID> {
}