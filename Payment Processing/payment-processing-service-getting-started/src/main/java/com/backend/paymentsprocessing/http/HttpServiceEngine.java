package com.backend.paymentsprocessing.http;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import com.backend.paymentsprocessing.constant.ErrorCodeEum;
import com.backend.paymentsprocessing.exception.PaymentProcessingException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class HttpServiceEngine {

	private final RestClient restClient;

	public HttpServiceEngine(RestClient.Builder restClientBuilder) {
		this.restClient = restClientBuilder.build();
	}

	public ResponseEntity<String> makeHttpCall(HttpRequest httpRequest) {
		log.info("HttpCall from Http Serviceengine");
		try {

			ResponseEntity<String> httpResponse = restClient.method(httpRequest.getHttpMethod())
					.uri(httpRequest.getUrl()).headers((t) -> t.addAll(httpRequest.getHttpHeaders()))
					.body(httpRequest.getRequestBody()).retrieve().toEntity(String.class);

			log.info("Http call httpResponse:{}", httpResponse);
			return httpResponse;
		} catch (HttpClientErrorException | HttpServerErrorException e) {

			log.error("HTTP error response fom Stripe:{}", e.getMessage(), e);

			if (e.getStatusCode() == HttpStatus.GATEWAY_TIMEOUT
					|| e.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
				throw new PaymentProcessingException(ErrorCodeEum.STRIPE_PROVIDER.getErrorCode(),
						ErrorCodeEum.STRIPE_PROVIDER.getErrorMessage(), HttpStatus.SERVICE_UNAVAILABLE);
			}
			return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
		} catch (Exception e) {
			log.error("Exception occurred while making http call:{}", e.getMessage(), e);
			throw new PaymentProcessingException(ErrorCodeEum.STRIPE_PROVIDER.getErrorCode(),
					ErrorCodeEum.STRIPE_PROVIDER.getErrorMessage(), HttpStatus.SERVICE_UNAVAILABLE);
		}
	}
}
