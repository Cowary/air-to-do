package org.cowary.airtodo.service.db.impl;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cowary.airtodo.entity.Task;
import org.cowary.airtodo.repository.TaskRepository;
import org.cowary.airtodo.service.db.TaskService;
import org.cowary.airtodo.utils.DateHelper;
import org.cowary.vikunja.model.ModelsTask;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    TaskRepository taskRepository;

    @Override
    @Nullable
    public Task create(ModelsTask vikunjaTask) {
        if (taskRepository.existsTaskByVikunjaId(vikunjaTask.getId())) {
            return null;
        }
        var task = Task.builder()
                .title(vikunjaTask.getTitle())
                .description(vikunjaTask.getDescription())
                .isDone(vikunjaTask.getDone())
                .doneAt(DateHelper.toLocalDateTime(vikunjaTask.getDoneAt()))
                .dueAt(DateHelper.toLocalDateTime(vikunjaTask.getDueDate()))
                .isRepeat(false)
                .priority(vikunjaTask.getPriority())
                .startDate(DateHelper.toLocalDateTime(vikunjaTask.getStartDate()))
                .endDate(DateHelper.toLocalDateTime(vikunjaTask.getEndDate()))
                .vikunjaId(Long.valueOf(vikunjaTask.getId()))
                .build();
        return taskRepository.save(task);
    }

    @Nullable
    @Override
    public Task update(ModelsTask vikunjaTask) {
        var task = taskRepository.findTaskByVikunjaId(vikunjaTask.getId());
        task = task.setTitle(vikunjaTask.getTitle())
                .setDescription(vikunjaTask.getDescription())
                .setIsDone(vikunjaTask.getDone())
                .setDoneAt(DateHelper.toLocalDateTime(vikunjaTask.getDoneAt()))
                .setDueAt(DateHelper.toLocalDateTime(vikunjaTask.getDueDate()))
                .setIsRepeat(false)
                .setPriority(vikunjaTask.getPriority())
                .setStartDate(DateHelper.toLocalDateTime(vikunjaTask.getStartDate()))
                .setEndDate(DateHelper.toLocalDateTime(vikunjaTask.getEndDate()));
        return taskRepository.save(task);
    }

    @Nonnull
    @Override
    public String getTask() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
        LOGGER.info("startOfWeek: {}", startOfWeek);

        List<Task> notDoneTaskList = taskRepository.findAllByIsDone(Boolean.FALSE);
        List<Task> completedTaskList = taskRepository.findAllByIsDoneAndDoneAtAfter(Boolean.TRUE, startOfWeek);
        LOGGER.debug("notDoneTaskList: {}", notDoneTaskList.stream().map(Task::getTitle).toList());

        return String.format("На дату %s не выполнено задач: %s\nВыполнено: %s", today, notDoneTaskList.size(), completedTaskList.size());
    }
}
