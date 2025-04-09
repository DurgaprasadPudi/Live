package com.hetero.heteroiconnect.worksheet.exception;

public class DeleteTaskException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DeleteTaskException(String message) {
		super(message);
	}

	public DeleteTaskException(String message, Throwable cause) {
		super(message, cause);
	}
}
