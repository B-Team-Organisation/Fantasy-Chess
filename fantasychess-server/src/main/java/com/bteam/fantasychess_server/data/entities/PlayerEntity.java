package com.bteam.fantasychess_server.data.entities;

import com.bteam.common.models.Player;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
public class PlayerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Player.Status status;

    public PlayerEntity(String name) {
        this.name = name;
        this.status = Player.Status.NOT_READY;
    }

    public PlayerEntity() {
        id = UUID.randomUUID();
        name = "";
        status = Player.Status.NOT_READY;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Player.Status getStatus() {
        return status;
    }

    public void setStatus(Player.Status status) {
        this.status = status;
    }
}
