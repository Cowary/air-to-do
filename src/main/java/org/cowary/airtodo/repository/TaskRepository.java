package org.cowary.airtodo.repository;

import org.cowary.airtodo.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Boolean existsTaskByVikunjaId(Integer vikunjaId);

    List<Task> findAllByIsDone(Boolean isDone);

    List<Task> findAllByIsDoneAndDoneAtAfter(Boolean isDone, LocalDateTime endDate);

    Task findTaskByVikunjaId(Integer vikunjaId);
}
