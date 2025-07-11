package com.hetero.heteroiconnect.stationaryandhousekeepingtrack;

public class AssignItemsException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public AssignItemsException(String message) {
        super(message);
    }

    public AssignItemsException(String message, Throwable cause) {
        super(message, cause);
    }
}
