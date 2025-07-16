package com.hetero.heteroiconnect.stationaryandhousekeepingtrack;

public class ParentRegistrationFetchingException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ParentRegistrationFetchingException(String message) {
        super(message);
    }

    public ParentRegistrationFetchingException(String message, Throwable cause) {
        super(message, cause);
    }
}
