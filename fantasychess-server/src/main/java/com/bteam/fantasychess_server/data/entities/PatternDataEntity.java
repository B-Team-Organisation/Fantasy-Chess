package com.bteam.fantasychess_server.data.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class PatternDataEntity {

    @Id
    private String id;

    @OneToMany
    private List<PatternDataEntity> subPatterns;
}
