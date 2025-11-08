package com.backend.paymentsprocessing.pojo;

import lombok.Data;

@Data
public class TxnResponse {
    
	private String txnReference;
	private String txnStatus; 
	private String redirectUrl;
}
