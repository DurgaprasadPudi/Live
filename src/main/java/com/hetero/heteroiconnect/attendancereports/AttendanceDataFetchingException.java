package com.hetero.heteroiconnect.attendancereports;

public class AttendanceDataFetchingException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public AttendanceDataFetchingException() {
	}

	public AttendanceDataFetchingException(String message) {
		super(message);
	}

	public AttendanceDataFetchingException(String message, Throwable cause) {
		super(message, cause);
	}
}
