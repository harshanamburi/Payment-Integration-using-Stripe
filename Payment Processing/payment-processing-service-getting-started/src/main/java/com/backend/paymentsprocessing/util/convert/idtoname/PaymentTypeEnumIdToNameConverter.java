package com.backend.paymentsprocessing.util.convert.idtoname;

import org.modelmapper.AbstractConverter;

import com.backend.paymentsprocessing.constant.PaymentTypeEnum;

public class PaymentTypeEnumIdToNameConverter extends AbstractConverter<Integer, String> {

	@Override
	protected String convert(Integer source) {
		return PaymentTypeEnum.fromId(source).getName();
	}
}
