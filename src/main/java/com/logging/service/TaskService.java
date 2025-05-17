package com.logging.service;

import com.logging.domain.Task;
import com.logging.repository.TaskRepository;
import com.logging.request.TaskServiceRequest;
import com.logging.response.TaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskResponse create(TaskServiceRequest request) {
        Task task = taskRepository.save(request.toEntity());
        return TaskResponse.of(task);
    }
    
}
