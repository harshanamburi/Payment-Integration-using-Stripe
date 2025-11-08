package com.backend.paymentsprocessing.util.convert.nametoid;

import org.modelmapper.AbstractConverter;

import com.backend.paymentsprocessing.constant.PaymentMethodEnum;

public class PaymentMethodEnumConverter extends AbstractConverter<String, Integer> {

	@Override
	protected Integer convert(String source) {
		return PaymentMethodEnum.fromName(source).getId();
	}
}
