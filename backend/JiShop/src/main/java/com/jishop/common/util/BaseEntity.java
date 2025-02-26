package com.jishop.common.util;

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

    @CreationTimestamp
    @Column(name = "crated_at")
    LocalDateTime cratedAt;

    @CreationTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Column(name = "delete_status", nullable = false)
    boolean deleteStatus;

    public void delete() {
        this.deleteStatus = true;
    }
}
