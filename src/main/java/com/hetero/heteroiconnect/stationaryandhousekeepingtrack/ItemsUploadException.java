package com.hetero.heteroiconnect.stationaryandhousekeepingtrack;

public class ItemsUploadException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ItemsUploadException(String message) {
        super(message);
    }

    public ItemsUploadException(String message, Throwable cause) {
        super(message, cause);
    }
}
