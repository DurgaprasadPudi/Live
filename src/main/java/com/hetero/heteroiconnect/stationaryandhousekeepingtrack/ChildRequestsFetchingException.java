package com.hetero.heteroiconnect.stationaryandhousekeepingtrack;

public class ChildRequestsFetchingException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ChildRequestsFetchingException(String message) {
        super(message);
    }

    public ChildRequestsFetchingException(String message, Throwable cause) {
        super(message, cause);
    }
}
