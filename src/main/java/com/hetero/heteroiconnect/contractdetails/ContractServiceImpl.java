package com.hetero.heteroiconnect.contractdetails;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.hetero.heteroiconnect.worksheet.exception.DuplicateEmployeeException;
import com.hetero.heteroiconnect.worksheet.exception.UserWorkSheetUploadException;
import com.hetero.heteroiconnect.worksheet.model.Master;
import com.hetero.heteroiconnect.worksheet.utility.MessageBundleSource;

@Service
public class ContractServiceImpl implements ContractService {
	private static final Logger logger = LoggerFactory.getLogger(ContractServiceImpl.class);

	private final JdbcTemplate jdbcTemplate;
	private final ContractRepository contractRepository;
	private final MessageBundleSource messageBundleSource;

	@Value("${contractdetails.folder-path}")
	private String folderPath;

	public ContractServiceImpl(JdbcTemplate jdbcTemplate, ContractRepository contractRepository,
			MessageBundleSource messageBundleSource) {
		this.jdbcTemplate = jdbcTemplate;
		this.contractRepository = contractRepository;
		this.messageBundleSource = messageBundleSource;
	}

	@Transactional(rollbackFor = Throwable.class)
	public String uploadContractEmployee(ContractPersonDetailsDTO dto, MultipartFile file) throws IOException {
		logger.info("Attempting to upload contract employee with ID: {}", dto.getEmployeeId());
		String checkSql = "SELECT COUNT(*) FROM test.tbl_contract_person_details WHERE employee_id = ? AND status = 1001";
		Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, dto.getEmployeeId());
		if (count != null && count > 0) {
			logger.warn("Duplicate employee found with ID: {}", dto.getEmployeeId());
			throw new DuplicateEmployeeException(
					messageBundleSource.getmessagebycode("contract.details.exists.error", null));
		}
		String aadharCheckSql = "SELECT COUNT(*) FROM test.tbl_contract_person_details WHERE aadhar_number = ? AND status = 1001";
		Integer aadharCount = jdbcTemplate.queryForObject(aadharCheckSql, Integer.class, dto.getAadharNumber());
		if (aadharCount != null && aadharCount > 0) {
			logger.warn("Duplicate Aadhar found with ID: {}", dto.getEmployeeId());
			throw new DuplicateEmployeeException(
					messageBundleSource.getmessagebycode("contract.details.aadhar.exists.error", null));
		}
		try {
			File dir = new File(folderPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			String fileName = dto.getEmployeeId() + "_" + file.getOriginalFilename();
			Path filePath = Paths.get(folderPath + File.separator + fileName);
			Files.write(filePath, file.getBytes());
			dto.setFile(fileName);
			logger.info("File saved: {}", filePath);
			contractRepository.uploadContractEmployee(dto);
			return "Employee uploaded successfully.";
		} catch (Exception e) {
			logger.error("Exception occurred while uploading employee ID: {}", dto.getEmployeeId(), e);
			throw new UserWorkSheetUploadException(
					messageBundleSource.getmessagebycode("contract.details.upload.error", new Object[] {}), e);
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<Master> getCompany() {
		return contractRepository.getCompany();
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<Master> getContracts() {
		return contractRepository.getContracts();
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<Master> getGender() {
		return contractRepository.getGender();
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<ContractDetailsDTO> getAllContractDetails() {
		try {
			return contractRepository.getAllContractDetails();
		} catch (Exception e) {
			throw new UserWorkSheetUploadException(
					messageBundleSource.getmessagebycode("contract.details.fetching.error", new Object[] {}), e);
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public String deleteEmployeeData(int id) {
		try {
			return contractRepository.deleteEmployeeData(id);
		} catch (Exception e) {
			throw new UserWorkSheetUploadException(
					messageBundleSource.getmessagebycode("contract.details.delete.error", new Object[] {}), e);
		}
	}
}
