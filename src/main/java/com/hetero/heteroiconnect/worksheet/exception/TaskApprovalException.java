package com.hetero.heteroiconnect.worksheet.exception;

public class TaskApprovalException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TaskApprovalException(String message) {
		super(message);
	}

	public TaskApprovalException(String message, Throwable cause) {
		super(message, cause);
	}
}