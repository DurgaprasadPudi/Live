package com.hetero.heteroiconnect.worksheet.exception;

public class UserWorkSheetUploadException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UserWorkSheetUploadException(String message) {
		super(message);
	}

	public UserWorkSheetUploadException(String message, Throwable cause) {
		super(message, cause);
	}
}
