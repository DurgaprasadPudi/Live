package com.hetero.heteroiconnect.stationaryandhousekeepingtrack;

public class StockNotAvailableException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public StockNotAvailableException(String message) {
        super(message);
    }

    public StockNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
