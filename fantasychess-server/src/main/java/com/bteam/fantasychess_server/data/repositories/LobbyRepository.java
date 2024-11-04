package com.bteam.fantasychess_server.data.repositories;

import com.bteam.fantasychess_server.data.entities.LobbyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LobbyRepository extends JpaRepository<LobbyEntity, UUID> {
}
