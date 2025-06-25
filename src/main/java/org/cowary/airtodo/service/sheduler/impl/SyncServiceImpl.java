package org.cowary.airtodo.service.sheduler.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cowary.airtodo.service.db.TaskService;
import org.cowary.airtodo.service.rest.VikunjaService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SyncServiceImpl {
    VikunjaService vikunjaService;
    TaskService taskService;

    @Scheduled(fixedDelay = 300000)
    public void sync() {
        LOGGER.info("Sync with vikunja started");
        var vikunjaTaskList = vikunjaService.getAllTasks()
                .stream()
                .toList();
        LOGGER.trace("vikunjaTaskList: {}", vikunjaTaskList);
        vikunjaTaskList.stream().filter(task -> task.getRepeatAfter().equals(0) ).forEach(taskService::create);
        vikunjaTaskList.stream().filter(task -> !task.getRepeatAfter().equals(0) ).forEach(taskService::createRepatedTask);
        vikunjaTaskList.forEach(taskService::update);
        taskService.delete(vikunjaTaskList);

        LOGGER.info("Sync with vikunja finished");
    }
}
