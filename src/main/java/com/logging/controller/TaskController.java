package com.logging.controller;

import com.logging.request.TaskRequest;
import com.logging.response.TaskResponse;
import com.logging.service.TaskService;
import com.logging.validation.OnCreate;
import com.logging.validation.OnUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponse> create(@RequestBody @Validated(OnCreate.class) TaskRequest request) {
        return ResponseEntity.ok(taskService.create(request.toCreateServiceRequest()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> update(@PathVariable Long id, @RequestBody @Validated(OnUpdate.class) TaskRequest request) {
        return ResponseEntity.ok(taskService.update(id, request.toUpdateServiceRequest()));
    }

}
