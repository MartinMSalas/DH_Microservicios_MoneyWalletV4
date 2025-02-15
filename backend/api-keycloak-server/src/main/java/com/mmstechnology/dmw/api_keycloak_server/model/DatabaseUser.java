package com.mmstechnology.dmw.api_keycloak_server.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class DatabaseUser {

    @Id
    @Column(nullable = false, name = "user_id")
    String userId;

    Blob image;


    @CreatedDate
    @Column(updatable = false, name= "create_dt")
    private LocalDateTime createDt;
    @LastModifiedDate
    @Column(name= "update_dt")
    private LocalDateTime updateDt;
    @Version
    private Integer version;
}
