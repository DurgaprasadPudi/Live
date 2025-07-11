package com.hetero.heteroiconnect.stationaryandhousekeepingtrack;

public class RequestItemsAlreadyUploadedException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public RequestItemsAlreadyUploadedException(String message) {
        super(message);
    }

    public RequestItemsAlreadyUploadedException(String message, Throwable cause) {
        super(message, cause);
    }
}
