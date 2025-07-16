package com.hetero.heteroiconnect.stationaryandhousekeepingtrack;

public class EmployeeDetailsFetchingException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public EmployeeDetailsFetchingException(String message) {
        super(message);
    }

    public EmployeeDetailsFetchingException(String message, Throwable cause) {
        super(message, cause);
    }
}
