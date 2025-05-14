package com.hetero.heteroiconnect;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hetero.heteroiconnect.attendancereports.AttendanceDataFetchingException;
import com.hetero.heteroiconnect.attendancereports.LocationsFetchingException;
import com.hetero.heteroiconnect.promotion.exception.EmployeeAlreadyFoundException;
import com.hetero.heteroiconnect.promotion.exception.EmployeeNotFoundException;
import com.hetero.heteroiconnect.promotion.exception.ErrorMessage;
import com.hetero.heteroiconnect.requisition.forms.UploadPositionException;
import com.hetero.heteroiconnect.worksheet.exception.DeleteTaskException;
import com.hetero.heteroiconnect.worksheet.exception.TaskApprovalException;
import com.hetero.heteroiconnect.worksheet.exception.TaskOverlapException;
import com.hetero.heteroiconnect.worksheet.exception.UserWorkSheetFetchingException;
import com.hetero.heteroiconnect.worksheet.exception.UserWorkSheetUpdateException;
import com.hetero.heteroiconnect.worksheet.exception.UserWorkSheetUploadException;
import com.hetero.heteroiconnect.worksheet.exceptionhandler.ErrorResponse;
import com.hetero.heteroiconnect.worksheet.exceptionhandler.ValidationErrorResponse;
import com.hetero.heteroiconnect.worksheet.report.Exception.ManagerAccessDeniedException;
import com.hetero.heteroiconnect.worksheet.report.Exception.NotFiledDataException;
import com.hetero.heteroiconnect.worksheet.report.entity.ReportErrorResponse;
 

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
	
	  

	    @ExceptionHandler(UploadPositionException.class)
		public ResponseEntity<ErrorResponse> handleException(UploadPositionException ex) {
			ErrorResponse errorResponse = new ErrorResponse("upload error", ex.getMessage());
			return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	    
	   //// Promotion letters
	    
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

	    //// worksheet
	    
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
		/// Work Sheet report
		
		 @ExceptionHandler(ManagerAccessDeniedException.class)
		    public ResponseEntity<ReportErrorResponse> handleManagerAccessDeniedException(ManagerAccessDeniedException ex) {
		        //ex.printStackTrace();
		        ReportErrorResponse errorResponse = new ReportErrorResponse("403", ex.getMessage());
		        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN); 
		    }

		    
			/*
			 * @ExceptionHandler(NotFiledDataException.class) public
			 * ResponseEntity<ReportErrorResponse>
			 * handleManagerAccessDeniedException(NotFiledDataException ex) {
			 * //ex.printStackTrace(); ReportErrorResponse errorResponse = new
			 * ReportErrorResponse("404", ex.getMessage()); return new
			 * ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND); }
			 */
		 @ExceptionHandler(NotFiledDataException.class)
		 public ResponseEntity<byte[]> handleNotFiledDataException(NotFiledDataException ex) {
		     String message = "No data found for the given parameters.";
		     return ResponseEntity
		             .status(HttpStatus.NOT_FOUND)
		             .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"error.txt\"")
		             .contentType(MediaType.TEXT_PLAIN)
		             .body(message.getBytes(StandardCharsets.UTF_8));
		 }


		    
}
