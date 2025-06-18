package com.hetero.heteroiconnect.insurancefiles;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.hetero.heteroiconnect.requisition.forms.ApiResponse;

public interface InsuranceFilesService {
	ApiResponse uploadFiles(String type, List<MultipartFile> files);

	List<InsuranceFileDTO> getAllInsuranceFiles(Integer type);

	InsuranceFileDTO getEmployeeInsuranceDetails(Integer loginId);

}
