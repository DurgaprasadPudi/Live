package com.hetero.heteroiconnect.fuelanddriverhistory;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hetero.heteroiconnect.worksheet.exception.DuplicateEmployeeException;
import com.hetero.heteroiconnect.worksheet.exception.UserWorkSheetUploadException;
import com.hetero.heteroiconnect.worksheet.utility.MessageBundleSource;

@Service
public class FuelAndDriverServiceImpl implements FuelAndDriverService {

	private final FuelAndDriverRepository fuelAndDriverRepository;
	private final MessageBundleSource messageBundleSource;

	public FuelAndDriverServiceImpl(FuelAndDriverRepository fuelAndDriverRepository,
			MessageBundleSource messageBundleSource) {
		this.fuelAndDriverRepository = fuelAndDriverRepository;
		this.messageBundleSource = messageBundleSource;
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<FuelAndDriverResponseDTO> getFuelAndDriverDetails(String payPeriod) {
		try {
			return fuelAndDriverRepository.getFuelAndDriverDetails(payPeriod);
		} catch (Exception e) {
			throw new UserWorkSheetUploadException(
					messageBundleSource.getmessagebycode("fuel.and.driver.maintanence.fetching.error", new Object[] {}),
					e);
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<Map<String, String>> getPayPeriodsWithMonthYear() {
		try {
			return fuelAndDriverRepository.getPayPeriodsWithMonthYear();
		} catch (Exception e) {
			throw new UserWorkSheetUploadException(messageBundleSource
					.getmessagebycode("fuel.and.driver.maintanence.payperiod.error", new Object[] {}), e);
		}
	}

	/*
	 * @Transactional(rollbackFor = Throwable.class) public String
	 * addFuelDriverHistory(FuelDriverHistoryInsertDTO dto) { double claimedAmount =
	 * Double.parseDouble(dto.getClaimedAmount()); double totalAmount =
	 * Double.parseDouble(dto.getTotalAmount()); if (claimedAmount > totalAmount) {
	 * throw new DuplicateEmployeeException(
	 * messageBundleSource.getmessagebycode("fuel.driver.claim.exceeds.total",
	 * null)); } try { return fuelAndDriverRepository.addFuelDriverHistory(dto); }
	 * catch (Exception e) { throw new UserWorkSheetUploadException(
	 * messageBundleSource.getmessagebycode(
	 * "fuel.and.driver.maintanence.upload.error", new Object[] {}), e); } }
	 */

	@Transactional(rollbackFor = Throwable.class)
	public String addFuelDriverHistory(List<FuelDriverHistoryInsertDTO> dtos) {
		for (FuelDriverHistoryInsertDTO dto : dtos) {
			double claimedAmount = Double.parseDouble(dto.getClaimedAmount());
			double totalAmount = Double.parseDouble(dto.getTotalAmount());
			if (claimedAmount > totalAmount) {
				throw new DuplicateEmployeeException(
						messageBundleSource.getmessagebycode("fuel.driver.claim.exceeds.total", null));
			}
		}
		try {
			return fuelAndDriverRepository.addFuelDriverHistory(dtos);
		} catch (Exception e) {
			throw new UserWorkSheetUploadException(
					messageBundleSource.getmessagebycode("fuel.and.driver.maintanence.upload.error", new Object[] {}),
					e);
		}
	}

}
