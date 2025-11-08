package com.backend.paymentsprocessing.service.helper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.backend.paymentsprocessing.constant.ErrorCodeEum;
import com.backend.paymentsprocessing.exception.PaymentProcessingException;
import com.backend.paymentsprocessing.http.HttpRequest;
import com.backend.paymentsprocessing.pojo.InitiateTxnReq;
import com.backend.paymentsprocessing.util.JsonUtil;
import com.backend.stripeprovider.CreatePaymentRequest;
import com.backend.stripeprovider.StripeProviderPaymentResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class StripeProviderCreatePaymentHelper {

	@Value("${stripe-provider.create-payment.url}")
	private String createPaymentUrl;

	private final ModelMapper modelMapper;

	private final JsonUtil jsonUtil;

	public HttpRequest prepareHttpRequest(InitiateTxnReq initiateTxnReq) {

		log.info("Preparing HttpRequest for Stripe Provider CreatePayment");
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		CreatePaymentRequest req = modelMapper.map(initiateTxnReq, CreatePaymentRequest.class);

		String reqAsJson = jsonUtil.convertObjectToJson(req);
		log.info("Converted create Payment to JSON:{}", reqAsJson);

		if (reqAsJson == null) {
			throw new PaymentProcessingException(ErrorCodeEum.UNABLE_TO_INITIATE_PAYMENT.getErrorCode(),
					ErrorCodeEum.UNABLE_TO_INITIATE_PAYMENT.getErrorMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		HttpRequest httpRequest = HttpRequest.builder().httpMethod(HttpMethod.POST).url(createPaymentUrl)
				.httpHeaders(httpHeaders).requestBody(reqAsJson).build();

		log.info("Prpared HttpRequest:{}", httpRequest);
		return httpRequest;
	}

	public StripeProviderPaymentResponse processResponse(ResponseEntity<String> response) {
		log.info("Processing Response from Stripe Provider CreatePayment:{}", response);

		if (response.getStatusCode().is2xxSuccessful()) {
			StripeProviderPaymentResponse stripeProviderPaymentResponse = jsonUtil
					.convertJsonToObject(response.getBody(), StripeProviderPaymentResponse.class);
			log.info("Converted StripeProviderResponse:{}", stripeProviderPaymentResponse);

			if (stripeProviderPaymentResponse != null && stripeProviderPaymentResponse.getId() != null
					&& stripeProviderPaymentResponse.getUrl() != null) {
				log.info("StripeProviderPaymentResponse is valid");
				return stripeProviderPaymentResponse;
				
			}
		}
		
		throw new RuntimeException("not Implemented yet");

	}

}
