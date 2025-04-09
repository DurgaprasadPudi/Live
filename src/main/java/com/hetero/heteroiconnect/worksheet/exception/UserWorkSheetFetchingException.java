package com.hetero.heteroiconnect.worksheet.exception;

public class UserWorkSheetFetchingException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UserWorkSheetFetchingException(String message) {
		super(message);
	}

	public UserWorkSheetFetchingException(String message, Throwable cause) {
		super(message, cause);
	}
}
