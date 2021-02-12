package com.retail.exception;

public class ProductDetailsException extends Exception{

    public ProductDetailsException(String message) {
        super(message);
    }

    public ProductDetailsException(String message, Throwable cause) {
        super(message, cause);
    }
}
