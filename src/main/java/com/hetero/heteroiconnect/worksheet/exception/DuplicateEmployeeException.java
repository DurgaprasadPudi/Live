
package com.hetero.heteroiconnect.worksheet.exception;

public class DuplicateEmployeeException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DuplicateEmployeeException(String message) {
		super(message);
	}

	public DuplicateEmployeeException(String message, Throwable cause) {
		super(message, cause);
	}
}
