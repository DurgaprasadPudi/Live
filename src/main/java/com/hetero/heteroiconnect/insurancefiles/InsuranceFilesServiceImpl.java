package com.hetero.heteroiconnect.insurancefiles;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import com.hetero.heteroiconnect.requisition.forms.ApiResponse;
import com.hetero.heteroiconnect.worksheet.exception.UserWorkSheetUploadException;
import com.hetero.heteroiconnect.worksheet.utility.MessageBundleSource;

@Service
public class InsuranceFilesServiceImpl implements InsuranceFilesService {

	private final InsuranceFilesRepository insuranceFilesRepository;
	private MessageBundleSource messageBundleSource;

	public InsuranceFilesServiceImpl(InsuranceFilesRepository insuranceFilesRepository,
			MessageBundleSource messageBundleSource) {
		this.insuranceFilesRepository = insuranceFilesRepository;
		this.messageBundleSource = messageBundleSource;
	}

	@Transactional(rollbackFor = Throwable.class)
	public ApiResponse uploadFiles(String type, List<MultipartFile> files) {
		try {
			return insuranceFilesRepository.uploadFiles(type, files);
		} catch (Exception e) {
			e.printStackTrace();
			throw new UserWorkSheetUploadException(
					messageBundleSource.getmessagebycode("insurance.file.error", new Object[] {}), e);

		}
	}

	@Transactional(readOnly = true)
	public List<InsuranceFileDTO> getAllInsuranceFiles(Integer type) {
		try {
			return insuranceFilesRepository.getAllInsuranceFiles(type);
		} catch (Exception e) {
			throw new UserWorkSheetUploadException(
					messageBundleSource.getmessagebycode("insurance.details.fetch.error", new Object[] {}), e);

		}
	}

	@Transactional(readOnly = true)
	public InsuranceFileDTO getEmployeeInsuranceDetails(Integer loginId) {
		try {
			return insuranceFilesRepository.getEmployeeInsuranceDetails(loginId);
		} catch (Exception e) {
			throw new UserWorkSheetUploadException(
					messageBundleSource.getmessagebycode("emp.insurance.details.fetch.error", new Object[] {}), e);

		}
	}

}
