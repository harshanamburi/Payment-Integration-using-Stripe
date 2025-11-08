package com.backend.paymentsprocessing.service.impl;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.backend.paymentsprocessing.constant.TransactionStatusEnum;
import com.backend.paymentsprocessing.dao.impl.TransactionDaoImpl;
import com.backend.paymentsprocessing.dto.TransactionDTO;
import com.backend.paymentsprocessing.entity.TransactionEntity;
import com.backend.paymentsprocessing.http.HttpRequest;
import com.backend.paymentsprocessing.http.HttpServiceEngine;
import com.backend.paymentsprocessing.pojo.CreateTxnRequest;
import com.backend.paymentsprocessing.pojo.InitiateTxnReq;
import com.backend.paymentsprocessing.pojo.TxnResponse;
import com.backend.paymentsprocessing.service.helper.StripeProviderCreatePaymentHelper;
import com.backend.paymentsprocessing.service.interfaces.PaymentService;
import com.backend.paymentsprocessing.service.interfaces.PaymentStatusService;
import com.backend.stripeprovider.StripeProviderPaymentResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	private final PaymentStatusService paymentStatusService;

	private final ModelMapper modelMapper;

	private final TransactionDaoImpl transactionDaoImpl;

	private final HttpServiceEngine httpServiceEngine;

	private final StripeProviderCreatePaymentHelper stripeProviderCreatePaymentHelper;

	@Override
	public TxnResponse createTxn(CreateTxnRequest createTxnRequest) {
		log.info("Created Txn from service layer | CreateTxnRequest:{}", createTxnRequest);

		TransactionDTO transactionDTO = modelMapper.map(createTxnRequest, TransactionDTO.class);

		log.info("Mapped DTO:{}", transactionDTO);

		String txnStatus = TransactionStatusEnum.CREATED.name();
		String txnReference = generateUniqueTxnRef(); // UUID

		transactionDTO.setTxnStatus(txnStatus);
		transactionDTO.setTxnReference(txnReference);

		log.info("Final TxnDto to be saved : {}", transactionDTO);
		TransactionDTO TransactionDTO = paymentStatusService.processStatus(transactionDTO);
		log.info("Response from PaymentStatusService: {}", transactionDTO);

		TxnResponse createTxnResponse = modelMapper.map(TransactionDTO, TxnResponse.class);
		log.info("Mapped to CreateTranscation Response:{}", createTxnResponse);

		return createTxnResponse;
	}

	private String generateUniqueTxnRef() {
		return UUID.randomUUID().toString();
	}

	@Override
	public TxnResponse initiateTxn(String txnReference, InitiateTxnReq initiateTxnReq) {
		log.info("Initiated Payment Transaction || txnReference:{}| InitiateTxnRequest:{}", txnReference,
				initiateTxnReq);

		TransactionEntity transactionEntity = transactionDaoImpl.getTransactionByTxnReference(txnReference);

		TransactionDTO txnDto = modelMapper.map(transactionEntity, TransactionDTO.class);

		log.info("Mapped TxnDto:{}", txnDto);

		txnDto.setTxnStatus(TransactionStatusEnum.INITIATED.name());
		log.info("Final Status Updated TxnDto:{}", txnDto);

		TransactionDTO txnDTOresponse = paymentStatusService.processStatus(txnDto);

		HttpRequest httpRequest = stripeProviderCreatePaymentHelper.prepareHttpRequest(initiateTxnReq);
		log.info("Prepared HttpRequest for Stripe-Provider-Service:{}", httpRequest);

		ResponseEntity<String> response = httpServiceEngine.makeHttpCall(httpRequest);

		StripeProviderPaymentResponse successResponse = stripeProviderCreatePaymentHelper.processResponse(response);
		log.info("Processed StripeProviderPaymentResponse:{}", successResponse);

		txnDto.setTxnStatus(TransactionStatusEnum.PENDING.name());
		txnDto.setProviderReference(successResponse.getId());
		txnDTOresponse = paymentStatusService.processStatus(txnDto);
		log.info("Updated TxnDto to Pending:{}", txnDTOresponse);

		TxnResponse txnResponse = new TxnResponse();
		txnResponse.setTxnReference(txnDto.getTxnReference());
		txnResponse.setTxnStatus(txnDto.getTxnReference());
		txnResponse.setRedirectUrl(successResponse.getUrl());

		log.info("Final TxnResponse to be returned:{}",txnResponse);
		return txnResponse;
	}

}
