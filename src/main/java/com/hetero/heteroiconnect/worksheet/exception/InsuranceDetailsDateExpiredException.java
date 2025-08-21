package com.hetero.heteroiconnect.worksheet.exception;

public class InsuranceDetailsDateExpiredException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InsuranceDetailsDateExpiredException(String message) {
		super(message);
	}

	public InsuranceDetailsDateExpiredException(String message, Throwable cause) {
		super(message, cause);
	}
}
