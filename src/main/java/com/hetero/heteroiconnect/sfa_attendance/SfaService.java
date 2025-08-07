package com.hetero.heteroiconnect.sfa_attendance;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hetero.heteroiconnect.worksheet.utility.MessageBundleSource;


@Service
public class SfaService {
	private SfaRepository sfaRepository;
	private MessageBundleSource messageBundleSource;

	public SfaService(SfaRepository sfaRepository, MessageBundleSource messageBundleSource) {
		this.sfaRepository = sfaRepository;
		this.messageBundleSource = messageBundleSource;
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<String> getEmployeeList() {
		try {
			return sfaRepository.getEmployeeList();
		} catch (Exception e) {
			e.printStackTrace();
			throw new SfaEmployeesFetchingException(
					messageBundleSource.getmessagebycode("sfa.employees.fetching.error", new Object[] {}));
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public Map<String, Object> getPayrollDates() {
		try {
			return sfaRepository.getPayrollDates();
		} catch (Exception e) {
			e.printStackTrace();
			throw new SfaAttendanceFetchingException(
					messageBundleSource.getmessagebycode("sfa.attendance.fetching.error", new Object[] {}));
		}
	}

	public Map<String, Object> enrichRecord(Map<String, Object> bean) {
		String empId = (String) bean.get("EmployeeID");
		Map<String, Object> details = sfaRepository.getEmployeeDetails(empId);

		LinkedHashMap<String, Object> updatedRecord = new LinkedHashMap<>();
		updatedRecord.put("EMPID", empId);
		updatedRecord.put("NAME", details.get("Name"));
		updatedRecord.put("STATUS", details.get("Status"));
		updatedRecord.put("COSTCENTER", details.get("CostCenter"));
		updatedRecord.put("DIVISION", details.get("Division"));
		updatedRecord.put("DEPARTMENT", details.get("Department"));
		updatedRecord.put("DESIGNATION", details.get("Designation"));

		for (Map.Entry<String, Object> entry : bean.entrySet()) {
			if (!"EmployeeID".equals(entry.getKey())) {
				updatedRecord.put(entry.getKey(), entry.getValue());
			}
		}
		return updatedRecord;
	}
}
