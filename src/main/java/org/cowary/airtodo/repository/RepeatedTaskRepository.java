package org.cowary.airtodo.repository;

import org.cowary.airtodo.entity.RepeatedTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepeatedTaskRepository extends JpaRepository<RepeatedTask, Long> {
    RepeatedTask findRepeatedTaskByVikunjaId(Integer vikunjaId);
}
