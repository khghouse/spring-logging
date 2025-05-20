package com.logging.enumeration;

import lombok.Getter;

@Getter
public enum ErrorCode {

    TASK_NOT_FOUND("할 일 정보가 존재하지 않습니다."),
    ALREADY_DELETED("이미 삭제되었습니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

}
