package com.hetero.heteroiconnect.insurancefiles;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.hetero.heteroiconnect.requisition.forms.ApiResponse;
import com.hetero.heteroiconnect.worksheet.exception.FuelAndDriverExpensesException;
import com.hetero.heteroiconnect.worksheet.exception.InsuranceDetailsDateExpiredException;
import com.hetero.heteroiconnect.worksheet.exception.UserWorkSheetUploadException;
import com.hetero.heteroiconnect.worksheet.model.Master;
import com.hetero.heteroiconnect.worksheet.utility.MessageBundleSource;

@Service
public class InsuranceFilesServiceImpl implements InsuranceFilesService {
	private static final Logger logger = LoggerFactory.getLogger(InsuranceFilesServiceImpl.class);

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
			logger.error("Error while uploading insurance files of type: {}", type, e);
			throw new UserWorkSheetUploadException(
					messageBundleSource.getmessagebycode("insurance.file.error", new Object[] {}), e);

		}
	}

	@Transactional(readOnly = true)
	public List<InsuranceFileDTO> getAllInsuranceFiles(Integer type) {
		try {
			return insuranceFilesRepository.getAllInsuranceFiles(type);
		} catch (Exception e) {
			logger.error("Error fetching insurance files for type: {}", type, e);
			throw new UserWorkSheetUploadException(
					messageBundleSource.getmessagebycode("insurance.details.fetch.error", new Object[] {}), e);

		}
	}

	@Transactional(readOnly = true)
	public InsuranceFileDTO getEmployeeInsuranceDetails(Integer loginId) {
		try {
			return insuranceFilesRepository.getEmployeeInsuranceDetails(loginId);
		} catch (Exception e) {
			logger.error("Error fetching insurance details for loginId: {}", loginId, e);
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
			logger.error("Error fetching user manuals from: {}", directoryPath, e);
			throw new UserWorkSheetUploadException(
					messageBundleSource.getmessagebycode("emp.insurance.usermanual.error", new Object[] {}), e);
		}
		return fileContents;
	}

	/*
	 * @Transactional(readOnly = true) public Map<String, byte[]> getHrForms() { try
	 * (Stream<Path> paths = Files.walk(Paths.get(formsPath), 1)) { return
	 * paths.filter(Files::isRegularFile).collect(Collectors.toMap(path ->
	 * path.getFileName().toString(), path ->
	 * FileUtil.getFileContentAsBytes(path.toString()))); } catch (Exception e) {
	 * throw new FuelAndDriverExpensesException(
	 * messageBundleSource.getmessagebycode("hr.registration.forms.error", new
	 * Object[] {}), e); } }
	 */

	@Transactional(readOnly = true)
	public List<HrFormDTO> getHrForms() {
		try {
			return insuranceFilesRepository.getHrForms();
		} catch (Exception e) {
			logger.error("Error fetching HR forms", e);
			throw new FuelAndDriverExpensesException(
					messageBundleSource.getmessagebycode("hr.registration.forms.error", new Object[] {}), e);
		}
	}

	/*
	 * @Transactional(rollbackFor = Throwable.class) public Boolean getDates() { try
	 * { return insuranceFilesRepository.getDates(); } catch (Exception e) { throw
	 * new FuelAndDriverExpensesException(
	 * messageBundleSource.getmessagebycode("family.insurance.enable.dates.error",
	 * new Object[] {}), e); } }
	 */

	@Transactional(rollbackFor = Throwable.class)
	public EmployeeBasicDetailsDTO getEmployeeDetails(int empId) {
		try {
			return insuranceFilesRepository.getEmployeeDetails(empId);
		} catch (Exception e) {
			logger.error("Error fetching employee details for empId: {}", empId, e);
			throw new FuelAndDriverExpensesException(messageBundleSource
					.getmessagebycode("employee.family.member.insurance.details.error", new Object[] {}), e);
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<Master> getRelation() {
		return insuranceFilesRepository.getRelation();
	}

	@Transactional(rollbackFor = Throwable.class)
	public ApiResponse deleteFamilyMember(int familyMemberId, int empId) {
		try {
			boolean flag = insuranceFilesRepository.getFlag(insuranceFilesRepository.getDOJ(empId));
			if (!flag) {
				logger.warn("Delete Family Member ,Date expired for employeeId: {}", empId);
				throw new InsuranceDetailsDateExpiredException(
						messageBundleSource.getmessagebycode("family.data.enable.date.error", new Object[] {}));
			}
			return insuranceFilesRepository.deleteFamilyMember(familyMemberId);
		} catch (InsuranceDetailsDateExpiredException ex) {
			throw ex;
		} catch (Exception e) {
			logger.error("Error deleting family member with ID: {}", familyMemberId, e);
			throw new FuelAndDriverExpensesException(
					messageBundleSource.getmessagebycode("family.member.delete.error", new Object[] {}), e);
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public ApiResponse getIntrestFlag(int familyMemberId, int flag) {
		try {
			return insuranceFilesRepository.getIntrestFlag(familyMemberId, flag);
		} catch (Exception e) {
			logger.error("Error updating interest flag for familyMemberId: {}, flag: {}", familyMemberId, flag, e);
			throw new FuelAndDriverExpensesException(
					messageBundleSource.getmessagebycode("family.member.update.flag.error", new Object[] {}), e);
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public ApiResponse saveFamilyMembers(UploadFamilyMembersDetails uploadDetails) {
		try {
			boolean flag = insuranceFilesRepository
					.getFlag(insuranceFilesRepository.getDOJ(uploadDetails.getEmployeeId()));
			if (!flag) {
				logger.warn("Date expired for employeeId: {}", uploadDetails.getEmployeeId());
				throw new InsuranceDetailsDateExpiredException(
						messageBundleSource.getmessagebycode("family.data.enable.date.error", new Object[] {}));
			}
			return insuranceFilesRepository.saveFamilyMembers(uploadDetails);
		} catch (InsuranceDetailsDateExpiredException ex) {
			throw ex;
		} catch (Exception e) {
			logger.error("Error saving family members for employeeId: {}", uploadDetails.getEmployeeId(), e);
			throw new FuelAndDriverExpensesException(
					messageBundleSource.getmessagebycode("family.members.upload.error", new Object[] {}), e);
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<EmployeeInsuranceCompleteDetailsDTO> getInsurancePremiumDetails() {
		try {
			return insuranceFilesRepository.getInsurancePremiumDetails();
		} catch (Exception e) {
			logger.error("Error fetching insurance premium details", e);
			throw new FuelAndDriverExpensesException(
					messageBundleSource.getmessagebycode("insurance.details.premium.error", new Object[] {}), e);
		}
	}

	/*
	 * @Transactional(rollbackFor = Throwable.class) public ApiResponse
	 * uploadPremiumDetailsInfo(MultipartFile file) { try { return
	 * insuranceFilesRepository.uploadPremiumDetailsInfo(file); } catch
	 * (InsuranceDetailsDateExpiredException ex) { throw ex; } catch (Exception e) {
	 * logger.error("Error uploading premium details file: {}",
	 * file.getOriginalFilename(), e); throw new FuelAndDriverExpensesException(
	 * messageBundleSource.getmessagebycode("insurance.details.excel.read.error",
	 * new Object[] {}), e); } }
	 */

	@Transactional(rollbackFor = Throwable.class)
	public ApiResponse uploadPremiumDetailsInfo(MultipartFile file) {
		List<String> missingGrossPremiumEmployees = new ArrayList<>();
		List<Object[]> validRecords = new ArrayList<>();

		try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
			Sheet sheet = workbook.getSheetAt(0);

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row == null)
					continue;

				String empId = getStringCellValue(row.getCell(1));
				String relation = getStringCellValue(row.getCell(3));
				Double sumInsurance = getNumericCellValue(row.getCell(11));
				Double grossPremium = getNumericCellValue(row.getCell(14));
				if (empId == null || relation == null || sumInsurance == null)
					continue;
				if ("EMP".equalsIgnoreCase(relation)) {
					if (grossPremium == null || grossPremium == 0) {
						missingGrossPremiumEmployees.add(empId);
					} else {
						validRecords.add(new Object[] { empId, sumInsurance, grossPremium });
					}
				}
			}
			if (!missingGrossPremiumEmployees.isEmpty()) {
				String missingList = String.join(", ", missingGrossPremiumEmployees);
				throw new InsuranceDetailsDateExpiredException(
						"Upload Excel Failed, Gross premium is missing for employees: [" + missingList + "]");
			}
			insuranceFilesRepository.insertPremiumDetails(validRecords);
			return new ApiResponse("Employee premium records uploaded successfully.");
		} catch (InsuranceDetailsDateExpiredException ex) {
			throw ex;
		} catch (Exception e) {
			logger.error("Error uploading premium details file: {}", file.getOriginalFilename(), e);
			throw new FuelAndDriverExpensesException(
					messageBundleSource.getmessagebycode("insurance.details.excel.read.error", new Object[] {}), e);
		}
	}

	private String getStringCellValue(Cell cell) {
		if (cell == null)
			return null;

		switch (cell.getCellType()) {
		case STRING:
			return cell.getStringCellValue().trim();
		case NUMERIC:
			return String.valueOf((long) cell.getNumericCellValue());
		default:
			return null;
		}
	}

	private Double getNumericCellValue(Cell cell) {
		if (cell == null)
			return null;
		return cell.getCellType() == CellType.NUMERIC ? cell.getNumericCellValue() : null;
	}

	@Transactional(rollbackFor = Throwable.class)
	public ApiResponse updateInterestStatus(int premiumInfoId, int flag) {
		try {
			return insuranceFilesRepository.updateInterestStatus(premiumInfoId, flag);
		} catch (Exception e) {
			logger.error("Error updating interest status for premiumInfoId: {}, flag: {}", premiumInfoId, flag, e);
			throw new FuelAndDriverExpensesException(
					messageBundleSource.getmessagebycode("insurance.interest.update.error", new Object[] {}), e);
		}
	}
}
