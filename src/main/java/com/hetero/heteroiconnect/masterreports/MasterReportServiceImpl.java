package com.hetero.heteroiconnect.masterreports;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hetero.heteroiconnect.hrassetrequests.Master;
import com.hetero.heteroiconnect.worksheet.exception.UserWorkSheetUploadException;
import com.hetero.heteroiconnect.worksheet.utility.MessageBundleSource;

@Service
public class MasterReportServiceImpl implements MasterReportService {

	private final MasterReportRepository masterReportRepository;
	private MessageBundleSource messageBundleSource;

	public MasterReportServiceImpl(MasterReportRepository masterReportRepository,
			MessageBundleSource messageBundleSource) {
		this.masterReportRepository = masterReportRepository;
		this.messageBundleSource = messageBundleSource;
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<EmployeeMasterDetailsDTO> getMasterDetails(MasterDetailsRequest request) {
		try {
			return masterReportRepository.getMasterDetails(request);
		} catch (Exception e) {
			e.printStackTrace();
			throw new UserWorkSheetUploadException(
					messageBundleSource.getmessagebycode("master.report.error", new Object[] {}), e);
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<Master> getStatusCodes() {
		return masterReportRepository.getStatusCodes();
	}

}
