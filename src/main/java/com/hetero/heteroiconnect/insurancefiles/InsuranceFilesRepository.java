package com.hetero.heteroiconnect.insurancefiles;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class InsuranceFilesRepository {
	private final JdbcTemplate jdbcTemplate;

	public InsuranceFilesRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
