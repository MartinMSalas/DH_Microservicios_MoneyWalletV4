package com.mmstechnology.dmw.wallet_service.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
public class Cvu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cvuId; // MySQL auto-generated consecutive number

    @Nonnull
    @Size(max = 22)
    @Column(unique = true)  // Ensures the CVU field is unique
    private String cvu;


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
