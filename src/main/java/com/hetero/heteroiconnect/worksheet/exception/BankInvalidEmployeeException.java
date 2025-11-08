package com.hetero.heteroiconnect.worksheet.exception;

public class BankInvalidEmployeeException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public BankInvalidEmployeeException(String message) {
		super(message);
	}

	public BankInvalidEmployeeException(String message, Throwable cause) {
		super(message, cause);
	}
}