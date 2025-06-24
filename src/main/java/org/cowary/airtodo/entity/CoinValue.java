package org.cowary.airtodo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class CoinValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    Long id;
    @Column(nullable = false)
    @CreationTimestamp
    LocalDateTime createdDate;
    @Column(nullable = false)
    @UpdateTimestamp
    LocalDateTime updatedDate;
    @Column(nullable = false)
    Long amount;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    Priority priority;
}
