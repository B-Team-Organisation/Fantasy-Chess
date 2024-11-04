package com.bteam.fantasychess_server.data.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
public class TokenEntity {
    @Id
    @Column(name = "id", nullable = false, length = 16)
    @JdbcTypeCode(SqlTypes.LONGVARCHAR)
    private String id;
    @Column(name = "userId", nullable = false)
    private UUID userId;
    @Column(name = "expires")
    private long expires;

    public TokenEntity(String id, UUID userId) {
        this.id = id;
        this.userId = userId;
    }

    public TokenEntity() {
        id = UUID.randomUUID().toString();
        userId = null;
    }

    public String getId() {
        return id;
    }

    public UUID getUserID() {
        return userId;
    }

    public long getExpires() {
        return expires;
    }
}
