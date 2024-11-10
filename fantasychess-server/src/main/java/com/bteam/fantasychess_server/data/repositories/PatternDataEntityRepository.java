package com.bteam.fantasychess_server.data.repositories;

import com.bteam.fantasychess_server.data.entities.PatternDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatternDataEntityRepository extends JpaRepository<PatternDataEntity, String> {
}