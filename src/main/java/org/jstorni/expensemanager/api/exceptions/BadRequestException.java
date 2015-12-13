package org.jstorni.expensemanager.api.exceptions;

public class BadRequestException extends RuntimeException {
	private final static String PREFIX = "BAD_REQUEST :: ";

	public BadRequestException(String message) {
		super(PREFIX + message);
	}

	public BadRequestException(String message, Throwable cause) {
		super(PREFIX + message, cause);
	}

}
