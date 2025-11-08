package com.hetero.heteroiconnect.bankinformation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.hetero.heteroiconnect.worksheet.exception.BankInvalidEmployeeException;
import com.hetero.heteroiconnect.worksheet.exception.FuelAndDriverExpensesException;
import com.hetero.heteroiconnect.worksheet.utility.MessageBundleSource;

@Service
public class BankInformationServiceImpl implements BankInformationService {

	private static final Logger logger = LoggerFactory.getLogger(BankInformationServiceImpl.class);

	private final BankInformationRepository bankInformationRepository;
	private final MessageBundleSource messageBundleSource;

	public BankInformationServiceImpl(BankInformationRepository bankInformationRepository,
			MessageBundleSource messageBundleSource) {
		this.bankInformationRepository = bankInformationRepository;
		this.messageBundleSource = messageBundleSource;
	}

	@Transactional(rollbackFor = Throwable.class)
	public Map<String, Object> processEmployeeExcel(MultipartFile file, int bankId, int loginId) {
		Map<String, Object> result = new HashMap<>();

		int excelSheetNo = bankInformationRepository.getLastExcelSheetNo();
		System.err.println(" db excel sheet no" + excelSheetNo);
		int nextExcelSheetNo = excelSheetNo + 1;
		System.out.println(" next excel sheet no + :" + nextExcelSheetNo);

		List<Integer> allSequenceNos = new ArrayList<>();

		// 1️⃣ Read all employee sequence numbers from Excel
		try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
			Sheet sheet = workbook.getSheetAt(0);
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row == null)
					continue;
				getCellValue(row.getCell(0)).filter(Double.class::isInstance).map(Double.class::cast)
						.map(Double::intValue).ifPresent(allSequenceNos::add);
			}
		} catch (IOException e) {
			logger.error("Error reading Excel file", e);
			throw new FuelAndDriverExpensesException(
					messageBundleSource.getmessagebycode("bank.file.read.error", new Object[] {}), e);
		}

		// 2️⃣ Fetch sequence→employeeId map from DB
		Map<Integer, Integer> empMap = bankInformationRepository.getEmployeeIdMapBySequenceNos(allSequenceNos);

		// 3️⃣ Identify not-found employees (sequence numbers not in map)
		List<Integer> notEmployees = allSequenceNos.stream().filter(seq -> !empMap.containsKey(seq))
				.collect(Collectors.toList());

		// 4️⃣ Extract valid employee IDs
		List<Integer> realEmployeeIds = new ArrayList<>(empMap.values());

		logger.info("Valid Employee IDs: {}", realEmployeeIds);
		logger.info("Not Employees (Invalid Sequence Numbers): {}", notEmployees);

		// 5️⃣ Get existing bank records
		List<EmployeeBankDTO> existingRecords = bankInformationRepository.getEmployeeBankDetails();
		Set<Integer> existingEmpIds = existingRecords.stream().map(EmployeeBankDTO::getEmpId)
				.collect(Collectors.toSet());
		Set<String> existingAccounts = existingRecords.stream().map(EmployeeBankDTO::getBankAccount)
				.collect(Collectors.toSet());

		List<Integer> typeDupData = bankInformationRepository.getEmployeeSequenceNumbersForType4();

		List<Integer> duplicateEmpIds = new ArrayList<>();
		List<Integer> invalidIfscEmpIds = new ArrayList<>();
		List<Integer> validEmpIds = new ArrayList<>();
		List<Integer> typeDuplicateEmpIds = new ArrayList<>();

		// 6️⃣ Re-read Excel for processing
		try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
			Sheet sheet = workbook.getSheetAt(0);

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row == null)
					continue;

				int empSequenceNo = getCellValue(row.getCell(0)).filter(Double.class::isInstance)
						.map(Double.class::cast).map(Double::intValue).orElse(0);

				// String name = getCellValue(row.getCell(1)).map(Object::toString).orElse("");
				// String doj = getCellValue(row.getCell(2)).map(Object::toString).orElse("");
				String ifsc = getCellValue(row.getCell(1)).map(Object::toString).orElse("").trim().toUpperCase();
				String accountNo = getCellValue(row.getCell(2)).map(Object::toString).orElse("");

				// 7️⃣ If sequence not found in DB, mark as not employee
				if (!empMap.containsKey(empSequenceNo)) {
					bankInformationRepository.insertEmployeeBankRecordDuplicates(empSequenceNo, ifsc, accountNo, bankId,
							nextExcelSheetNo, 1, loginId);
					continue;
				}

				int empId = empMap.get(empSequenceNo);

				// 9️⃣ Original Data Check duplicates
				if (existingEmpIds.contains(empId) || existingAccounts.contains(accountNo)) {
					duplicateEmpIds.add(empId);
					bankInformationRepository.insertEmployeeBankRecordDuplicates(empId, ifsc, accountNo, bankId,
							nextExcelSheetNo, 2, loginId);
					continue;
				}
				// 11 Type Duplicates
				if (typeDupData.contains(empId)) {
					typeDuplicateEmpIds.add(empId);
					bankInformationRepository.insertEmployeeBankRecordDuplicates(empId, ifsc, accountNo, bankId,
							nextExcelSheetNo, 5, loginId);
					continue;
				}
				// 8️⃣ Validate IFSC
				if (!isValidIfsc(ifsc, bankId)) {
					invalidIfscEmpIds.add(empId);
					bankInformationRepository.insertEmployeeBankRecordDuplicates(empId, ifsc, accountNo, bankId,
							nextExcelSheetNo, 3, loginId);
					continue;
				}

				// 🔟 New valid record
				bankInformationRepository.insertEmployeeBankRecordDuplicates(empId, ifsc, accountNo, bankId,
						nextExcelSheetNo, 4, loginId);
				validEmpIds.add(empId);
			}

		} catch (IOException e) {
			logger.error("Error processing Excel file", e);
			throw new FuelAndDriverExpensesException(
					messageBundleSource.getmessagebycode("bank.file.read.error", new Object[] {}), e);
		}

		// ✅ Prepare result summary
		result.put("Original Duplicate Employees", duplicateEmpIds);
		result.put("IFSC Invalid Employees", invalidIfscEmpIds);
		result.put("Invalid Employees", notEmployees);
		result.put("Valid Employees", validEmpIds);

		return result;
	}

	private Optional<Object> getCellValue(Cell cell) {
		if (cell == null)
			return Optional.empty();

		switch (cell.getCellType()) {
		case STRING:
			return Optional.of(cell.getStringCellValue().trim());
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				return Optional.of(cell.getDateCellValue().toString());
			} else {
				return Optional.of(cell.getNumericCellValue());
			}
		case BOOLEAN:
			return Optional.of(cell.getBooleanCellValue());
		case FORMULA:
			try {
				return Optional.of(cell.getStringCellValue());
			} catch (IllegalStateException e) {
				return Optional.of(cell.getNumericCellValue());
			}
		default:
			return Optional.empty();
		}
	}
	/*
	 * private boolean isValidIfsc(String ifsc,int bankId) { return ifsc != null &&
	 * ifsc.matches("^[A-Z]{4}0[A-Z0-9]{6}$"); }
	 */

	private boolean isValidIfsc(String ifsc, int bankId) {
		if (ifsc == null || ifsc.isEmpty()) {
			return false;
		}
		String dbPattern = bankInformationRepository.getIfscNameByBankId(bankId);
		// If no pattern found in DB, use default pattern
		// String regex = (dbPattern != null && !dbPattern.trim().isEmpty()) ?
		// dbPattern.trim() : "^[A-Z]{4}0[A-Z0-9]{6}$";
		String regex = (dbPattern != null && !dbPattern.trim().isEmpty()) ? "^" + dbPattern.trim() + "0[A-Z0-9]{6}$"
				: "^[A-Z]{4}0[A-Z0-9]{6}$";
		return ifsc.matches(regex);
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<BankDTO> getLatestEmployeeBankDetails(int bankId, int loginId) {
		try {
			return bankInformationRepository.getLatestEmployeeBankDetails(bankId, loginId);
		} catch (Exception e) {
			throw new FuelAndDriverExpensesException(
					messageBundleSource.getmessagebycode("bank.details.fecthing.error", new Object[] {}), e);
		}
	}

	/*
	 * @Transactional(rollbackFor = Throwable.class) public List<EmployeeBankDTO>
	 * bulkInsertEmployeeBankDetails(List<EmployeeBankDTO> employeeBankList) { try {
	 * List<Integer> snoList =
	 * employeeBankList.stream().map(EmployeeBankDTO::getSno).collect(Collectors.
	 * toList()); checkingValidOrNot(snoList);
	 * bankInformationRepository.bulkInsertEmployeeBankDetails(employeeBankList);
	 * bankInformationRepository.updatedToMainTableStatus(snoList); return
	 * employeeBankList; } catch (BankInvalidEmployeeException e) { throw e; } catch
	 * (Exception e) { throw new FuelAndDriverExpensesException(
	 * messageBundleSource.getmessagebycode("bank.details.inserting.error", new
	 * Object[] {}), e); } }
	 */

	@Transactional(rollbackFor = Throwable.class)
	public Map<String, Object> bulkInsertEmployeeBankDetails(List<EmployeeBankDTO> employeeBankList) {
		Map<String, Object> response = new HashMap<>();
		try {
			List<Integer> snoList = employeeBankList.stream().map(EmployeeBankDTO::getSno).collect(Collectors.toList());
			checkingValidOrNot(snoList);

			bankInformationRepository.bulkInsertEmployeeBankDetails(employeeBankList);
			bankInformationRepository.updatedToMainTableStatus(snoList);
			response.put("message", "Employee bank details inserted successfully");
			/*
			 * response.put("insertedCount", inserted.size()); response.put("totalRecords",
			 * employeeBankList.size());
			 */
		} catch (BankInvalidEmployeeException ex) {
			throw ex;
		} catch (Exception e) {
			throw new FuelAndDriverExpensesException(
					messageBundleSource.getmessagebycode("bank.details.inserting.error", new Object[] {}), e);
		}
		return response;
	}

	public int checkingValidOrNot(List<Integer> snoList) {
		int result = bankInformationRepository.areAllType4(snoList);
		if (result == 0) {
			throw new BankInvalidEmployeeException(
					messageBundleSource.getmessagebycode("bank.details.invalid.type", new Object[] {}));
		}
		return result;
	}

	@Transactional(rollbackFor = Throwable.class)

	public List<Map<String, Object>> getDistinctExcelSheetNos(int loginId, int bankId) {
		try {
			return bankInformationRepository.getDistinctExcelSheetNos(loginId, bankId);
		} catch (Exception e) {
			throw new BankInvalidEmployeeException(
					messageBundleSource.getmessagebycode("bank.details.count.fetching.error", new Object[] {}), e);
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<BankDTO> getOverallEmployeeBankDetails(int excelSheetNo) {
		try {
			return bankInformationRepository.getOverallEmployeeBankDetails(excelSheetNo);
		} catch (Exception e) {
			throw new FuelAndDriverExpensesException(
					messageBundleSource.getmessagebycode("bank.details.fecthing.error", new Object[] {}), e);
		}
	}
}
