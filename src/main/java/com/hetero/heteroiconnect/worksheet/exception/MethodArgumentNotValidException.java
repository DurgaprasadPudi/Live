package com.hetero.heteroiconnect.worksheet.exception;

public class MethodArgumentNotValidException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public MethodArgumentNotValidException(String message) {
		super(message);
	}

	public MethodArgumentNotValidException(String message, Throwable cause) {
		super(message, cause);
	}
}
