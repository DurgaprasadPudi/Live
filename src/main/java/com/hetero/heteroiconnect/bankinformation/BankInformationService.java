package com.hetero.heteroiconnect.bankinformation;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface BankInformationService {
	public Map<String, Object> processEmployeeExcel(MultipartFile file, int bankId, int loginId);

	public List<BankDTO> getLatestEmployeeBankDetails(int bankId, int loginId);

//	public List<EmployeeBankDTO> bulkInsertEmployeeBankDetails(List<EmployeeBankDTO> employeeBankList);
	Map<String, Object> bulkInsertEmployeeBankDetails(List<EmployeeBankDTO> employeeBankList);

	List<Map<String, Object>> getDistinctExcelSheetNos(int loginId, int bankId);

	public List<BankDTO> getOverallEmployeeBankDetails(int excelSheetNo);

}