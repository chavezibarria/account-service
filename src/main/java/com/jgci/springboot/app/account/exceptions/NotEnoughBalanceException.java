package com.jgci.springboot.app.account.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotEnoughBalanceException extends ResponseStatusException {
	
	private static final long serialVersionUID = 779784581055673953L;

	public NotEnoughBalanceException(String message) {
		super(HttpStatus.CONFLICT, message);
	}

}
