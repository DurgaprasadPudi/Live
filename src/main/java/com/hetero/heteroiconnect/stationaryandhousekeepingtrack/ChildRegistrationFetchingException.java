package com.hetero.heteroiconnect.stationaryandhousekeepingtrack;

public class ChildRegistrationFetchingException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ChildRegistrationFetchingException(String message) {
        super(message);
    }

    public ChildRegistrationFetchingException(String message, Throwable cause) {
        super(message, cause);
    }
}
