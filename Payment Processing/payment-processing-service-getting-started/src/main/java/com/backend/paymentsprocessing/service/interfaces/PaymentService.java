package com.backend.paymentsprocessing.service.interfaces;

import com.backend.paymentsprocessing.pojo.CreateTxnRequest;
import com.backend.paymentsprocessing.pojo.InitiateTxnReq;
import com.backend.paymentsprocessing.pojo.TxnResponse;

public interface PaymentService {
	public TxnResponse createTxn(CreateTxnRequest createTxnRequest);

	public TxnResponse initiateTxn(String id,InitiateTxnReq initiateTxnReq);
}
