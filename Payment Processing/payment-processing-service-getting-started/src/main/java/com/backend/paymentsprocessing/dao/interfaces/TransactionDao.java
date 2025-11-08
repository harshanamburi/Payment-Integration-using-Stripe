package com.backend.paymentsprocessing.dao.interfaces;

import com.backend.paymentsprocessing.entity.TransactionEntity;

public interface TransactionDao {

	public Integer insertTranscation(TransactionEntity txn);

	public TransactionEntity getTransactionByTxnReference(String txnReference);
	
	public Integer updateTransactionStatusDetailsByTxnReference(TransactionEntity txn);
}
