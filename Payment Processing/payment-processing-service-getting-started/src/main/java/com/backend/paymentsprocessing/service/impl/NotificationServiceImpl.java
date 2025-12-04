package com.backend.paymentsprocessing.service.impl;


import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.backend.paymentsprocessing.constant.Constant;
import com.backend.paymentsprocessing.constant.NotificationType;
import com.backend.paymentsprocessing.constant.ProviderEnum;
import com.backend.paymentsprocessing.constant.TransactionStatusEnum;
import com.backend.paymentsprocessing.dao.interfaces.TransactionDao;
import com.backend.paymentsprocessing.dto.TransactionDTO;
import com.backend.paymentsprocessing.entity.TransactionEntity;
import com.backend.paymentsprocessing.pojo.NotificationRequest;
import com.backend.paymentsprocessing.service.interfaces.NotificationService;
import com.backend.paymentsprocessing.service.interfaces.PaymentStatusService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

	private final ModelMapper modelMapper;

	private final TransactionDao transactionDao;
	
	private final PaymentStatusService paymentStatusService;
	
	public void processNotification(NotificationRequest notificationRequest) {
		log.info("Processing payment notification "
				+ "||notificationRequest:{}", notificationRequest);

		ProviderEnum provider = ProviderEnum.fromName(notificationRequest.getProvider());
		
		TransactionEntity txnEntity = transactionDao.getTransactionByProviderReference(
				notificationRequest.getProviderReference(),
				provider.getId());
		TransactionDTO txnDto = modelMapper.map(txnEntity, TransactionDTO.class);
		log.info("Mapped txnDto: {}", txnDto);

		NotificationType type = NotificationType.fromName(
				notificationRequest.getNotificationType());
		log.info("Notification type identified: {}", type);

		switch (type) {
		case PAYMENT_SUCCESS:
			log.info("Handling PAYMENT_SUCCESS notification");
			
			processPaymentSuccess(notificationRequest, txnDto);
			break;
		case PAYMENT_FAILED:
			log.info("Handling PAYMENT_FAILURE notification");
			// Add logic to handle payment failure
			processPaymentFailed(notificationRequest, txnDto);
			break;
		default:
			log.warn("Unhandled notification type: {}", type);
			break;
		}
	}

	private void processPaymentSuccess(NotificationRequest notificationRequest, TransactionDTO txnDto) {
		txnDto.setTxnStatus(TransactionStatusEnum.SUCCESS.name());
		txnDto = paymentStatusService.processStatus(txnDto);
		log.info("Transaction updated to SUCCESS: {}", txnDto);
	}

	private void processPaymentFailed(NotificationRequest notificationRequest, 
			TransactionDTO txnDto) {
		txnDto.setTxnStatus(TransactionStatusEnum.FAILED.name());
		
		txnDto.setErrorCode(
				notificationRequest.getPayload().get(Constant.ERROR_CODE));
		
		txnDto.setErrorMessage(
				notificationRequest.getPayload().get(Constant.ERROR_MESSAGE));
		
		txnDto = paymentStatusService.processStatus(txnDto);
		log.info("Transaction updated to SUCCESS: {}", txnDto);
		
	}

}
