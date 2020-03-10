package com.java.alquiler.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class DateBeforeTodayException extends Exception {

	private static final long serialVersionUID = 1L;

	public DateBeforeTodayException(String message){
    	super(message);
    }

}
