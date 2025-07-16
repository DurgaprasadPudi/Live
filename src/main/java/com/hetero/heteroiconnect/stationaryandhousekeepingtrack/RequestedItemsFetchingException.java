package com.hetero.heteroiconnect.stationaryandhousekeepingtrack;

public class RequestedItemsFetchingException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public RequestedItemsFetchingException(String message) {
		super(message);
	}

	public RequestedItemsFetchingException(String message, Throwable cause) {
		super(message, cause);
	}
}
