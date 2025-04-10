package com.hetero.heteroiconnect.promotion.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomException {


    @ExceptionHandler(EmployeeNotFoundException.class)
	public ResponseEntity<ErrorMessage> handleException(EmployeeNotFoundException ex) {
		ErrorMessage errorResponse = new ErrorMessage("Employee Not Found", ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorMessage> handleRuntimeException(RuntimeException ex) {
        ErrorMessage errorResponse = new ErrorMessage("Error processing request", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(EmployeeAlreadyFoundException.class)
    public ResponseEntity<ErrorMessage> handleRuntimeException(EmployeeAlreadyFoundException ex) {
        ErrorMessage errorResponse = new ErrorMessage("Error processing request", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
