package com.hetero.heteroiconnect.attendancereports;

public class LocationsFetchingException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public LocationsFetchingException(String message) {
		super(message);
	}

	public LocationsFetchingException(String message, Throwable cause) {
		super(message, cause);
	}
}
