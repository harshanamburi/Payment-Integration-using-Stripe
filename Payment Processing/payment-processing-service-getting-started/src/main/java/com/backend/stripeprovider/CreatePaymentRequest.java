package com.backend.stripeprovider;

import java.util.List;

import com.backend.paymentsprocessing.pojo.LineItem;

import lombok.Data;

@Data
public class CreatePaymentRequest {
	private String successUrl;
	private String cancelUrl;

	private List<LineItem> lineItems;
}
