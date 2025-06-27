package org.cowary.airtodo.service.other;

import jakarta.annotation.Nullable;
import org.cowary.airtodo.entity.Task;

import java.util.List;

public interface ReportService {
    String generateReport(@Nullable List<Task> tasks);

    String doneTaskMessage(Task task);
}
