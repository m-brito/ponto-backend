package io.github.mbrito.ponto.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.github.mbrito.ponto.exceptions.ApiException;
import io.github.mbrito.ponto.exceptions.ListIsEmptyException;
import io.github.mbrito.ponto.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class BaseController {
    private static final Logger LOGGER = LogManager.getLogger(BaseController.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiException> handleResourceNotFoundException(ResourceNotFoundException exception, HttpServletRequest request) {
        LOGGER.error(exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiException(404, exception, request));
    }
    
    @ExceptionHandler(ListIsEmptyException.class)
    public ResponseEntity<ApiException> handleListIsEmptyException(ListIsEmptyException exception, HttpServletRequest request) {
    	LOGGER.error(exception);
    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiException(500, exception, request));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiException> handleGenericException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        LOGGER.error(exception);
    	List<String> erros = exception.getBindingResult().getAllErrors()
				.stream()
				.map(erro -> erro.getDefaultMessage())
				.collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiException(400, exception, request, erros));
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiException> handleGenericException(Exception exception, HttpServletRequest request) {
    	LOGGER.error(exception);
    	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiException(500, exception, request));
    }
}