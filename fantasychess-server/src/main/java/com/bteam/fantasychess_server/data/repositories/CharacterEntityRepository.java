package com.bteam.fantasychess_server.data.repositories;

import com.bteam.fantasychess_server.data.entities.CharacterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterEntityRepository extends JpaRepository<CharacterEntity, Long> {
}