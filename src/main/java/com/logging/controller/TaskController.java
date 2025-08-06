package com.logging.controller;

import com.logging.dto.request.TaskRequest;
import com.logging.dto.ApiResponse;
import com.logging.dto.response.TaskResponse;
import com.logging.service.TaskService;
import com.logging.validation.OnCreate;
import com.logging.validation.OnUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ApiResponse<TaskResponse> create(@RequestBody @Validated(OnCreate.class) TaskRequest request) {
        return ApiResponse.ok(taskService.create(request.toCreateServiceRequest()));
    }

    @GetMapping("/{id}")
    public ApiResponse<TaskResponse> get(@PathVariable Long id) {
        return ApiResponse.ok(taskService.get(id));
    }

    @PutMapping("/{id}")
    public ApiResponse<TaskResponse> update(@PathVariable Long id, @RequestBody @Validated(OnUpdate.class) TaskRequest request) {
        return ApiResponse.ok(taskService.update(id, request.toUpdateServiceRequest()));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        taskService.delete(id);
        return ApiResponse.ok();
    }

}
