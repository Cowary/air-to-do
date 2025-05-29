package org.cowary.airtodo.controller;

import lombok.RequiredArgsConstructor;
import org.cowary.airtodo.service.rest.VikunjaService;
import org.cowary.vikunja.model.ModelsTask;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final VikunjaService vikunjaService;

    @GetMapping("/test")
    public List<ModelsTask> test() {
        return vikunjaService.getAllTasks();
    }
}
