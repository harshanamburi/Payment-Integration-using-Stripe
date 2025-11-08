package com.backend.paymentsprocessing.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class PaymentProcessingException  extends RuntimeException{
	
	private static final long serialVersionUID = -6448364631716780585L;
	private final String errorCode;
	private final String errorMessage;
	private final HttpStatus httpStatus;

	public PaymentProcessingException (String errorCode, String errorMessage, HttpStatus httpStatus) {
		super(errorMessage);
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.httpStatus = httpStatus;
	}
}
