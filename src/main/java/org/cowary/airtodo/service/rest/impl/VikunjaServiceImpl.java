package org.cowary.airtodo.service.rest.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cowary.airtodo.service.rest.VikunjaService;
import org.cowary.airtodo.utils.ApiExecutorHelper;
import org.cowary.vikunja.api.TaskApi;
import org.cowary.vikunja.model.ModelsTask;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;


@Service
@RequiredArgsConstructor
@Slf4j
public class VikunjaServiceImpl implements VikunjaService {

    private static final String SPRING_FILTER_ACTIVE = "due_date <= now/w+1w && done = false";
    private static final String SPRING_FILTER_COMPLETED = "due_date <= now/w+1w && done = true && due_date > now/w-4w";

    private final TaskApi taskApi;

    @Override
    public List<ModelsTask> getActiveSprintTasks() {
        var vikunjaResponse = taskApi.tasksAllGetWithHttpInfo(null, null, null, null, null, SPRING_FILTER_ACTIVE, null, null, null);
        LOGGER.trace("active spring tasks: {}", vikunjaResponse.getBody());
        return vikunjaResponse.getBody();
    }

    @Override
    public List<ModelsTask> getCompletedSprintTasks() {
        var response = taskApi.tasksAllGetWithHttpInfo(null, null, null, null, null, SPRING_FILTER_COMPLETED, null, null, null);
        LOGGER.trace("completed spring tasks: {}", response.getBody());
        return response.getBody();
    }

    @Override
    public List<ModelsTask> getLastWeekTasks() {
        var response = taskApi.tasksAllGetWithHttpInfo(null, null, null, null, null, SPRING_FILTER_COMPLETED, null, null, null);
        LOGGER.trace("last week spring tasks: {}", response.getBody());
        return response.getBody();
    }

    @Override
    public List<ModelsTask> getAllTasks() {
        var taskList = fetchAllPages();
        LOGGER.trace("task list: {}", taskList);
        return taskList;
    }

    public List<ModelsTask> fetchAllPages() {
        List<ModelsTask> allItems = new ArrayList<>();
        int currentPage = 1;
        int totalPages = 1;

        while (currentPage <= totalPages) {
            int finalCurrentPage = currentPage;
            Supplier<ResponseEntity<List<ModelsTask>>> requestAction = () -> taskApi.tasksAllGetWithHttpInfo(finalCurrentPage, null, null, null, null, null, null, null, null);
            ResponseEntity<List<ModelsTask>> response = ApiExecutorHelper.doRequest(requestAction, "taskEnity", null, 0);
            List<ModelsTask> pageData = Objects.requireNonNull(response).getBody();
            if (pageData != null) {
                allItems.addAll(pageData);
            }
            HttpHeaders headers = response.getHeaders();
            String totalPagesHeader = headers.getFirst("X-Pagination-Total-Pages");
            if (totalPagesHeader != null) {
                totalPages = Integer.parseInt(totalPagesHeader);
            }
            currentPage++;
        }

        return allItems;
    }
}


