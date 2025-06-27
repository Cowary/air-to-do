package org.cowary.airtodo.service.db.impl;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.cowary.airtodo.entity.Priority;
import org.cowary.airtodo.entity.RepeatMod;
import org.cowary.airtodo.entity.RepeatedTask;
import org.cowary.airtodo.entity.Task;
import org.cowary.airtodo.repository.RepeatedTaskRepository;
import org.cowary.airtodo.repository.TaskRepository;
import org.cowary.airtodo.service.db.CoinService;
import org.cowary.airtodo.service.db.TaskService;
import org.cowary.airtodo.utils.DateHelper;
import org.cowary.vikunja.model.ModelsTask;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    TaskRepository taskRepository;
    RepeatedTaskRepository repeatedTaskRepository;
    CoinService coinService;

    @Override
    @Nullable
    @Transactional
    public Task create(ModelsTask vikunjaTask) {
        if (taskRepository.existsTaskByVikunjaId(vikunjaTask.getId())) {
            LOGGER.trace("Existed task found. New task with vikinjaTaskId-{} not created", vikunjaTask.getId());
            return null;
        }
        return createTask(vikunjaTask, null);
    }

    @Nullable
    @Transactional
    @Override
    public Task createRepatedTask(ModelsTask vikunjaTask) {
        if (taskRepository.existsTaskByVikunjaId(vikunjaTask.getId())) {
            LOGGER.debug("Existed task found. New repeated task with vikinjaTaskId-{} not created", vikunjaTask.getId());
            return null;
        }
        var repeatedTask = repeatedTaskRepository.findRepeatedTaskByVikunjaId(vikunjaTask.getId());
        if (ObjectUtils.isEmpty(repeatedTask)) {
            repeatedTask = RepeatedTask.builder()
                    .title(vikunjaTask.getTitle())
                    .description(vikunjaTask.getDescription())
                    .priority(vikunjaTask.getPriority())
                    .repeatMod(RepeatMod.DEFAULT)
                    .vikunjaId(vikunjaTask.getId())
                    .repeatAfter(vikunjaTask.getRepeatAfter())
                    .build();
            repeatedTaskRepository.save(repeatedTask);
            LOGGER.debug("Created new repeated task with id: {} and title: {}", repeatedTask.getId(), repeatedTask.getTitle());
        }

        return createTask(vikunjaTask, repeatedTask);
    }

    @Transactional
    public Task createTask(@Nonnull ModelsTask vikunjaTask, @Nullable RepeatedTask repeatedTask) {
        var task = Task.builder()
                .title(vikunjaTask.getTitle())
                .description(vikunjaTask.getDescription())
                .isDone(vikunjaTask.getDone())
                .doneAt(DateHelper.toLocalDateTime(vikunjaTask.getDoneAt()))
                .dueAt(DateHelper.toLocalDateTime(vikunjaTask.getDueDate()))
                .isRepeat(repeatedTask != null)
                .priority(vikunjaTask.getPriority())
                .startDate(DateHelper.toLocalDateTime(vikunjaTask.getStartDate()))
                .endDate(DateHelper.toLocalDateTime(vikunjaTask.getEndDate()))
                .vikunjaId(vikunjaTask.getId())
                .repeatedTask(repeatedTask)
                .build();
        taskRepository.save(task);
        LOGGER.debug("Created new task with id: {} ,title: {} and repeatedTaskId: {}", task.getId(), task.getTitle(), repeatedTask != null ? repeatedTask.getId() : null);
        return task;
    }

    @Nullable
    @Override
    @Transactional
    public Task update(ModelsTask vikunjaTask) {
        var task = taskRepository.findTaskByVikunjaId(vikunjaTask.getId());
        if (Boolean.TRUE.equals(vikunjaTask.getDone()) && Boolean.FALSE.equals(task.getIsDone())) {
            var priority = Priority.findByPriorityNumber(Objects.requireNonNull(vikunjaTask.getPriority()));
            coinService.addCoin(priority);
        }
        if (vikunjaTask.getRepeatAfter() > 0 && Boolean.FALSE.equals(vikunjaTask.getDone())) {
            task.setTitle(vikunjaTask.getTitle())
                .setDescription(vikunjaTask.getDescription())
                    .setIsDone(vikunjaTask.getDone())
                    .setDoneAt(DateHelper.toLocalDateTime(vikunjaTask.getDoneAt()))
                    .setDueAt(DateHelper.toLocalDateTime(vikunjaTask.getDueDate()))
                    .setIsRepeat(true)
                    .setPriority(vikunjaTask.getPriority())
                    .setStartDate(DateHelper.toLocalDateTime(vikunjaTask.getStartDate()))
                    .setEndDate(DateHelper.toLocalDateTime(vikunjaTask.getEndDate()));
        } else if (vikunjaTask.getRepeatAfter() > 0 && Boolean.TRUE.equals(vikunjaTask.getDone())) {
            task.setTitle(vikunjaTask.getTitle())
                    .setDescription(vikunjaTask.getDescription())
                    .setIsDone(vikunjaTask.getDone())
                    .setDoneAt(DateHelper.toLocalDateTime(vikunjaTask.getDoneAt()))
                    .setDueAt(DateHelper.toLocalDateTime(vikunjaTask.getDueDate()))
                    .setIsRepeat(true)
                    .setPriority(vikunjaTask.getPriority())
                    .setStartDate(DateHelper.toLocalDateTime(vikunjaTask.getStartDate()))
                    .setEndDate(DateHelper.toLocalDateTime(vikunjaTask.getEndDate()))
                    .setVikunjaId(null);
        } else {
            task = task.setTitle(vikunjaTask.getTitle())
                    .setDescription(vikunjaTask.getDescription())
                    .setIsDone(vikunjaTask.getDone())
                    .setDoneAt(DateHelper.toLocalDateTime(vikunjaTask.getDoneAt()))
                    .setDueAt(DateHelper.toLocalDateTime(vikunjaTask.getDueDate()))
                    .setPriority(vikunjaTask.getPriority())
                    .setStartDate(DateHelper.toLocalDateTime(vikunjaTask.getStartDate()))
                    .setEndDate(DateHelper.toLocalDateTime(vikunjaTask.getEndDate()));
        }
        LOGGER.trace("Was updated task with id: {}", task.getId());
        return task;
    }

    @Override
    public void delete(List<ModelsTask> vikunjaTaskList) {
        var taskList = taskRepository.findAllByIsDone(Boolean.FALSE)
                .stream()
                .filter(task -> ObjectUtils.isNotEmpty(task.getVikunjaId()))
                .toList();
        var vikunjaTaskIdList = vikunjaTaskList.stream().map(ModelsTask::getId).toList();
        var toDelete = taskList.stream()
                .filter(task -> ObjectUtils.isNotEmpty(task.getVikunjaId()))
                .filter(task -> !vikunjaTaskIdList.contains(task.getVikunjaId()))
                .toList();
        taskRepository.deleteAll(toDelete);
        LOGGER.debug("These tasks was deleted: {}", toDelete.stream().map(Task::getId).toList());
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

    @Nullable
    @Override
    public List<Task> getNotDoneThisWeekTasks() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
        LOGGER.info("startOfWeek: {}", startOfWeek);

        List<Task> notDoneTaskList = taskRepository.findAllByIsDone(Boolean.FALSE);
        LOGGER.debug("notDoneTaskList: {}", notDoneTaskList.stream().map(Task::getTitle).toList());
        return notDoneTaskList;
    }

    @Nullable
    @Override
    public List<Task> getIsDoneAndAfterDate(Boolean isDone, LocalDateTime afterDate) {
        return taskRepository.findAllByIsDoneAndDoneAtAfter(isDone, afterDate);
    }

}
