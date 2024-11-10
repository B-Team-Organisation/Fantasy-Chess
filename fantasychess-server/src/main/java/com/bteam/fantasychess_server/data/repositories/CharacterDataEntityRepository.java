package com.bteam.fantasychess_server.data.repositories;

import com.bteam.fantasychess_server.data.entities.CharacterDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterDataEntityRepository extends JpaRepository<CharacterDataEntity, String> {
}