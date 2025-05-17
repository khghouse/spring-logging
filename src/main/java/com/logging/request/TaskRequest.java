package com.logging.request;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskRequest {

    private Long id;

    private String title;

    private String description;

    public TaskServiceRequest toCreateServiceRequest() {
        return TaskServiceRequest.withTitleAndDescription(title, description);
    }

}
