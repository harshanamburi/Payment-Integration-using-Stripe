package com.backend.paymentsprocessing.util.convert.nametoid;

import org.modelmapper.AbstractConverter;

import com.backend.paymentsprocessing.constant.TransactionStatusEnum;

public class TransactionStatusEnumConverter extends AbstractConverter<String, Integer> {

	@Override
	protected Integer convert(String source) {
		return TransactionStatusEnum.fromName(source).getId();
	}
}
