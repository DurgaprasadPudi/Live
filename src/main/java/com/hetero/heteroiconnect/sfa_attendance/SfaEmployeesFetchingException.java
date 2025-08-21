package com.hetero.heteroiconnect.sfa_attendance;

public class SfaEmployeesFetchingException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public SfaEmployeesFetchingException(String message) {
        super(message);
    }

    public SfaEmployeesFetchingException(String message, Throwable cause) {
        super(message, cause);
    }
}

