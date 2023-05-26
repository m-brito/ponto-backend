package io.github.mbrito.ponto.exceptions;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;

import jakarta.servlet.http.HttpServletRequest;

public class ApiException {
	private LocalDateTime timestamp;
    private Integer status;
    private String reason;
    private String message;
    private String path;
    private String exception;

    private ApiException(Integer status, Exception exception, HttpServletRequest request, String message) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.reason = HttpStatus.valueOf(status).getReasonPhrase();
        this.message = message;
        this.path = request.getServletPath();
        this.exception = exception.getClass().getSimpleName();
    }

    public ApiException(Integer status, Exception exception, HttpServletRequest request) {
        this(status, exception, request, exception.getLocalizedMessage());
    }

    public ApiException(Integer status, Exception exception, HttpServletRequest request, List<String> messages) {
        this(status, exception, request, String.join(", ", messages));
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }
}