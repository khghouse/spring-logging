package com.logging.request;

import com.logging.domain.Task;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskServiceRequest {

    private String title;

    private String description;

    private Boolean completed;

    @Builder(access = AccessLevel.PRIVATE)
    private TaskServiceRequest(String title, String description, Boolean completed) {
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    public Task toPersistEntity() {
        return Task.builder()
                .title(title)
                .description(description)
                .completed(false)
                .deleted(false)
                .build();
    }

    public static TaskServiceRequest withTitleAndDescription(String title, String description) {
        return TaskServiceRequest.builder()
                .title(title)
                .description(description)
                .build();
    }

    public static TaskServiceRequest of(String title, String description, Boolean completed) {
        return TaskServiceRequest.builder()
                .title(title)
                .description(description)
                .completed(completed)
                .build();
    }

}
