package com.hetero.heteroiconnect.sfa_attendance;

public class SfaAttendanceFetchingException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public SfaAttendanceFetchingException() {
	}

	public SfaAttendanceFetchingException(String message) {
		super(message);
	}

	public SfaAttendanceFetchingException(String message, Throwable cause) {
		super(message, cause);
	}
}