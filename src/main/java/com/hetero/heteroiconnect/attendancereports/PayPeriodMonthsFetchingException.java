package com.hetero.heteroiconnect.attendancereports;

public class PayPeriodMonthsFetchingException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public PayPeriodMonthsFetchingException(String message) {
        super(message);
    }

    public PayPeriodMonthsFetchingException(String message, Throwable cause) {
        super(message, cause);
    }
}
