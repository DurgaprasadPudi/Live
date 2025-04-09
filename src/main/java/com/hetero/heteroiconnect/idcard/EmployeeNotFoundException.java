package com.hetero.heteroiconnect.idcard;

public class EmployeeNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public EmployeeNotFoundException(String employeeId) {
		super("Employee not found with employeeId: " + employeeId);
	}
}
