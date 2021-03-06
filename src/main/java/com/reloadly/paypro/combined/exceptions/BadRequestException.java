package com.reloadly.paypro.combined.exceptions;

public class BadRequestException extends RuntimeException{

    public BadRequestException(String message){
        super(message);
    }
}
