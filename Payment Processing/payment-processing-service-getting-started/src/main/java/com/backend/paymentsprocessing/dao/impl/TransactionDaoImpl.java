package com.backend.paymentsprocessing.dao.impl;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.backend.paymentsprocessing.dao.interfaces.TransactionDao;
import com.backend.paymentsprocessing.entity.TransactionEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
@RequiredArgsConstructor
public class TransactionDaoImpl implements TransactionDao {

	private final NamedParameterJdbcTemplate jdbcTemplate;

	private static final String INSERT_SQL = """
			    INSERT INTO payments.Transaction (
			        userId,
			        paymentMethodId,
			        providerId,
			        paymentTypeId,
			        txnStatusId,
			        amount,
			        currency,
			        merchantTransactionReference,
			        txnReference
			  )
			    VALUES (
			    :userId,
			    :paymentMethodId,
			    :providerId,
			    :paymentTypeId,
			    :txnStatusId,
			    :amount,
			    :currency,
			    :merchantTransactionReference,
			    :txnReference)
			""";

	@Override
	public Integer insertTranscation(TransactionEntity txn) {

		KeyHolder keyHolder = new GeneratedKeyHolder();
		BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(txn);
		jdbcTemplate.update(INSERT_SQL, params, keyHolder, new String[] { "id" });
		txn.setId(keyHolder.getKey().intValue());
		return txn.getId();
	}

	@Override
	public TransactionEntity getTransactionByTxnReference(String txnReference) {

		log.info("Fetching transaction with TxnReference:{}", txnReference);

		String sql = "SELECT * FROM transaction WHERE txnreference = :txnReference";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("txnReference", txnReference);

		TransactionEntity transactionEntity = jdbcTemplate.queryForObject(sql, params,
				new BeanPropertyRowMapper<>(TransactionEntity.class));
		log.info("Fetched transaction Entity:{}", transactionEntity);
		return transactionEntity;

	}

	@Override
	public Integer updateTransactionStatusDetailsByTxnReference(TransactionEntity txn) {
	    log.info("Updating transaction status and provider reference for txnReference: {}", txn.getTxnReference());

	    String sql = "UPDATE payments.Transaction " +
	                 "SET txnStatusId = :txnStatusId, providerReference = :providerReference " +
	                 "WHERE txnReference = :txnReference";

	    MapSqlParameterSource params = new MapSqlParameterSource();
	    params.addValue("txnStatusId", txn.getTxnStatusId());
	    params.addValue("providerReference", txn.getProviderReference());
	    params.addValue("txnReference", txn.getTxnReference());

	    log.info("Executing SQL: {} with params: {}", sql, params.getValues());

	    return  jdbcTemplate.update(sql, params);
	}
}
