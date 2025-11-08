package com.backend.paymentsprocessing.service.interfaces;

import com.backend.paymentsprocessing.dto.TransactionDTO;

public interface TxnStatusProcessor {
	public TransactionDTO processStatus(TransactionDTO transactionDTO);
}
