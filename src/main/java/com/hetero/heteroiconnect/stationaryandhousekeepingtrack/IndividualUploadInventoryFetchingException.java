package com.hetero.heteroiconnect.stationaryandhousekeepingtrack;

public class IndividualUploadInventoryFetchingException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public IndividualUploadInventoryFetchingException(String message) {
        super(message);
    }

    public IndividualUploadInventoryFetchingException(String message, Throwable cause) {
        super(message, cause);
    }
}
