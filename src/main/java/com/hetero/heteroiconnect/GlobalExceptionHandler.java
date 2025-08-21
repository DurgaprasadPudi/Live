package com.hetero.heteroiconnect;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletionException;

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
import com.hetero.heteroiconnect.attendancereports.PayPeriodMonthsFetchingException;
import com.hetero.heteroiconnect.attendancereports.ReaderDataFetchingException;
import com.hetero.heteroiconnect.couriertracker.DocketAlreadyFoundException;
import com.hetero.heteroiconnect.couriertracker.DocketMissingException;
import com.hetero.heteroiconnect.promotion.exception.EmployeeAlreadyFoundException;
import com.hetero.heteroiconnect.promotion.exception.EmployeeNotFoundException;
import com.hetero.heteroiconnect.promotion.exception.ErrorMessage;
import com.hetero.heteroiconnect.requisition.forms.UploadPositionException;
import com.hetero.heteroiconnect.sfa_attendance.SfaAttendanceFetchingException;
import com.hetero.heteroiconnect.sfa_attendance.SfaEmployeesFetchingException;
import com.hetero.heteroiconnect.stationaryandhousekeepingtrack.AssignItemsException;
import com.hetero.heteroiconnect.stationaryandhousekeepingtrack.AssignedItemsHistoryFetchingException;
import com.hetero.heteroiconnect.stationaryandhousekeepingtrack.ChildRegistrationFetchingException;
import com.hetero.heteroiconnect.stationaryandhousekeepingtrack.ChildRequestsFetchingException;
import com.hetero.heteroiconnect.stationaryandhousekeepingtrack.IndividualUploadInventoryFetchingException;
import com.hetero.heteroiconnect.stationaryandhousekeepingtrack.InventoryFetchingException;
import com.hetero.heteroiconnect.stationaryandhousekeepingtrack.ItemsFetchingException;
import com.hetero.heteroiconnect.stationaryandhousekeepingtrack.ItemsUploadException;
import com.hetero.heteroiconnect.stationaryandhousekeepingtrack.ParentRegistrationFetchingException;
import com.hetero.heteroiconnect.stationaryandhousekeepingtrack.ParentRequestTicketsFetchingException;
import com.hetero.heteroiconnect.stationaryandhousekeepingtrack.ParentRequestsFetchingException;
import com.hetero.heteroiconnect.stationaryandhousekeepingtrack.RequestItemsAlreadyUploadedException;
import com.hetero.heteroiconnect.stationaryandhousekeepingtrack.RequestedItemsFetchingException;
import com.hetero.heteroiconnect.stationaryandhousekeepingtrack.StationaryHouseKeepingRequestRaiseException;
import com.hetero.heteroiconnect.stationaryandhousekeepingtrack.StockNotAvailableException;
import com.hetero.heteroiconnect.worksheet.exception.DeleteTaskException;
import com.hetero.heteroiconnect.worksheet.exception.DuplicateEmployeeException;
import com.hetero.heteroiconnect.worksheet.exception.FuelAndDriverExpensesException;
import com.hetero.heteroiconnect.worksheet.exception.InsuranceDetailsDateExpiredException;
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
import com.hetero.heteroiconnect.zonedetails.EmptyDataNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

	// https://jira.spring.io/browse/SPR-14651
	// 4.3.5 supports RedirectAttributes redirectAttributes
	@ExceptionHandler(MultipartException.class)
	public String handleError1(MultipartException e, RedirectAttributes redirectAttributes) {

		redirectAttributes.addFlashAttribute("message", e.getCause().getMessage());
		return "FAIL";
	}

	@ExceptionHandler(CompletionException.class)
	public ResponseEntity<ErrorResponse> handleCompletionException(CompletionException ex) {
		Throwable cause = ex.getCause();
		if (cause instanceof FuelAndDriverExpensesException) {
			return handleException((FuelAndDriverExpensesException) cause);
		}
		return new ResponseEntity<>(new ErrorResponse("Internal  Server Error", cause.getMessage()),
				HttpStatus.INTERNAL_SERVER_ERROR);
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

	@ExceptionHandler(DuplicateEmployeeException.class)
	public ResponseEntity<ErrorMessage> handleRuntimeException(DuplicateEmployeeException ex) {
		ErrorMessage errorResponse = new ErrorMessage("Error processing request", ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
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
		// ex.printStackTrace();
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
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"error.txt\"")
				.contentType(MediaType.TEXT_PLAIN).body(message.getBytes(StandardCharsets.UTF_8));
	}

	@ExceptionHandler(PayPeriodMonthsFetchingException.class)
	public ResponseEntity<ErrorResponse> handleException(PayPeriodMonthsFetchingException ex) {
		ErrorResponse errorResponse = new ErrorResponse("Pay Period Months Fetching Error", ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(FuelAndDriverExpensesException.class)
	public ResponseEntity<ErrorResponse> handleException(FuelAndDriverExpensesException ex) {
		ErrorResponse errorResponse = new ErrorResponse("Error", ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
	}
	
	@ExceptionHandler(InsuranceDetailsDateExpiredException.class)
	public ResponseEntity<ErrorResponse> handleException(InsuranceDetailsDateExpiredException ex) {
		ErrorResponse errorResponse = new ErrorResponse("Error", ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(EmptyDataNotFoundException.class)
	public ResponseEntity<String> handleEmptyDataException(EmptyDataNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No tour data found for the selected period.");
	}

	/////

	@ExceptionHandler(ItemsFetchingException.class)

	public ResponseEntity<ErrorResponse> handleException(ItemsFetchingException ex) {

		ErrorResponse errorResponse = new ErrorResponse("Stationary and House keeping items fetching error",

				ex.getMessage());

		return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);

	}

	@ExceptionHandler(StationaryHouseKeepingRequestRaiseException.class)

	public ResponseEntity<ErrorResponse> handleException(StationaryHouseKeepingRequestRaiseException ex) {

		ErrorResponse errorResponse = new ErrorResponse("Stationary and House keeping items request raising error",

				ex.getMessage());

		return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);

	}

	@ExceptionHandler(ParentRequestsFetchingException.class)

	public ResponseEntity<ErrorResponse> handleException(ParentRequestsFetchingException ex) {

		ErrorResponse errorResponse = new ErrorResponse("Parent requests fetching error", ex.getMessage());

		return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);

	}

	@ExceptionHandler(ChildRequestsFetchingException.class)

	public ResponseEntity<ErrorResponse> handleException(ChildRequestsFetchingException ex) {

		ErrorResponse errorResponse = new ErrorResponse("Child items fetching error", ex.getMessage());

		return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);

	}

	@ExceptionHandler(ParentRequestTicketsFetchingException.class)

	public ResponseEntity<ErrorResponse> handleException(ParentRequestTicketsFetchingException ex) {

		ErrorResponse errorResponse = new ErrorResponse("Parent request tickets fetching error", ex.getMessage());

		return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);

	}

	@ExceptionHandler(ItemsUploadException.class)

	public ResponseEntity<ErrorResponse> handleException(ItemsUploadException ex) {

		ErrorResponse errorResponse = new ErrorResponse("Items uploading error", ex.getMessage());

		return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);

	}

	@ExceptionHandler(RequestItemsAlreadyUploadedException.class)

	public ResponseEntity<ErrorResponse> handleException(RequestItemsAlreadyUploadedException ex) {

		ErrorResponse errorResponse = new ErrorResponse("Items already uploaded", ex.getMessage());

		return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);

	}

	@ExceptionHandler(ParentRegistrationFetchingException.class)

	public ResponseEntity<ErrorResponse> handleException(ParentRegistrationFetchingException ex) {

		ErrorResponse errorResponse = new ErrorResponse("Parent registrations fetching error", ex.getMessage());

		return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);

	}

	@ExceptionHandler(ChildRegistrationFetchingException.class)

	public ResponseEntity<ErrorResponse> handleException(ChildRegistrationFetchingException ex) {

		ErrorResponse errorResponse = new ErrorResponse("Child items fetching error", ex.getMessage());

		return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);

	}

	@ExceptionHandler(InventoryFetchingException.class)

	public ResponseEntity<ErrorResponse> handleException(InventoryFetchingException ex) {

		ErrorResponse errorResponse = new ErrorResponse("Inventory fetching error", ex.getMessage());

		return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);

	}

	@ExceptionHandler(IndividualUploadInventoryFetchingException.class)

	public ResponseEntity<ErrorResponse> handleException(IndividualUploadInventoryFetchingException ex) {

		ErrorResponse errorResponse = new ErrorResponse("Inventory fetching error", ex.getMessage());

		return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);

	}

	@ExceptionHandler(RequestedItemsFetchingException.class)

	public ResponseEntity<ErrorResponse> handleException(RequestedItemsFetchingException ex) {

		ErrorResponse errorResponse = new ErrorResponse("Requested items fetching error", ex.getMessage());

		return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);

	}

	@ExceptionHandler(AssignItemsException.class)

	public ResponseEntity<ErrorResponse> handleException(AssignItemsException ex) {

		ErrorResponse errorResponse = new ErrorResponse("Items assigning error", ex.getMessage());

		return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);

	}

	@ExceptionHandler(StockNotAvailableException.class)

	public ResponseEntity<ErrorResponse> handleException(StockNotAvailableException ex) {

		ErrorResponse errorResponse = new ErrorResponse("Stock not available", ex.getMessage());

		return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);

	}

	@ExceptionHandler(AssignedItemsHistoryFetchingException.class)

	public ResponseEntity<ErrorResponse> handleException(AssignedItemsHistoryFetchingException ex) {

		ErrorResponse errorResponse = new ErrorResponse("Assigned assets history fetching error", ex.getMessage());

		return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);

	}

	@ExceptionHandler(SfaEmployeesFetchingException.class)
	public ResponseEntity<ErrorResponse> handleException(SfaEmployeesFetchingException ex) {
		ErrorResponse errorResponse = new ErrorResponse("Sfa employees fetching error", ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(SfaAttendanceFetchingException.class)
	public ResponseEntity<ErrorResponse> handleException(SfaAttendanceFetchingException ex) {
		ErrorResponse errorResponse = new ErrorResponse("Sfa attendance fetching error", ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(ReaderDataFetchingException.class)
	public ResponseEntity<ErrorResponse> handleException(ReaderDataFetchingException ex) {
		ErrorResponse errorResponse = new ErrorResponse("Reader Data Fetching Error", ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
	}
	
	@ExceptionHandler(DocketAlreadyFoundException.class)
	public ResponseEntity<ErrorResponse> handleDocketExists(DocketAlreadyFoundException ex) {
	    return new ResponseEntity<>(
	        new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), ex.getMessage()), 
	        HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DocketMissingException.class)
	public ResponseEntity<ErrorResponse> handleDocketMissing(DocketMissingException ex) {
	    return new ResponseEntity<>(
	        new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), ex.getMessage()), 
	        HttpStatus.BAD_REQUEST);
	}

	
	 

}
