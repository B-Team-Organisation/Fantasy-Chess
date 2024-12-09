package com.bteam.fantasychess_server.data.repositories;

import com.bteam.fantasychess_server.data.entities.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GameRepository extends JpaRepository<GameEntity, UUID> {
}
