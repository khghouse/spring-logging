package com.logging.dto.request;

import com.logging.validation.OnCreate;
import com.logging.validation.OnUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskRequest {

    @NotBlank(message = "할 일 제목을 입력해 주세요.", groups = {OnCreate.class, OnUpdate.class})
    private String title;

    @NotBlank(message = "할 일 내용을 입력해 주세요.", groups = {OnCreate.class, OnUpdate.class})
    private String description;

    @NotNull(message = "완료 여부는 필수입니다.", groups = OnUpdate.class)
    private Boolean completed;

    public TaskServiceRequest toCreateServiceRequest() {
        return TaskServiceRequest.withTitleAndDescription(title, description);
    }

    public TaskServiceRequest toUpdateServiceRequest() {
        return TaskServiceRequest.of(title, description, completed);
    }

}
