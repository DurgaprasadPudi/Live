package com.hetero.heteroiconnect.requisition.forms;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hetero.heteroiconnect.worksheet.utility.MessageBundleSource;

 

@Service
public class RequisitionFormServiceImpl implements RequisitionFormService {

	private static final Logger logger = LoggerFactory.getLogger(RequisitionFormServiceImpl.class);

	private final RequisitionFormRepository requisitionFormRepository;
	private final MessageBundleSource messageBundleSource;

	public RequisitionFormServiceImpl(RequisitionFormRepository requisitionFormRepository,
			MessageBundleSource messageBundleSource) {
		this.requisitionFormRepository = requisitionFormRepository;
		this.messageBundleSource = messageBundleSource;
	}

	@Transactional(rollbackFor = Throwable.class)
	public String insertManpowerForm(ManpowerRequisitionFormDTO dto) {
		try {
			return requisitionFormRepository.insertManpowerForm(dto);
		} catch (Exception e) {
			logger.error("Error inserting manpower form", e);
			throw new UploadPositionException(
					messageBundleSource.getmessagebycode("manpower.insertion.form.error", new Object[] {}));
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public EmployeeDetailsDTO getEmployeeDetails(int employeeId) {
		try {
			return requisitionFormRepository.getEmployeeDetails(employeeId);
		} catch (Exception e) {
			logger.error("Error  while retriving  EmployeeDetails", e);
			throw new UploadPositionException(
					messageBundleSource.getmessagebycode("employee.details.error", new Object[] {}));
		}

	}

	@Transactional(rollbackFor = Throwable.class)
	public List<MasterData> getBu(String name) {
		return requisitionFormRepository.getBu(name);
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<MasterData> getTitle() {
		return requisitionFormRepository.getTitle();
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<MasterData> getDepartment() {
		return requisitionFormRepository.getDepartment();
	}

	@Transactional(rollbackFor = Throwable.class)
	public String insertPositions(CompanyBasicDetailsDTO dto) {
		try {
			int companyId = requisitionFormRepository.insertCompanyBasicDetails(dto);
			requisitionFormRepository.insertEmployeeStrengthAndVacancies(dto.getEmployeeStrengthAndVacanciesDTO(),
					companyId);
			return "Insertion Successful";
		} catch (Exception e) {
			logger.error("Error  while retriving  insertPositions", e);
			throw new UploadPositionException(
					messageBundleSource.getmessagebycode("manpower.insertion.positions.form.error", new Object[] {}));
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<MasterData> getDependentName(String name) {
		return requisitionFormRepository.getDependentName(name);
	}
}
