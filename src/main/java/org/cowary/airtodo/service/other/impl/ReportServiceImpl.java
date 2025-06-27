package org.cowary.airtodo.service.other.impl;

import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cowary.airtodo.entity.Task;
import org.cowary.airtodo.service.other.ReportService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ReportServiceImpl implements ReportService {

    @Override
    public String generateReport(@Nullable List<Task> tasks) {
        if (CollectionUtils.isEmpty(tasks)) {
            return "Список задач пуст. Вы свободны!";
        }
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
                    String dueDateStr = (task.getDueAt() != null && task.getDueAt().isAfter(LocalDateTime.of(2000, 1, 1, 0, 0)))
                            ? "\nдата: " + task.getDueAt()
                            : "";
                    report.append(task.getTitle()).append(dueDateStr).append("\n");
                }
            }
        }

        return report.toString();
    }

    @Override
    public String doneTaskMessage(Task task) {
        return String.format("Ура! Выполнена задача %s!", task.getTitle());
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
