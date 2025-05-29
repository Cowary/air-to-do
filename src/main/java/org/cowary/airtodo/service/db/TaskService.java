package org.cowary.airtodo.service.db;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.cowary.airtodo.entity.Task;
import org.cowary.vikunja.model.ModelsTask;

public interface TaskService {
    Task create(ModelsTask vikunjaTask);

    @Nullable
    Task update(ModelsTask vikunjaTask);

    @Nonnull
    String getTask();
}
