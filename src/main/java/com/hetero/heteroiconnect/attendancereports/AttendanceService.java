package com.hetero.heteroiconnect.attendancereports;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hetero.heteroiconnect.worksheet.utility.MessageBundleSource;

@Service
public class AttendanceService {
	private AttendanceRepository attendanceRepository;
	private MessageBundleSource messageBundleSource;
	Logger logger = LoggerFactory.getLogger(AttendanceService.class);

	public AttendanceService(AttendanceRepository attendanceRepository, MessageBundleSource messageBundleSource) {
		this.attendanceRepository = attendanceRepository;
		this.messageBundleSource = messageBundleSource;
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<AttendanceLocationPojo> getLocations(int empId) {
		try {
			return attendanceRepository.getLocations(empId);
		} catch (Exception e) {
			logger.error("{} error fetching locations", e.getMessage());
			throw new LocationsFetchingException(
					messageBundleSource.getmessagebycode("attendance.locations.error", new Object[] {}));
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<Map<String, Object>> getData(AttendanceFilterPojo attendanceFilterPojo) {
		try {
			return attendanceRepository.getData(attendanceFilterPojo);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("{} error fetching attendance data at {}", e.getMessage(), LocalDateTime.now());
			throw new AttendanceDataFetchingException(
					messageBundleSource.getmessagebycode("attendance.data.error", new Object[] {}));
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<AttendanceLocationPojo> getEmployeebusinessunit(int empId) {
		try {
			return attendanceRepository.getEmployeebusinessunit(empId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("{} error fetching locations", e.getMessage());
			throw new LocationsFetchingException(
					messageBundleSource.getmessagebycode("attendance.locations.error", new Object[] {}));
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<Map<String, Object>> getPayPeriodMonths(Integer empId, Integer location, List<Integer> bu,
			String callName, boolean isPayPeriod) {
		try {
			return attendanceRepository.getPayPeriodMonths(empId, location, bu, callName, isPayPeriod);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("{} error fetching pay period months", e.getMessage());
			throw new PayPeriodMonthsFetchingException(
					messageBundleSource.getmessagebycode("payperiod.months.error", new Object[] {}));
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<Map<String, Object>> getReaderData(LocalDate fromDate, LocalDate toDate) {
		try {
			return attendanceRepository.getReaderData(fromDate, toDate);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("{} error fetching reader data", e.getMessage());
			throw new ReaderDataFetchingException(
					messageBundleSource.getmessagebycode("reader.data.fetch.error", new Object[] {}));
		}
	}
	
	
	@Transactional(rollbackFor = Throwable.class)
	public List<Map<String, Object>> getEmployeesLunchData(String dateValue) {
		try {
			return attendanceRepository.getEmployeesLunchData(dateValue);
		} catch (Exception e) {
			logger.error("{} error fetching reader employeelunch data", e.getMessage());
			throw new ReaderDataFetchingException(
					messageBundleSource.getmessagebycode("employee.lunch.reader.data.fetch.error", new Object[] {}));
		}
	}
}
