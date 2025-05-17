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

    @Builder(access = AccessLevel.PRIVATE)
    private TaskServiceRequest(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Task toEntity() {
        return Task.builder()
                .title(title)
                .description(description)
                .completed(false)
                .build();
    }

    public static TaskServiceRequest withTitleAndDescription(String title, String description) {
        return TaskServiceRequest.builder()
                .title(title)
                .description(description)
                .build();
    }

}
