package com.hetero.heteroiconnect.attendancereports;
 
public class ReaderDataFetchingException extends RuntimeException {
	private static final long serialVersionUID = 1L;
 
	public ReaderDataFetchingException(String message) {
        super(message);
    }
 
    public ReaderDataFetchingException(String message, Throwable cause) {
        super(message, cause);
    }
}