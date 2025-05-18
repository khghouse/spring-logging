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

    public TaskResponse get(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("할 일 정보가 존재하지 않습니다."));

        return TaskResponse.of(task);
    }

}
