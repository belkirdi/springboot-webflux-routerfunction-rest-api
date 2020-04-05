package com.ram.javacoderhint.routerfunction.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ResourceNotFoundException extends ResponseStatusException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(HttpStatus status, String message) {
        super(status, message);
    }

    public ResourceNotFoundException(HttpStatus status, String message, Throwable e) {
        super(status, message, e);
    }
}
