package com.backend.paymentsprocessing.pojo;

import java.util.List;

import lombok.Data;

@Data
public class InitiateTxnReq {

	private String successUrl;
	private String cancelUrl;

	private List<LineItem> lineItems;
}
