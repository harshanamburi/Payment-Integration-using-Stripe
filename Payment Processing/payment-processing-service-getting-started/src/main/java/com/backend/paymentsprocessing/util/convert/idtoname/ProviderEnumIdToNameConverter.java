package com.backend.paymentsprocessing.util.convert.idtoname;

import org.modelmapper.AbstractConverter;

import com.backend.paymentsprocessing.constant.ProviderEnum;

public class ProviderEnumIdToNameConverter extends AbstractConverter<Integer, String> {

	@Override
	protected String convert(Integer source) {
		return ProviderEnum.fromId(source).getName();
	}
}
