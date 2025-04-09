package com.hetero.heteroiconnect.worksheet.exceptionhandler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.hetero.heteroiconnect.worksheet.exception.DeleteTaskException;
import com.hetero.heteroiconnect.worksheet.exception.TaskApprovalException;
import com.hetero.heteroiconnect.worksheet.exception.TaskOverlapException;
import com.hetero.heteroiconnect.worksheet.exception.UserWorkSheetFetchingException;
import com.hetero.heteroiconnect.worksheet.exception.UserWorkSheetUpdateException;
import com.hetero.heteroiconnect.worksheet.exception.UserWorkSheetUploadException;

 
@ControllerAdvice
public class CustomExceptionHandler {
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception ex) {
		ex.printStackTrace();
		ErrorResponse errorResponse = new ErrorResponse("INTERNAL_SERVER_ERROR", "Server Error");
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(UserWorkSheetUploadException.class)
	public ResponseEntity<ErrorResponse> handleException(UserWorkSheetUploadException ex) {
		ErrorResponse errorResponse = new ErrorResponse("WorkSheet Upload Error", ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(UserWorkSheetFetchingException.class)
	public ResponseEntity<ErrorResponse> handleException(UserWorkSheetFetchingException ex) {
		ErrorResponse errorResponse = new ErrorResponse("worksheet list fetching error", ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(TaskOverlapException.class)
	public ResponseEntity<Map<String, String>> handleTaskOverlapException(TaskOverlapException ex) {
		Map<String, String> response = new HashMap<>();
		response.put("message", ex.getMessage());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
		BindingResult bindingResult = ex.getBindingResult();
		Map<String, String> errors = new HashMap<>();
		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			errors.put(fieldError.getField(), fieldError.getDefaultMessage());
		}
		ValidationErrorResponse errorResponse = new ValidationErrorResponse("Validation Error", errors);
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(TaskApprovalException.class)
	public ResponseEntity<ErrorResponse> handleException(TaskApprovalException ex) {
		ErrorResponse errorResponse = new ErrorResponse("User Approval Error", ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(DeleteTaskException.class)
	public ResponseEntity<ErrorResponse> handleException(DeleteTaskException ex) {
		ErrorResponse errorResponse = new ErrorResponse("user task error", ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(UserWorkSheetUpdateException.class)
	public ResponseEntity<ErrorResponse> handleException(UserWorkSheetUpdateException ex) {
		ErrorResponse errorResponse = new ErrorResponse("WorkSheet Update Error", ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}