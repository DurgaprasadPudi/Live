package com.hetero.heteroiconnect.requisition.forms;

public class UploadPositionException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UploadPositionException(String message) {
		super(message);
	}

	public UploadPositionException(String message, Throwable cause) {
		super(message, cause);
	}
}
