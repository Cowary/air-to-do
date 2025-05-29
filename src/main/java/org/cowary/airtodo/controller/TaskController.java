package org.cowary.airtodo.controller;

import lombok.RequiredArgsConstructor;
import org.cowary.airtodo.service.db.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/task")
@RequiredArgsConstructor
public class TaskController {
    final TaskService taskService;

    @GetMapping("/get-task-status")
    public ResponseEntity<String> WeekResult() {
        return ResponseEntity.ok(taskService.getTask());
    }
}
