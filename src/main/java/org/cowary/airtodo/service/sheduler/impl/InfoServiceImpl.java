package org.cowary.airtodo.service.sheduler.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cowary.airtodo.configuration.AppConfig;
import org.cowary.airtodo.service.db.TaskService;
import org.cowary.airtodo.service.other.ReportService;
import org.cowary.airtodo.service.rest.TelegramBotService;
import org.cowary.airtodo.service.sheduler.InfoService;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class InfoServiceImpl implements InfoService {
    TaskService taskService;
    TelegramBotService telegramBotService;
    AppConfig appConfig;
    ReportService reportService;

    @Scheduled(cron = "0 0 9,13,18 * * *", zone = "Europe/Moscow")
    public void sendListNotDoneTasks() {
        LOGGER.info("start method by scheduler: sendListNotDoneTasks");
        var taskList = taskService.getNotDoneThisWeekTasks();
        var report = reportService.generateReport(taskList);
        telegramBotService.sendMessage(appConfig.getTelegramUserId(), report);
        LOGGER.info("stop method by scheduler: sendListNotDoneTasks");
    }

    @EventListener(ContextRefreshedEvent.class)
    public void sendMessageIsDone() {
        LocalDateTime now = LocalDateTime.now();
        while (true) {
            LOGGER.info("Start method sendMessageIsDone");
            var taskList = taskService.getIsDoneAndAfterDate(Boolean.TRUE, now);
            now = LocalDateTime.now();
            if (!CollectionUtils.isEmpty(taskList)) {
                for (var task : taskList) {
                    var message = reportService.doneTaskMessage(task);
                    telegramBotService.sendMessage(appConfig.getTelegramUserId(), message);
                    LOGGER.debug("Send message about the close task with id: {}", task.getId());
                }
            }
            try {
                Thread.sleep(Duration.ofMinutes(5));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
