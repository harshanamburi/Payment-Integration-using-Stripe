package com.backend.paymentsprocessing.service;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.backend.paymentsprocessing.constant.TransactionStatusEnum;
import com.backend.paymentsprocessing.service.impl.processor.CreatedStatusProcessor;
import com.backend.paymentsprocessing.service.impl.processor.FailedStatusProcessor;
import com.backend.paymentsprocessing.service.impl.processor.InitiatedStatusProcessor;
import com.backend.paymentsprocessing.service.impl.processor.PendingStatusProcessor;
import com.backend.paymentsprocessing.service.impl.processor.SuccessStatusProcessor;

import com.backend.paymentsprocessing.service.interfaces.TxnStatusProcessor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentStatusFactory {
	private final ApplicationContext applicationContext;

	public TxnStatusProcessor getStatusProcessor(TransactionStatusEnum statusEnum) {
		log.info("Getting Status Processor for TxnStatusId: {}", statusEnum);
		switch (statusEnum) {
		case CREATED:
			return applicationContext.getBean(CreatedStatusProcessor.class);
		case INITIATED:
			return applicationContext.getBean(InitiatedStatusProcessor.class);
		case PENDING:
			return applicationContext.getBean(PendingStatusProcessor.class);
		case SUCCESS:
			return applicationContext.getBean(SuccessStatusProcessor.class);
		case FAILED:
			return applicationContext.getBean(FailedStatusProcessor.class);
		default:
			return null;
		}
	}
}
