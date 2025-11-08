package com.backend.paymentsprocessing.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.backend.paymentsprocessing.constant.ErrorCodeEum;
import com.backend.paymentsprocessing.pojo.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(PaymentProcessingException.class)
	public ResponseEntity<ErrorResponse> handlePaymentProcessingException(PaymentProcessingException ex) {

		log.error("Handling ProcessingExeption:{}", ex.getMessage(), ex);
		ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode(), ex.getMessage());

		log.info("Error Response: {}", errorResponse);
		return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception ex) {
		log.error("Handling Exception:{}", ex.getMessage(), ex);
		ErrorResponse errorResponse = new ErrorResponse(ErrorCodeEum.GENERIC_ERROR.getErrorCode(),
				ErrorCodeEum.GENERIC_ERROR.getErrorMessage());
		log.info("Error Response: {}", errorResponse);
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
