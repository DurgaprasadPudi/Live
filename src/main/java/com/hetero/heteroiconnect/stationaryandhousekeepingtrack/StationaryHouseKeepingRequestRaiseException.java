package com.hetero.heteroiconnect.stationaryandhousekeepingtrack;

public class StationaryHouseKeepingRequestRaiseException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public StationaryHouseKeepingRequestRaiseException(String message) {
        super(message);
    }

    public StationaryHouseKeepingRequestRaiseException(String message, Throwable cause) {
        super(message, cause);
    }
}
