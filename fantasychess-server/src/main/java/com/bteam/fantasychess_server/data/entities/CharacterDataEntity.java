package com.bteam.fantasychess_server.data.entities;

import jakarta.persistence.*;

@Entity
public class CharacterDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
