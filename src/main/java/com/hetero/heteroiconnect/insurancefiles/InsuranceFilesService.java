package com.hetero.heteroiconnect.insurancefiles;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.hetero.heteroiconnect.requisition.forms.ApiResponse;
import com.hetero.heteroiconnect.worksheet.model.Master;

public interface InsuranceFilesService {
	ApiResponse uploadFiles(String type, List<MultipartFile> files);

	List<InsuranceFileDTO> getAllInsuranceFiles(Integer type);

	InsuranceFileDTO getEmployeeInsuranceDetails(Integer loginId);

	Map<String, byte[]> getUserManuals();

	List<HrFormDTO> getHrForms(int buId);

	List<HrFormDTO> getMasterHrForms();

	List<HrFormDTO> getSOPForms();

	// Boolean getDates();

	EmployeeBasicDetailsDTO getEmployeeDetails(int empId);

	public List<Master> getRelation();

	ApiResponse deleteFamilyMember(int familyMemberId, int empId);

	ApiResponse getIntrestFlag(int familyMemberId, int flag, int empId);

	ApiResponse saveFamilyMembers(UploadFamilyMembersDetails uploadDetails);

	List<EmployeeInsuranceCompleteDetailsDTO> getInsurancePremiumDetails();

	ApiResponse uploadPremiumDetailsInfo(MultipartFile file);

	ApiResponse updateInterestStatus(int premiumInfoId, int flag);

}
