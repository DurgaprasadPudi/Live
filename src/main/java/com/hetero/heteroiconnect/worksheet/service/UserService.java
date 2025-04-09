package com.hetero.heteroiconnect.worksheet.service;

import java.util.List;
 
import com.hetero.heteroiconnect.worksheet.model.EmployeeSummary;
import com.hetero.heteroiconnect.worksheet.model.TotalWorkingHours;
import com.hetero.heteroiconnect.worksheet.model.UserWorksheet;

public interface UserService {
	public UserWorksheet addUserData(UserWorksheet userWorksheet);

	public TotalWorkingHours getDailyWorkSheet(int employeeId);

	public String submitForApproval(int employeeId);

	public List<EmployeeSummary> getUserWeekSummaryWorkSheet(int employeeId);

	public String deleteTask(int sno);

	// public UpdateWorkSheet updateWorkSheet(UpdateWorkSheet updateWorkSheet);

}
