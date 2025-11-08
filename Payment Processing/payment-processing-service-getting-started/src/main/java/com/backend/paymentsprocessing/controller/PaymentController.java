package com.backend.paymentsprocessing.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.paymentsprocessing.constant.Constant;
import com.backend.paymentsprocessing.pojo.CreateTxnRequest;
import com.backend.paymentsprocessing.pojo.InitiateTxnReq;
import com.backend.paymentsprocessing.pojo.TxnResponse;
import com.backend.paymentsprocessing.service.impl.PaymentServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(Constant.V1_PAYMENTS)
@Slf4j
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentServiceImpl paymentServiceImpl;

	@PostMapping
	public TxnResponse createTxn(@RequestBody CreateTxnRequest createTxnRequest) {
		log.info("Created Payment Transcation|| createTxnRequest:{}", createTxnRequest);

		TxnResponse response = paymentServiceImpl.createTxn(createTxnRequest);

		log.info("Response from service Layer:{}", response);
		return response;
	}

	@PostMapping("{txnReference}/initiate")
	public TxnResponse initiateTxn(@PathVariable String txnReference, @RequestBody InitiateTxnReq initiateTxnReq) {
		log.info("Initiated Payment Transcation || id:{} | inititaeTxnReq:{}", txnReference, initiateTxnReq);
		TxnResponse response = paymentServiceImpl.initiateTxn(txnReference,initiateTxnReq);
		log.info("Response from service Layer:{}", response);
		return response;
	}
}
