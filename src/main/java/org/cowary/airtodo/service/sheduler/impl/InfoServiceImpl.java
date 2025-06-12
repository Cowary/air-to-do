package org.cowary.airtodo.service.sheduler.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cowary.airtodo.configuration.AppConfig;
import org.cowary.airtodo.entity.Task;
import org.cowary.airtodo.service.db.TaskService;
import org.cowary.airtodo.service.rest.TelegramBotService;
import org.cowary.airtodo.service.sheduler.InfoService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class InfoServiceImpl implements InfoService {
    TaskService taskService;
    TelegramBotService telegramBotService;
    AppConfig appConfig;

    @Override
    @Scheduled(cron = "0 0 9,13,18 * * *", zone = "Europe/Moscow")
    public void sendListNotDoneTasks() {
        var taskList = taskService.getNotDoneThisWeekTasks();
        var report = generateReport(taskList);
        telegramBotService.sendMessage(appConfig.getTelegramUserId(), report);
    }

    public String generateReport(List<Task> tasks) {
        var sortedTasks = tasks
                .stream()
                .filter(task -> task.getPriority() >= 2)
                .sorted(Comparator
                        .comparingInt(Task::getPriority)
                        .thenComparing(
                                Task::getDueAt,
                                Comparator.nullsLast(Comparator.naturalOrder())
                        )
                ).toList();

        Map<Integer, List<Task>> groupedByPriority = sortedTasks.stream()
                .collect(Collectors.groupingBy(Task::getPriority));

        StringBuilder report = new StringBuilder();

        report.append("Total tasks by priority: ");
        for (Map.Entry<Integer, List<Task>> entry : groupedByPriority.entrySet()) {
            int priority = entry.getKey();
            long count = entry.getValue().size();
            String priorityName = getPriorityName(priority);
            if (priorityName != null) {
                report.append(priorityName).append(": ").append(count).append(", ");
            }
        }

        if (!report.isEmpty() && report.charAt(report.length() - 2) == ',') {
            report.setLength(report.length() - 2);
        }

        for (Map.Entry<Integer, List<Task>> entry : groupedByPriority.entrySet()) {
            int priority = entry.getKey();
            List<Task> tasksByPriority = entry.getValue();

            String priorityName = getPriorityName(priority);
            if (priorityName != null) {
                report.append("\n-----\n").append(priorityName).append("\n");

                for (Task task : tasksByPriority) {
                    String dueDateStr = task.getDueAt() != null
                            ? " дата выполнения: " + task.getDueAt()
                            : "";
                    report.append(task.getTitle()).append(dueDateStr).append("\n");
                }
            }
        }

        return report.toString();
    }

    private static String getPriorityName(int priority) {
        return switch (priority) {
            case 4 -> "IMMEDIATE";
            case 3 -> "VERY HIGH";
            case 2 -> "HIGH";
            case 1 -> "MEDIUM";
            case 0 -> "LOW";
            default -> null;
        };
    }
}
