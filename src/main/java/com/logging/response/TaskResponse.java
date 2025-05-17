package com.logging.response;

import com.logging.domain.Task;

public record TaskResponse(
        Long id,
        String title,
        String description
) {
    public static TaskResponse of(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription()
        );
    }
}
