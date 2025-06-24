package org.cowary.airtodo.entity;

import jakarta.persistence.*;
import lombok.*;
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
public class RepeatedTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    Long id;
    @Column(nullable = false)
    String title;
    String description;
    @Column(nullable = false)
    @CreationTimestamp
    LocalDateTime createdDate;
    @Column(nullable = false)
    @UpdateTimestamp
    LocalDateTime updatedDate;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    RepeatMod repeatMod;
    @Column(nullable = false)
    Integer priority;
    Integer vikunjaId;
    @Column(nullable = false)
    Integer repeatAfter;
}
