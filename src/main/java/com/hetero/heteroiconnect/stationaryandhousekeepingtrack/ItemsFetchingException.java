package com.hetero.heteroiconnect.stationaryandhousekeepingtrack;

public class ItemsFetchingException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ItemsFetchingException(String message) {
        super(message);
    }

    public ItemsFetchingException(String message, Throwable cause) {
        super(message, cause);
    }
}
