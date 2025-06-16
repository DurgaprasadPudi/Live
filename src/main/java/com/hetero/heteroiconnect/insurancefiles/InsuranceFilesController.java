package com.hetero.heteroiconnect.insurancefiles;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class InsuranceFilesController {

	private InsuranceFilesService insuranceFilesService;

	public InsuranceFilesController(InsuranceFilesService insuranceFilesService) {
		this.insuranceFilesService = insuranceFilesService;
	}

}
