package com.logging.dto;

import com.logging.enumeration.ErrorCode;
import lombok.Getter;

import static com.logging.enumeration.ErrorCode.BAD_REQUEST;

@Getter
public class ErrorBody {

    private final String code;
    private final String message;

    private ErrorBody(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ErrorBody fromErrorCode(ErrorCode errorCode) {
        return new ErrorBody(errorCode.name(), errorCode.getMessage());
    }

    public static ErrorBody badRequest(String message) {
        return new ErrorBody(BAD_REQUEST.name(), message);
    }

}
