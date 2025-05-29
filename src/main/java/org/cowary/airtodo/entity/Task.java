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
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    Long id;
    @Column(nullable = false)
    String title;
    String description;
    @Column(nullable = false)
    Boolean isDone;
    LocalDateTime doneAt;
    LocalDateTime dueAt;
    @Column(nullable = false)
    @CreationTimestamp
    LocalDateTime createdDate;
    @Column(nullable = false)
    @UpdateTimestamp
    LocalDateTime updatedDate;
    @Column(nullable = false)
    Boolean isRepeat;
    @Column(nullable = false)
    Integer priority;
    LocalDateTime startDate;
    LocalDateTime endDate;
    Long vikunjaId;
    @ManyToOne
    @JoinColumn(name = "repeated_task_id")
    RepeatedTask repeatedTask;
}
