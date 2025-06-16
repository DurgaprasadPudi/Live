package com.hetero.heteroiconnect.insurancefiles;

import org.springframework.stereotype.Service;

@Service
public class InsuranceFilesServiceImpl implements InsuranceFilesService {
	private final InsuranceFilesRepository insuranceFilesRepository;

	public InsuranceFilesServiceImpl(InsuranceFilesRepository insuranceFilesRepository) {
		this.insuranceFilesRepository = insuranceFilesRepository;
	}
}
