package com.hetero.heteroiconnect.masterreports;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MasterReportServiceImpl implements MasterReportService {

	private final MasterReportRepository masterReportRepository;

	public MasterReportServiceImpl(MasterReportRepository masterReportRepository) {
		this.masterReportRepository = masterReportRepository;
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<EmployeeMasterDetailsDTO> getMasterDetails(String buName) {
		try {
			return masterReportRepository.getMasterDetails(buName);
		} catch (Exception e) {

			throw new RuntimeException("Error loading master details", e);
		}
	}

}
