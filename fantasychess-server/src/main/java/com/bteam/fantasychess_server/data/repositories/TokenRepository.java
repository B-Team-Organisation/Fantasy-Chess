package com.bteam.fantasychess_server.data.repositories;

import com.bteam.fantasychess_server.data.entities.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TokenRepository extends JpaRepository<TokenEntity, String> {
    TokenEntity findTokenEntitiesByUserId(UUID userId);
}