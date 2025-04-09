package com.hetero.heteroiconnect.worksheet.exception;

public class UserWorkSheetUpdateException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UserWorkSheetUpdateException(String message) {
		super(message);
	}

	public UserWorkSheetUpdateException(String message, Throwable cause) {
		super(message, cause);
	}
}
