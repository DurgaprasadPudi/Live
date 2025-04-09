
package com.hetero.heteroiconnect.hrassetrequests;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Service
public class HrAssetRaisingServiceImpl implements HrAssetRaisingService {
	private static final Logger logger = LoggerFactory.getLogger(HrAssetRaisingServiceImpl.class);

	private AssetRepository employeeRepository;
	private ITAsyncEmailService asyncEmailService;
	private HRAsyncEmailService asyncEmailServiceForHR;

	public HrAssetRaisingServiceImpl(AssetRepository employeeRepository, ITAsyncEmailService asyncEmailService,
			HRAsyncEmailService asyncEmailServiceForHR) {
		this.employeeRepository = employeeRepository;
		this.asyncEmailService = asyncEmailService;
		this.asyncEmailServiceForHR = asyncEmailServiceForHR;
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<Master> getBusinessUnits(String loginId) {
		List<Object[]> rows = employeeRepository.getBu(loginId);
		return rows.stream().map(row -> new Master((int) row[0], (String) row[1])).collect(Collectors.toList());
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<Master> getDepartments() {
		List<Object[]> rows = employeeRepository.getDepartments();
		return rows.stream().map(row -> new Master((int) row[0], (String) row[1])).collect(Collectors.toList());
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<Master> getDesignations() {
		List<Object[]> rows = employeeRepository.getDesignations();
		return rows.stream().map(row -> new Master((int) row[0], (String) row[1])).collect(Collectors.toList());
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<Master> getManagers() {
		List<Object[]> rows = employeeRepository.getManagers();
		return rows.stream().map(row -> new Master((int) row[0], (String) row[1])).collect(Collectors.toList());
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<Master> getAssetTypes() {
		List<Object[]> rows = employeeRepository.getAssetTypes();
		return rows.stream().map(row -> new Master((int) row[0], (String) row[1])).collect(Collectors.toList());
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<Master> getDomain() {
		List<Object[]> rows = employeeRepository.getDomain();
		return rows.stream().map(row -> new Master((int) row[0], (String) row[1])).collect(Collectors.toList());
	}

	@Transactional(rollbackFor = Throwable.class)
	public ResponseEntity<String> employeeAssetsRaise(String empData, String assets) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		try {
			EmployeeDetailsDTO employeeData = objectMapper.readValue(empData, EmployeeDetailsDTO.class);
			Integer existingBuRecords = employeeRepository.getActiveHrBu(employeeData.getLoginId(),
					employeeData.getBu());
			if (existingBuRecords == 0) {
				logger.warn("Employee {} with BU {} is not authorized for asset request.", employeeData.getEmpId(),
						employeeData.getBu());
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
						"You're not authorized to raise a request for this Business Unit. kindly refresh the page, To continue");
			}
			EmployeeAssetDataDTO[] assetDataArray = objectMapper.readValue(assets, EmployeeAssetDataDTO[].class);
			List<EmployeeAssetDataDTO> assetDataList = Arrays.asList(assetDataArray);
			logger.info("Raising employee asset request for employee: {}", employeeData.getEmpName());
			employeeRepository.raiseEmployeeAssetRequest(employeeData.getEmpId(), employeeData.getEmpName(),
					employeeData.getDepartment(), employeeData.getDesignation(), employeeData.getContactNum(),
					employeeData.getReportingManager(), employeeData.getTentativeJoiningDate(),
					employeeData.getLoginId(), employeeData.getBu(), employeeData.getWorkLocation());

			Integer requestId = Optional.ofNullable(employeeRepository.getLastInsertedRequestId())
					.orElseThrow(() -> new Exception("Failed to retrieve last inserted request ID"));
			assetDataList.forEach(assetData -> {
				try {
					employeeRepository.raiseAssetRequest(assetData.getAssetTypeId(), assetData.getCount(), requestId,
							assetData.getRemarks());
				} catch (Exception e) {
					logger.error("Error inserting asset request for asset type: {}", assetData.getAssetTypeId(), e);
					throw new RuntimeException(
							"Error inserting asset request for asset type: " + assetData.getAssetTypeId(), e);
				}
			});
			List<Object[]> emailData = employeeRepository.getEmailAddress(employeeData.getLoginId(),
					employeeData.getBu(), requestId);
			List<Object[]> asset = employeeRepository.getAssetNames(requestId);
			asyncEmailService.sendEmail(employeeData.getLoginId(), emailData, asset, requestId);
			return ResponseEntity.ok("Employee asset request raised successfully.");
		} catch (Exception e) {
			logger.error("Error processing employee asset requests: {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error processing employee asset requests" + e.getMessage());
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<AssetRequestDTO> getRaisedAssetsWithItems(HrFilter hrFilter) {
		Integer loginId = hrFilter.getLoginId();
		Integer dept = hrFilter.getDepartment() != 0 ? hrFilter.getDepartment() : null;
		String assetType = Optional.ofNullable(hrFilter.getAssetType()).orElse(null);
		Integer bu = hrFilter.getBu() != 0 ? hrFilter.getBu() : null;
		LocalDate fromDate = hrFilter.getTentativeFromDate();
		LocalDate toDate = hrFilter.getTentativeToDate();
		Integer status = hrFilter.getStatus() != 0 ? hrFilter.getStatus() : null;
		Integer pageSize = hrFilter.getPageSize();
		Integer pageNo = hrFilter.getPageNo();

		int offset = (pageNo - 1) * pageSize;

		logger.info(
				"Fetching raised assets with items for loginId: {}, department: {}, assetType: {}, bu: {}, fromDate: {}, toDate: {}, status: {}",
				loginId, dept, assetType, bu, fromDate, toDate, status);

		try {
			List<Object[]> assets = employeeRepository.getRaisedAssetsWithItems(loginId, dept, bu, fromDate, toDate,
					status, assetType, pageSize, offset);

			if (assets.isEmpty()) {
				logger.warn("No raised assets found for the given filters.");
				return Collections.emptyList();
			}

			return assets.stream().map(asset -> {
				try {
					Integer requestId = (Integer) asset[0];
					List<Object[]> items = employeeRepository.getRaisedAssetsItems(requestId, assetType);

					if (items.isEmpty()) {
						logger.warn("Items list is empty for request ID: {}. Skipping.", requestId);
						return null;
					}
					List<AssetItemDTO> itemDTOs = items.stream()
							.map(item -> new AssetItemDTO((String) item[0], (Integer) item[1], (String) item[2]))
							.collect(Collectors.toList());

					return new AssetRequestDTO(requestId, (Integer) asset[1], (String) asset[2], (String) asset[3],
							(String) asset[4], (String) asset[5], (String) asset[6], (String) asset[9],
							asset[7] != null ? asset[7].toString() : null, (String) asset[8], (Date) asset[10],
							(String) asset[11], (Integer) asset[12], (String) asset[13], (String) asset[14],
							(String) asset[15], (String) asset[16], (Date) asset[17], 
							 asset[21] != null ? ((BigInteger) asset[21]).longValue() : null,
							itemDTOs);

				} catch (Exception e) {
					logger.error("Error processing asset data: {}", Arrays.toString(asset), e);
					return null;
				}
			}).filter(Objects::nonNull).collect(Collectors.toList());

		} catch (Exception e) {
			logger.error("Error fetching raised assets with items", e);
			throw new RuntimeException("An unexpected error occurred while fetching asset requests");
		}
	}

	@Retryable(include = CannotAcquireLockException.class, maxAttempts = 1, backoff = @Backoff(delay = 500))
	@Transactional(rollbackFor = Throwable.class)
	public int getAknowledgeStatus(String acknowledgeId, String requestId) {
		try {
			Optional<Integer> data = employeeRepository.findByBU(acknowledgeId, requestId);
			if (!data.isPresent()) {
				logger.warn("No BU found for acknowledgeId: {} and requestId: {}", acknowledgeId, requestId);
				return 1001;
			}
			try {
				Optional<Integer> lockedRequest = employeeRepository.findByRequestIdForUpdate(requestId);
				if (!lockedRequest.isPresent()) {
					logger.warn("Lock not acquired for requestId: {}", requestId);
					return 0;
				}
				int status = employeeRepository.getAknowledgeStatus(acknowledgeId, requestId);
				List<Object[]> emailHrData = employeeRepository.getHrEmailAddress(requestId);
				List<Object[]> asset = employeeRepository.getAssetNames(Integer.parseInt(requestId));
				asyncEmailServiceForHR.sendEmail(emailHrData, asset, requestId);
				return status;
			} catch (CannotAcquireLockException e) {
				logger.error("Lock issue for requestId {}: {}", requestId, e.getMessage());
				throw new CannotAcquireLockException(
						"Request is locked by another transaction. Please try again later.");
			} catch (Exception e) {
				logger.error("Error processing requestId {}: {}", requestId, e.getMessage(), e);
				throw new RuntimeException("Internal server error occurred.");
			}
		} catch (Exception e) {
			logger.error("Error processing requestId {} due to user disabled or other issues: {}", requestId,
					e.getMessage(), e);
			throw new RuntimeException("Internal server error occurred due to user or other system issues.");
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<AssetRequestDTO> getITPendingApprovals(ITFilter itFilter) {
		Integer dept = itFilter.getDepartment() != 0 ? itFilter.getDepartment() : null;
		String assetType = Optional.ofNullable(itFilter.getAssetType()).orElse(null);
		Integer bu = itFilter.getBu() != 0 ? itFilter.getBu() : null;
		LocalDate fromDate = itFilter.getTentativeFromDate();
		LocalDate toDate = itFilter.getTentativeToDate();
		Integer pageSize = itFilter.getPageSize();
		Integer pageNo = itFilter.getPageNo();
		int offset = (pageNo - 1) * pageSize;
		logger.info(
				"Fetching approval assets with items for  department: {}, assetType: {}, bu: {}, fromDate: {}, toDate: {}",
				dept, assetType, bu, fromDate, toDate);
		List<Integer> buMapping = employeeRepository.mappingBuForPendingApproval(itFilter.getLoginId());
		if (buMapping.isEmpty()) {
			logger.warn("No data present for the admin with ID: {}", itFilter.getLoginId());
			return Collections.emptyList();
		}
		try {
			List<Object[]> assets = employeeRepository.getITPendingApprovals(buMapping, dept, bu, fromDate, toDate,
					assetType, pageSize, offset);
			if (assets.isEmpty()) {
				logger.warn("No Approval assets found for the given filters.");
				return Collections.emptyList();
			}
			return assets.stream().map(asset -> {
				try {
					Integer requestId = (Integer) asset[0];
					List<Object[]> items = employeeRepository.getRaisedAssetsItems(requestId, assetType);

					if (items.isEmpty()) {
						logger.warn("Items list is empty for request ID: {}. Skipping.", requestId);
						return null;
					}
					List<AssetItemDTO> itemDTOs = items.stream()
							.map(item -> new AssetItemDTO((String) item[0], (Integer) item[1], (String) item[2]))
							.collect(Collectors.toList());

					return new AssetRequestDTO(requestId, (Integer) asset[1], (String) asset[2], (String) asset[3],
							(String) asset[4], (String) asset[5], (String) asset[6], (String) asset[9],
							asset[7] != null ? asset[7].toString() : null, (String) asset[8], (Date) asset[10],
							(String) asset[11], (Integer) asset[12], (String) asset[13], (String) asset[14],
							(String) asset[15], (String) asset[16], (Date) asset[17], asset[21] != null ? ((BigInteger) asset[21]).longValue() : null, itemDTOs);
 
				} catch (Exception e) {
					logger.error("Error processing asset data: {}", Arrays.toString(asset), e);
					return null;
				}
			}).filter(Objects::nonNull).collect(Collectors.toList());

		} catch (Exception e) {
			logger.error("Error fetching approval assets with items", e);
			throw new RuntimeException("An unexpected error occurred while fetching approval  asset requests");
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<AssetRequestDTO> getITApprovalData(HrFilter hrFilter) {
		//Integer loginId = hrFilter.getLoginId();
		Integer loginId = 1;
		Integer dept = hrFilter.getDepartment() != 0 ? hrFilter.getDepartment() : null;
		String assetType = Optional.ofNullable(hrFilter.getAssetType()).orElse(null);
		Integer bu = hrFilter.getBu() != 0 ? hrFilter.getBu() : null;
		LocalDate fromDate = hrFilter.getTentativeFromDate();
		LocalDate toDate = hrFilter.getTentativeToDate();
		Integer status = hrFilter.getStatus() != 0 ? hrFilter.getStatus() : null;
		Integer pageSize = hrFilter.getPageSize();
		Integer pageNo = hrFilter.getPageNo();

		int offset = (pageNo - 1) * pageSize;

		logger.info(
				"Fetching Approved assets with items for loginId: {}, department: {}, assetType: {}, bu: {}, fromDate: {}, toDate: {}, status: {}",
				loginId, dept, assetType, bu, fromDate, toDate, status);

		try {
			List<Object[]> assets = employeeRepository.getITApprovalData(loginId, dept, bu, fromDate, toDate, status,
					assetType, pageSize, offset);

			if (assets.isEmpty()) {
				logger.warn("No approved assets found for the given filters.");
				return Collections.emptyList();
			}

			return assets.stream().map(asset -> {
				try {
					Integer requestId = (Integer) asset[0];
					List<Object[]> items = employeeRepository.getRaisedAssetsItems(requestId, assetType);

					if (items.isEmpty()) {
						logger.warn("Items list is empty for request ID: {}. Skipping.", requestId);
						return null;
					}

					List<AssetItemDTO> itemDTOs = items.stream()
							.map(item -> new AssetItemDTO((String) item[0], (Integer) item[1], (String) item[2]))
							.collect(Collectors.toList());

					return new AssetRequestDTO(requestId, (Integer) asset[1], (String) asset[2], (String) asset[3],
							(String) asset[4], (String) asset[5], (String) asset[6], (String) asset[9],
							asset[7] != null ? asset[7].toString() : null, (String) asset[8], (Date) asset[10],
							(String) asset[11], (Integer) asset[12], (String) asset[13], (String) asset[14],
							(String) asset[15], (String) asset[16], (Date) asset[17], asset[21] != null ? ((BigInteger) asset[21]).longValue() : null, itemDTOs);
				} catch (Exception e) {
					logger.error("Error processing asset data: {}", Arrays.toString(asset), e);
					return null;
				}
			}).filter(Objects::nonNull).collect(Collectors.toList());

		} catch (Exception e) {
			logger.error("Error fetching Approved assets with items", e);
			throw new RuntimeException("An unexpected error occurred while fetching Approved asset requests");
		}
	}
}
