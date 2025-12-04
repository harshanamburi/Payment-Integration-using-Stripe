package com.backend.paymentsprocessing.pojo;

import lombok.Data;

@Data
public class CreateTxnResponse {
	
	private String txnReference;
	private String txnStatus;

}

