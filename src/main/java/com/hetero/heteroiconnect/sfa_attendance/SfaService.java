package com.hetero.heteroiconnect.sfa_attendance;

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
}
