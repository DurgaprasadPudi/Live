package com.hetero.heteroiconnect.contractdetails;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.hetero.heteroiconnect.worksheet.model.Master;

public interface ContractService {
	String uploadContractEmployee(ContractPersonDetailsDTO dto, MultipartFile file) throws IOException;

	public List<Master> getCompany();

	public List<Master> getContracts(int companyId);

	public List<ContractType> getContractTypes(int contractId, int companyId);

	public List<Master> getGender();

	List<ContractDetailsDTO> getAllContractDetails();

	String deleteEmployeeData(int id);

	String updateDOE(int id, String dateOfExit, String comment);

	Map<String, byte[]> getFile(int contractPersonId);
}
