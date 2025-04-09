package com.hetero.heteroiconnect.worksheet.serviceimpl;

import java.util.List;

import javax.validation.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hetero.heteroiconnect.worksheet.exception.DeleteTaskException;
import com.hetero.heteroiconnect.worksheet.exception.TaskApprovalException;
import com.hetero.heteroiconnect.worksheet.exception.TaskOverlapException;
import com.hetero.heteroiconnect.worksheet.exception.UserWorkSheetFetchingException;
import com.hetero.heteroiconnect.worksheet.exception.UserWorkSheetUploadException;
import com.hetero.heteroiconnect.worksheet.model.EmployeeSummary;
import com.hetero.heteroiconnect.worksheet.model.TotalWorkingHours;
import com.hetero.heteroiconnect.worksheet.model.UserWorksheet;
import com.hetero.heteroiconnect.worksheet.repository.UserRepository;
import com.hetero.heteroiconnect.worksheet.service.UserService;
import com.hetero.heteroiconnect.worksheet.utility.MessageBundleSource;

  

@Service
public class UserServiceImpl implements UserService {
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	private UserRepository userRepository;
	private MessageBundleSource messageBundleSource;

	public UserServiceImpl(UserRepository userRepository, MessageBundleSource messageBundleSource) {
		this.userRepository = userRepository;
		this.messageBundleSource = messageBundleSource;
	}

	@Transactional(rollbackFor = Throwable.class)
	public UserWorksheet addUserData(UserWorksheet userWorksheet) {
		try {
			return userRepository.addUserData(userWorksheet);
		} catch (ValidationException ve) {
			logger.error("Validation error while uploading worksheet for employee {}: {}",
					userWorksheet.getEmployeeId(), ve.getMessage());
			throw ve;
		} catch (TaskOverlapException v) {
			logger.error(
					"Error while checking task overlap. Task Date: {}, Employee ID: {}, Start Time: {}, Message: {}",
					userWorksheet.getTaskDate(), userWorksheet.getEmployeeId(), userWorksheet.getStartTime(),
					v.getMessage());
			throw v;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error uploading worksheet for employee {}: {}", userWorksheet.getEmployeeId(),
					e.getMessage());
			throw new UserWorkSheetUploadException(
					messageBundleSource.getmessagebycode("worksheet.upload.error", new Object[] {}), e);
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public TotalWorkingHours getDailyWorkSheet(int employeeId) {
		try {
			return userRepository.getDailyWorkSheet(employeeId);
		} catch (Exception e) {
			logger.error("{} --- error while fetching  {} worksheet", e.getMessage(), employeeId);
			throw new UserWorkSheetFetchingException(
					messageBundleSource.getmessagebycode("daily.worksheet.list.fetching.error", new Object[] {}));
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public String submitForApproval(int employeeId) {
		try {
			return userRepository.submitForApproval(employeeId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new TaskApprovalException(
					messageBundleSource.getmessagebycode("approval.update.error", new Object[] {}), e);
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<EmployeeSummary> getUserWeekSummaryWorkSheet(int employeeId) {
		try {
			return userRepository.getUserWeekSummaryWorkSheet(employeeId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("{} --- error while fetching  {} worksheet", e.getMessage(), employeeId);
			throw new UserWorkSheetFetchingException(
					messageBundleSource.getmessagebycode("worksheet.list.fetching.error", new Object[] {}));
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public String deleteTask(int sno) {
		try {
			return userRepository.deleteTask(sno);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DeleteTaskException(messageBundleSource.getmessagebycode("task.delete.error", new Object[] {}),
					e);
		}
	}

	/*
	 * @Transactional(rollbackFor = Throwable.class) public UpdateWorkSheet
	 * updateWorkSheet(UpdateWorkSheet updateWorkSheet) { try { return
	 * userRepository.updateWorkSheet(updateWorkSheet); } catch
	 * (TaskOverlapException v) { logger.error(
	 * "Error while checking task overlap. Task Date: {}, Employee ID: {}, Start Time: {}, Message: {}"
	 * , updateWorkSheet.getTaskDate(), updateWorkSheet.getEmployeeId(),
	 * updateWorkSheet.getStartTime(), v.getMessage()); throw v; } catch (Exception
	 * e) { e.printStackTrace();
	 * logger.error("Error uploading worksheet for employee {}: {}",
	 * updateWorkSheet.getEmployeeId(), e.getMessage()); throw new
	 * UserWorkSheetUpdateException(
	 * messageBundleSource.getmessagebycode("worksheet.update.error", new Object[]
	 * {}), e); } }
	 */
}
