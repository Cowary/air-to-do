package org.cowary.airtodo.service.db;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.cowary.airtodo.entity.Task;
import org.cowary.vikunja.model.ModelsTask;

import java.util.List;

public interface TaskService {
    Task create(ModelsTask vikunjaTask);

    @Nullable
    Task update(ModelsTask vikunjaTask);

    void delete(List<ModelsTask> vikunjaTaskList);

    @Nonnull
    String getTask();

    @Nullable
    List<Task> getNotDoneThisWeekTasks();
}
