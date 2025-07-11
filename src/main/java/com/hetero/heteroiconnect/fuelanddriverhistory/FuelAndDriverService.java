package com.hetero.heteroiconnect.fuelanddriverhistory;

import java.util.List;
import java.util.Map;

public interface FuelAndDriverService {
	List<FuelAndDriverResponseDTO> getFuelAndDriverDetails(String payPeriod);

	List<Map<String, String>> getPayPeriodsWithMonthYear();

	String addFuelDriverHistory(List<FuelDriverHistoryInsertDTO>  dto);

}
