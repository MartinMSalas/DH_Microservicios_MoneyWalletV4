package com.mmstechnology.dmw.wallet_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "wallet")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {

    /**
     * Unique identifier for the wallet.
     */
    @Id
    @Column(name = "wallet_id", nullable = false, unique = true, length = 36)
    @EqualsAndHashCode.Include
    @Size(max = 36)
    private String walletId;

    /**
     * Wallet User ID (max length 36).
     */
    @Column(name = "user_id", nullable = false, length = 36)
    @Size(max = 36)
    private String userId;

    /**
     * Wallet's CVU (max length 22).
     */
    @Size(max = 22)
    @Column(name = "cvu", length = 22)
    private String cvu;

    /**
     * Wallet's alias (max length 36).
     */
    @Size(max = 36)
    @Column(name = "alias", length = 36)
    private String alias;

    /**
     * The balance of the wallet.
     * Using BigDecimal to ensure precision for financial calculations.
     */
    @Column(name = "balance", precision = 19, scale = 4, nullable = false)
    private BigDecimal balance;

    /**
     * The currency of the wallet.
     * Default value is set to 'ARS'.
     */
    @Column(name = "currency", length = 3, nullable = false, columnDefinition = "varchar(3) default 'ARS'")
    private String currency;

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
