package com.mmstechnology.dmw.wallet_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "card")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Card {


    @Id
    @EqualsAndHashCode.Include
    @Column(name = "card_id", nullable = false, unique = true, length = 36)
    @Size(max = 36)
    private String cardId;



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
