package org.cowary.airtodo.service.rest;

import org.cowary.vikunja.model.ModelsTask;

import java.util.List;

public interface VikunjaService {
    List<ModelsTask> getActiveSprintTasks();

    List<ModelsTask> getCompletedSprintTasks();

    List<ModelsTask> getLastWeekTasks();

    List<ModelsTask> getAllTasks();
}
