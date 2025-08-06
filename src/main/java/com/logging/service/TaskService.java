package com.logging.service;

import com.logging.domain.Task;
import com.logging.exception.NotFoundException;
import com.logging.repository.TaskRepository;
import com.logging.dto.request.TaskServiceRequest;
import com.logging.dto.response.TaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.logging.enumeration.ErrorCode.TASK_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {

    private final TaskRepository taskRepository;

    @Transactional
    public TaskResponse create(TaskServiceRequest request) {
        Task task = taskRepository.save(request.toPersistEntity());
        return TaskResponse.of(task);
    }

    public TaskResponse get(Long id) {
        Task task = findValidTask(id);
        return TaskResponse.of(task);
    }

    @Transactional
    public TaskResponse update(Long id, TaskServiceRequest request) {
        Task task = findValidTask(id);
        task.update(request.getTitle(), request.getDescription(), request.getCompleted());
        return TaskResponse.of(task);
    }

    @Transactional
    public void delete(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(TASK_NOT_FOUND));
        task.delete();
    }

    private Task findValidTask(Long id) {
        return taskRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException(TASK_NOT_FOUND));
    }

}
