package com.bteam.fantasychess_server.data.repositories;

import com.bteam.fantasychess_server.data.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GameRepository extends JpaRepository<Game, UUID> {
}
