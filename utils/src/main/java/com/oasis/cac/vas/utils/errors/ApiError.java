package com.oasis.cac.vas.utils.errors;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Data
public class ApiError implements Serializable {

    private int statusCode;
    private HttpStatus status;
    private String message;
    private List<String> errors;
    private Object data;
    private boolean success;

    public ApiError(int statusCode, HttpStatus status, String message, boolean success, List<String> errors, Object data) {
        super();
        this.statusCode = statusCode;
        this.status = status;
        this.message = message;
        this.errors = errors;
        this.data = data;
        this.success = success;
    }

    public ApiError(int statusCode, HttpStatus status, String message, boolean success, String error, Object data) {
        super();
        this.statusCode = statusCode;
        this.status = status;
        this.message = message;
        errors = Arrays.asList(error);
        this.data = data;
        this.success = success;
    }
}
