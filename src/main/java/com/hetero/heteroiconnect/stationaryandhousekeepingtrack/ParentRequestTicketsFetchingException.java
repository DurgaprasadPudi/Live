package com.hetero.heteroiconnect.stationaryandhousekeepingtrack;

public class ParentRequestTicketsFetchingException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ParentRequestTicketsFetchingException(String message) {
        super(message);
    }

    public ParentRequestTicketsFetchingException(String message, Throwable cause) {
        super(message, cause);
    }
}
