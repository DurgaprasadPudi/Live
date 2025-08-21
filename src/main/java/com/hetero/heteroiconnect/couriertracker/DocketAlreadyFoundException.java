package com.hetero.heteroiconnect.couriertracker;
public class DocketAlreadyFoundException extends RuntimeException {
    public DocketAlreadyFoundException(String message) {
        super(message);
    }
}