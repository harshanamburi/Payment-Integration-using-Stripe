package com.backend.paymentsprocessing.service.impl;

import org.springframework.stereotype.Service;

import com.backend.paymentsprocessing.constant.TransactionStatusEnum;
import com.backend.paymentsprocessing.dto.TransactionDTO;
import com.backend.paymentsprocessing.service.PaymentStatusFactory;
import com.backend.paymentsprocessing.service.interfaces.PaymentStatusService;
import com.backend.paymentsprocessing.service.interfaces.TxnStatusProcessor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentStatusServiceImpl implements PaymentStatusService {

	private final PaymentStatusFactory paymentStatusFactory;

	@Override
	public TransactionDTO processStatus(TransactionDTO transactionDTO) {

		log.info("Processing Status for transStatus Id: {}", transactionDTO);
		
		TransactionStatusEnum statusEnum=TransactionStatusEnum.fromName(transactionDTO.getTxnStatus());
		TxnStatusProcessor statusProcessor = paymentStatusFactory.getStatusProcessor(statusEnum);

		log.info("Retrieved status processor: {}", statusProcessor);
		if (statusProcessor == null) {
			throw new IllegalArgumentException("Invalid Status: " + transactionDTO.getTxnStatus());
		}

		TransactionDTO response = statusProcessor.processStatus(transactionDTO);
		log.info("Status processed with reponse:{}", response);
		return response;
	}

}
