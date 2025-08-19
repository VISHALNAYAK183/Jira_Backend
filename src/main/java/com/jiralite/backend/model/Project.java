package com.jiralite.backend.model;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "projects")
public class Project {
   @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "org_id", nullable = false)
    private Organization organization;

    private String name;
    private String description;

    @Column(name = "created_at")
    private Instant createdAt = Instant.now();
}
