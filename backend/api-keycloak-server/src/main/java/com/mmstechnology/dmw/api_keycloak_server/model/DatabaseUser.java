package com.mmstechnology.dmw.api_keycloak_server.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Blob;
import java.time.LocalDateTime;

/**
 * Represents a user record with identity and auditing details.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "database_user")
@Data // Lombok:  Generates getters, setters, toString, equals, and hashCode methods
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Lombok: Use only explicitly included fields for equals and hashCode
@Builder
public class DatabaseUser {

    /**
     * Unique identifier for the user.
     */

    @Id
    @Column(name = "user_id", nullable = false, unique = true, length = 36)
    @EqualsAndHashCode.Include // Lombok: Include userId in equals and hashCode
    @Size(max = 36)
    private String userId;

    /**
     * The user's image stored as a BLOB.
     * Note: Consider using an external storage solution for large images.
     */
    @Lob
    @Column(name = "image")
    private Blob image;

    /**
     * User's phone number (max length 20).
     */
    @Size(max = 20)
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    /**
     * Timestamp of the user's last login.
     */
    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    /**
     * The count of consecutive failed login attempts.
     */
    @Column(name = "failed_attempts")
    private Integer failedAttempts = 0;

    /**
     * The List of Id of Wallet of an User
     */
    @Column(name = "wallet" , length = 36)
    private String walletId;

    /**
     * Timestamp when the record was created.
     * Automatically set when the entity is persisted.
     */
    @CreatedDate
    @Column(name = "create_dt", updatable = false)
    private LocalDateTime createDt;

    /**
     * Timestamp when the record was last updated.
     * Automatically updated on entity modification.
     */
    @LastModifiedDate
    @Column(name = "update_dt")
    private LocalDateTime updateDt;

    /**
     * Version field for optimistic locking.
     */
    @Version
    private Integer version;

}
