package com.example.log.common.util;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "crated_at")
    @CreationTimestamp
    LocalDateTime cratedAt;

    @Column(name = "updated_at")
    @CreationTimestamp
    LocalDateTime updatedAt;

    @Column(name = "delete_status", nullable = false)
    boolean deleteStatus;

    public void delete() {
        this.deleteStatus = true;
    }
}
