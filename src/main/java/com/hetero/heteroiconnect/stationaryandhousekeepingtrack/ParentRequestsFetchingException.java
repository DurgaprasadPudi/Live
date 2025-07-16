package com.hetero.heteroiconnect.stationaryandhousekeepingtrack;

public class ParentRequestsFetchingException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ParentRequestsFetchingException(String message) {
        super(message);
    }

    public ParentRequestsFetchingException(String message, Throwable cause) {
        super(message, cause);
    }
}
