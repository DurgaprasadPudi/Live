package com.hetero.heteroiconnect.stationaryandhousekeepingtrack;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.hetero.heteroiconnect.idcard.EmployeeNotFoundException;
import com.hetero.heteroiconnect.worksheet.utility.MessageBundleSource;

@Service
public class SHKService {
	@Value("${stationary.housekeeping.registration.files}")
	private String docPath;
	private SHKRepository shkRepository;
	private MessageBundleSource messageBundleSource;
	private FileToByteConverter fileToByteConverter;
	Logger logger = LoggerFactory.getLogger(SHKService.class);

	public SHKService(SHKRepository shkRepository, MessageBundleSource messageBundleSource,
			FileToByteConverter fileToByteConverter) {
		this.shkRepository = shkRepository;
		this.messageBundleSource = messageBundleSource;
		this.fileToByteConverter = fileToByteConverter;
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<Map<String, Object>> getItems() {
		try {
			return shkRepository.getItems();
		} catch (Exception e) {
			logger.error("{} error fetching all items", e.getMessage());
			throw new ItemsFetchingException(
					messageBundleSource.getmessagebycode("items.fetching.error", new Object[] {}));
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<Map<String, Object>> getStationaryItems() {
		try {
			return shkRepository.getStationaryItems();
		} catch (Exception e) {
			logger.error("{} error fetching stationary items", e.getMessage());
			throw new ItemsFetchingException(
					messageBundleSource.getmessagebycode("items.fetching.error", new Object[] {}));
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<Map<String, Object>> getHouseKeepingItems() {
		try {
			return shkRepository.getHouseKeepingItems();
		} catch (Exception e) {
			logger.error("{} error fetching house keeping items", e.getMessage());
			throw new ItemsFetchingException(
					messageBundleSource.getmessagebycode("items.fetching.error", new Object[] {}));
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public Object raiseRequest(SHKRaiseRequest shkRaiseRequest) {
		try {
			return shkRepository.raiseRequest(shkRaiseRequest);
		} catch (DuplicateKeyException e) {
			e.printStackTrace();
			logger.error("Duplicate key exception while raising item request");
			throw new StationaryHouseKeepingRequestRaiseException(
					messageBundleSource.getmessagebycode("shk.request.raise.error", new Object[] {}));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("{} error raising stationary and house keeping items request", e.getMessage());
			throw new StationaryHouseKeepingRequestRaiseException(
					messageBundleSource.getmessagebycode("shk.request.raise.error", new Object[] {}));
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<Map<String, Object>> parentRequestHistory(LocalDate fromDate, LocalDate toDate) {
		try {
			return shkRepository.parentRequestHistory(fromDate, toDate);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("{} error fetching parent requests", e.getMessage());
			throw new ParentRequestsFetchingException(
					messageBundleSource.getmessagebycode("parent.request.fetch.error", new Object[] {}));
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<Map<String, Object>> childRequestHistory(long requestId) {
		try {
			return shkRepository.childRequestHistory(requestId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("{} error fetching child requests", e.getMessage());
			throw new ChildRequestsFetchingException(
					messageBundleSource.getmessagebycode("child.request.fetch.error", new Object[] {}));
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<Long> getParentsRequests() {
		try {
			return shkRepository.getParentsRequests();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("{} error fetching parent request tickets", e.getMessage());
			throw new ParentRequestTicketsFetchingException(
					messageBundleSource.getmessagebycode("parent.request.tickets.fetch.error", new Object[] {}));
		}
	}

	@Retryable(value = { CannotAcquireLockException.class }, maxAttempts = 2, backoff = @Backoff(delay = 1000))
	@Transactional(rollbackFor = Throwable.class)
	public Object uploadItems(long requestId, int empId, MultipartFile doc, List<SHKItems> items) {
		try {
			String fileName = null;
			if (doc != null && !doc.isEmpty()) {
				// Convert requestId to string and create folder path
				Path requestFolder = Paths.get(docPath, String.valueOf(requestId));

				// Create the directory if it doesn't exist
				if (!Files.exists(requestFolder)) {
					Files.createDirectories(requestFolder);
				}

				// Get original file name and build the target file path
				fileName = doc.getOriginalFilename();
				Path targetFile = requestFolder.resolve(fileName);

				// Copy file to target location
				Files.copy(doc.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
			}
			return shkRepository.uploadItems(requestId, empId, fileName, items);
		} catch (RequestItemsAlreadyUploadedException e) {
			logger.error("{} request items already uploaded", requestId);
			throw new RequestItemsAlreadyUploadedException(
					messageBundleSource.getmessagebycode("request.items.already.uploaded.error", new Object[] {}));
		} catch (CannotAcquireLockException e) {
			logger.error("Concurrent exception while uploading items for the parent request ticket {}", requestId);
			throw new CannotAcquireLockException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("{} error uploading stationary and house keeping items", e.getMessage());
			throw new ItemsUploadException(messageBundleSource.getmessagebycode("items.upload.error", new Object[] {}));
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<Map<String, Object>> parentRegistrationHistory(LocalDate fromDate, LocalDate toDate) {
		try {
			return shkRepository.parentRegistrationHistory(fromDate, toDate).stream().map(parent -> {
				Object document = parent.get("Doc");
				if (document != null) {
					try {
						parent.put("Doc", Base64.getEncoder().encodeToString(fileToByteConverter.getFileAsByteArray(
								docPath + File.separator + parent.get("RegId") + File.separator + document)));
					} catch (Exception e) {
						logger.error("{} error processing parent registration document", e.getMessage());
					}
				}
				return parent;
			}).collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("{} error fetching parent registrations", e.getMessage());
			throw new ParentRegistrationFetchingException(
					messageBundleSource.getmessagebycode("parent.registration.fetch.error", new Object[] {}));
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<Map<String, Object>> childRegistrationhistory(long requestId) {
		try {
			return shkRepository.childRegistrationhistory(requestId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("{} error fetching child registration items", e.getMessage());
			throw new ChildRegistrationFetchingException(
					messageBundleSource.getmessagebycode("child.registration.fetch.error", new Object[] {}));
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<Map<String, Object>> getInventory(String requestType) {
		try {
			return shkRepository.getInventory(requestType);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("{} error fetching inventory data", e.getMessage());
			throw new InventoryFetchingException(
					messageBundleSource.getmessagebycode("inventory.fetch.error", new Object[] {}));
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public Map<String, Object> getEmpDetails(int empId) {
		try {
			return shkRepository.getEmpDetails(empId);
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			logger.error("{} employee not found", empId);
			throw new EmployeeNotFoundException(
					messageBundleSource.getmessagebycode("employee.not.found.error", new Object[] {}));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("{} error fetching employee details", e.getMessage());
			throw new EmployeeDetailsFetchingException(
					messageBundleSource.getmessagebycode("employee.details.fetch.error", new Object[] {}));
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public Map<String, Object> getEmpgetContractorEmpDetailsDetails(int empId) {
		try {
			return shkRepository.getEmpgetContractorEmpDetailsDetails(empId);
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			logger.error("{} contractor employee not found", empId);
			throw new EmployeeNotFoundException(
					messageBundleSource.getmessagebycode("employee.not.found.error", new Object[] {}));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("{} error fetching contractor employee details", e.getMessage());
			throw new EmployeeDetailsFetchingException(
					messageBundleSource.getmessagebycode("employee.details.fetch.error", new Object[] {}));
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<Map<String, Object>> getUploadsOfSameType(int type) {
		try {
			return shkRepository.getUploadsOfSameType(type);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("{} error fetching individually uploaded inventory data", e.getMessage());
			throw new IndividualUploadInventoryFetchingException(
					messageBundleSource.getmessagebycode("individual.upload.inventory.fetch.error", new Object[] {}));
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<Map<String, Object>> getRequestedItemTypes(long parentRequestId) {
		try {
			return shkRepository.getRequestedItemTypes(parentRequestId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("{} error fetching requested item types", e.getMessage());
			throw new RequestedItemsFetchingException(
					messageBundleSource.getmessagebycode("requested.items.fetch.error", new Object[] {}));
		}
	}

	@Retryable(value = { CannotAcquireLockException.class }, maxAttempts = 2, backoff = @Backoff(delay = 1000))
	@Transactional(rollbackFor = Throwable.class)
	public Object assign(AssignPojo request) {
		try {
			return shkRepository.assign(request);
		} catch (StockNotAvailableException e) {
			logger.error("{} to the user {}", e.getMessage(), request.getEmpId());
			throw new StockNotAvailableException(e.getMessage());
		} catch (CannotAcquireLockException e) {
			logger.error("Concurrent exception while assigning items to the user {}", request.getEmpId());
			throw new CannotAcquireLockException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("{} error assigning items to user {}", e.getMessage(), request.getEmpId());
			throw new AssignItemsException(messageBundleSource.getmessagebycode("items.assign.error", new Object[] {}));
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<Map<String, Object>> assignHistory() {
		try {
			return shkRepository.assignHistory();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("{} error fetching assigned item history", e.getMessage());
			throw new AssignedItemsHistoryFetchingException(
					messageBundleSource.getmessagebycode("assigned.items.history.error", new Object[] {}));
		}
	}
}
