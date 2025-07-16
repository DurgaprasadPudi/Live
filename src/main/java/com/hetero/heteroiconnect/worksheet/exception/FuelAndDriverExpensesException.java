package com.hetero.heteroiconnect.worksheet.exception;

public class FuelAndDriverExpensesException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public FuelAndDriverExpensesException(String message) {
		super(message);
	}

	public FuelAndDriverExpensesException(String message, Throwable cause) {
		super(message, cause);
	}
}