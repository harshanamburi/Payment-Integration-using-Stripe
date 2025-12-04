package com.backend.paymentsprocessing.config;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.backend.paymentsprocessing.dto.TransactionDTO;
import com.backend.paymentsprocessing.entity.TransactionEntity;
import com.backend.paymentsprocessing.util.convert.idtoname.PaymentMethodEnumIdToNameConverter;
import com.backend.paymentsprocessing.util.convert.idtoname.PaymentTypeEnumIdToNameConverter;
import com.backend.paymentsprocessing.util.convert.idtoname.ProviderEnumIdToNameConverter;
import com.backend.paymentsprocessing.util.convert.idtoname.TransactionStatusEnumIdToNameConverter;
import com.backend.paymentsprocessing.util.convert.nametoid.PaymentMethodEnumConverter;
import com.backend.paymentsprocessing.util.convert.nametoid.PaymentTypeEnumConverter;
import com.backend.paymentsprocessing.util.convert.nametoid.ProviderEnumConverter;
import com.backend.paymentsprocessing.util.convert.nametoid.TransactionStatusEnumConverter;

@Configuration
public class AppConfig {

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();

		// Configure mapping strategy
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT).setSkipNullEnabled(true);

		// Converters: Name → ID
		Converter<String, Integer> paymentMethodEnumConverter = new PaymentMethodEnumConverter();
		Converter<String, Integer> paymentTypeEnumConverter = new PaymentTypeEnumConverter();
		Converter<String, Integer> providerEnumConverter = new ProviderEnumConverter();
		Converter<String, Integer> transactionStatusEnumConverter = new TransactionStatusEnumConverter();

		// Converters: ID → Name
		Converter<Integer, String> paymentMethodEnumIdToNameConverter = new PaymentMethodEnumIdToNameConverter();
		Converter<Integer, String> paymentTypeEnumIdToNameConverter = new PaymentTypeEnumIdToNameConverter();
		Converter<Integer, String> providerEnumIdToNameConverter = new ProviderEnumIdToNameConverter();
		Converter<Integer, String> transactionStatusEnumIdToNameConverter = new TransactionStatusEnumIdToNameConverter();

		// DTO → Entity mapping
		modelMapper.addMappings(new PropertyMap<TransactionDTO, TransactionEntity>() {
			@Override
			protected void configure() {
				using(paymentMethodEnumConverter).map(source.getPaymentMethod(), destination.getPaymentMethodId());
				using(transactionStatusEnumConverter).map(source.getTxnStatus(), destination.getTxnStatusId());
				using(providerEnumConverter).map(source.getProvider(), destination.getProviderId());
				using(paymentTypeEnumConverter).map(source.getPaymentType(), destination.getPaymentTypeId());
			}
		});

		// Entity → DTO mapping
		modelMapper.addMappings(new PropertyMap<TransactionEntity, TransactionDTO>() {
			@Override
			protected void configure() {
				using(paymentMethodEnumIdToNameConverter).map(source.getPaymentMethodId(),
						destination.getPaymentMethod());
				using(transactionStatusEnumIdToNameConverter).map(source.getTxnStatusId(), destination.getTxnStatus());
				using(providerEnumIdToNameConverter).map(source.getProviderId(), destination.getProvider());
				using(paymentTypeEnumIdToNameConverter).map(source.getPaymentTypeId(), destination.getPaymentType());
			}
		});

		return modelMapper;
	}
}
