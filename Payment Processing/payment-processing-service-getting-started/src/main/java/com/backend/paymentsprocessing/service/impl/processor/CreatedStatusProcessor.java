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
public class CreatedStatusProcessor implements TxnStatusProcessor {

	private final TransactionDao transactionDao;
	private final ModelMapper modelMapper;

	@Override
	public TransactionDTO processStatus(TransactionDTO transactionDTO) {
		log.info("Processing CREATED Status:{}", transactionDTO.getTxnStatus());

		TransactionEntity txnEntity = modelMapper.map(transactionDTO, TransactionEntity.class);
		log.info("Mapped txnEntity:{}", txnEntity);

		int pkId = transactionDao.insertTranscation(txnEntity);
		log.info("Inserted transaction with pkId:{}", pkId);

		transactionDTO.setId(pkId);
		
		log.info("Final TransactionDto after insertion:{}", transactionDTO);
		return transactionDTO;
	}

}
