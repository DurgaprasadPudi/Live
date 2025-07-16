package com.hetero.heteroiconnect.stationaryandhousekeepingtrack;

public class InventoryFetchingException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InventoryFetchingException(String message) {
        super(message);
    }

    public InventoryFetchingException(String message, Throwable cause) {
        super(message, cause);
    }
}
