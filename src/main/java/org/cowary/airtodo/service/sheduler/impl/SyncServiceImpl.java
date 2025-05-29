package org.cowary.airtodo.service.sheduler.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.cowary.airtodo.service.db.TaskService;
import org.cowary.airtodo.service.rest.VikunjaService;
import org.cowary.vikunja.model.ModelsTaskRepeatMode;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SyncServiceImpl {
    VikunjaService vikunjaService;
    TaskService taskService;

    @Scheduled(fixedRate = 60000)
    public void sync() {
        LOGGER.info("Sync started");
        var vikunjaTaskList = vikunjaService.getAllTasks()
                .stream()
                .filter(task -> task.getRepeatAfter().equals(0) )
                .toList();
        LOGGER.info("vikunjaTaskList: {}", vikunjaTaskList);
        vikunjaTaskList.forEach(taskService::create);
        vikunjaTaskList.forEach(taskService::update);

        LOGGER.info("Sync finished");
    }
}
