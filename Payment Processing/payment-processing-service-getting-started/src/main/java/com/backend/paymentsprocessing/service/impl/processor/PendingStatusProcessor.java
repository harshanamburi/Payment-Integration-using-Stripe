package com.backend.paymentsprocessing.service.impl.processor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.backend.paymentsprocessing.dao.interfaces.TransactionDao;
import com.backend.paymentsprocessing.dto.TransactionDTO;
import com.backend.paymentsprocessing.entity.TransactionEntity;
import com.backend.paymentsprocessing.service.interfaces.TxnStatusProcessor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PendingStatusProcessor implements TxnStatusProcessor {

	private final TransactionDao transactionDao;
	private final ModelMapper modelMapper;
	@Override
	public TransactionDTO processStatus(TransactionDTO transactionDTO) {
		log.info("Processing Pending Status || transaction:{}", transactionDTO);
		TransactionEntity transactionEntity = modelMapper.map(transactionDTO, TransactionEntity.class);
		log.info("Mapped to Transaction Entity:{}", transactionEntity);
		transactionDao.updateTransactionStatusDetailsByTxnReference(transactionEntity);
		log.info("Updated Transaction Status Successfully for Transaction Reference:{}",
				transactionDTO.getTxnReference());
		return transactionDTO;
	}

}
