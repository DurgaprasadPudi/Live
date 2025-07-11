package com.hetero.heteroiconnect.contractdetails;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hetero.heteroiconnect.worksheet.model.Master;

public interface ContractService {
	String uploadContractEmployee(ContractPersonDetailsDTO dto, MultipartFile file) throws IOException;

	public List<Master> getCompany();

	public List<Master> getContracts();

	public List<Master> getGender();

	List<ContractDetailsDTO> getAllContractDetails();

	String deleteEmployeeData(int id);

}
