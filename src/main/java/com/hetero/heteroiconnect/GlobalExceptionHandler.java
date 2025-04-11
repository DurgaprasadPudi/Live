package com.hetero.heteroiconnect;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hetero.heteroiconnect.attendancereports.AttendanceDataFetchingException;
import com.hetero.heteroiconnect.attendancereports.LocationsFetchingException;
import com.hetero.heteroiconnect.worksheet.exceptionhandler.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    //https://jira.spring.io/browse/SPR-14651
    //4.3.5 supports RedirectAttributes redirectAttributes
    @ExceptionHandler(MultipartException.class)
    public String handleError1(MultipartException e, RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("message", e.getCause().getMessage());
        return "FAIL";

    }
    
    @ExceptionHandler(LocationsFetchingException.class)
	public ResponseEntity<ErrorResponse> handleException(LocationsFetchingException ex) {
		ErrorResponse errorResponse = new ErrorResponse("Attendance Locations Fetching Error", ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
	}
 
	@ExceptionHandler(AttendanceDataFetchingException.class)
	public ResponseEntity<ErrorResponse> handleException(AttendanceDataFetchingException ex) {
		ErrorResponse errorResponse = new ErrorResponse("Attendance Data Fetching Error", ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
	}

}
