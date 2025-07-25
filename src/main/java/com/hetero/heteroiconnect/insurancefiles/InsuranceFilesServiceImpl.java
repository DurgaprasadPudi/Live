package com.hetero.heteroiconnect.insurancefiles;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.hetero.heteroiconnect.contractdetails.FileUtil;
import com.hetero.heteroiconnect.requisition.forms.ApiResponse;
import com.hetero.heteroiconnect.worksheet.exception.FuelAndDriverExpensesException;
import com.hetero.heteroiconnect.worksheet.exception.UserWorkSheetUploadException;
import com.hetero.heteroiconnect.worksheet.utility.MessageBundleSource;

@Service
public class InsuranceFilesServiceImpl implements InsuranceFilesService {

	private final InsuranceFilesRepository insuranceFilesRepository;
	private MessageBundleSource messageBundleSource;
	@Value("${insurance.usermanuals.path}")
	private String directoryPath;

	@Value("${company.forms.path}")
	private String formsPath;

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

	public Map<String, byte[]> getUserManuals() {
		Map<String, byte[]> fileContents = new HashMap<>();
		try {
			File folder = new File(directoryPath);
			File[] files = folder.listFiles();

			if (files != null) {
				for (File file : files) {
					if (file.isFile()) {
						Path filePath = file.toPath();
						byte[] content = Files.readAllBytes(filePath);
						fileContents.put(file.getName(), content);
					}
				}
			}
		} catch (Exception e) {
			throw new UserWorkSheetUploadException(
					messageBundleSource.getmessagebycode("emp.insurance.usermanual.error", new Object[] {}), e);
		}
		return fileContents;
	}

	@Transactional(readOnly = true)
	public Map<String, byte[]> getHrForms() {
		try (Stream<Path> paths = Files.walk(Paths.get(formsPath), 1)) {
			return paths.filter(Files::isRegularFile).collect(Collectors.toMap(path -> path.getFileName().toString(),
					path -> FileUtil.getFileContentAsBytes(path.toString())));
		} catch (Exception e) {
			throw new FuelAndDriverExpensesException(
					messageBundleSource.getmessagebycode("hr.registration.forms.error", new Object[] {}), e);
		}
	}

}
