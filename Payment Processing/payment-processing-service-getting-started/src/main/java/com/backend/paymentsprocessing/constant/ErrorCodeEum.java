package com.backend.paymentsprocessing.constant;

import lombok.Getter;

@Getter
public enum ErrorCodeEum {

	GENERIC_ERROR("30000", "Unable to process the request at this time,Please try again later"), // empty message, can
	STRIPE_PROVIDER("30001", "Unable to connect to stripe"), // be filled later
	UNABLE_TO_INITIATE_PAYMENT("30002", "Error while Initiating Payment");

	private final String errorCode;
	private final String errorMessage;

	ErrorCodeEum(String errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}