package com.logging.controller;

import com.logging.request.TaskRequest;
import com.logging.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity create(@RequestBody TaskRequest request) {
        return ResponseEntity.ok(taskService.create(request.toCreateServiceRequest()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.get(id));
    }

}
