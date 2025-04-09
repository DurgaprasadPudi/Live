package com.hetero.heteroiconnect.promotion.exception;
@SuppressWarnings("serial")
public class EmployeeAlreadyFoundException extends RuntimeException {
    public EmployeeAlreadyFoundException(String message) {
        super(message);
    }
}