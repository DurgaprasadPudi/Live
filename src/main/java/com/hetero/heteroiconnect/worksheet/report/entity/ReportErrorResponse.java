package com.hetero.heteroiconnect.worksheet.report.entity;

public class ReportErrorResponse {
	private String status;
	private String message;

	public ReportErrorResponse(String status, String message) {
		this.status=status;
		this.message=message;
	}
	
	public ReportErrorResponse() {
		// TODO Auto-generated constructor stub
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
