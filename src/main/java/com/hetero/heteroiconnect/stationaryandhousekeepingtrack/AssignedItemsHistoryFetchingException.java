package com.hetero.heteroiconnect.stationaryandhousekeepingtrack;

public class AssignedItemsHistoryFetchingException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public AssignedItemsHistoryFetchingException(String message) {
        super(message);
    }

    public AssignedItemsHistoryFetchingException(String message, Throwable cause) {
        super(message, cause);
    }
}
