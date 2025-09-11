package com.hetero.heteroiconnect.couriertracker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hetero.heteroiconnect.promotion.exception.EmployeeNotFoundException;

 

@Service
public class CourierTrackingServiceImpl implements CourierTrackingService {

	@Autowired
	private CourierTrackingRepository courierTrackingRepository;

	@PersistenceContext
	private EntityManager entityManager;

//	@Override
//	public List<Map<String, Object>> getTypeOfCouriers() {
//		return courierTrackingRepository.getTypeOfCouriers();
//	}
//	
	public List<Map<String, Object>> getTypeOfCouriers() {
	    List<Object[]> results = courierTrackingRepository.getTypeOfCouriers();
	    List<Map<String, Object>> list = new ArrayList<>();
	    for (Object[] row : results) {
	        Map<String, Object> map = new HashMap<>();
	        map.put("courierTypeId", row[0]);
	        map.put("CourierName", row[1]);
	        list.add(map);
	    }
	    return list;
	}




//	@Override
//	public Object getByEmpid(int employeeseq) {
//	    List<Map<String, Object>> result = courierTrackingRepository.getEmployeeDetailsByEmpSeq(employeeseq);
//	    if (result.isEmpty()) {
//	        throw new EmployeeNotFoundException("Employee not found for employee sequence number: " + employeeseq);
//	    }
//	    return result;
//	}
	
	
	@Override
	public Object getByEmpid(int employeeseq) {
	    List<Object[]> result = courierTrackingRepository.getEmployeeDetailsByEmpSeq(employeeseq);
	    
	    if (result.isEmpty()) {
	        throw new EmployeeNotFoundException("Employee not found for employee sequence number: " + employeeseq);
	    }

	    List<Map<String, Object>> response = new ArrayList<>();
	    
	    for (Object[] row : result) {
	        Map<String, Object> map = new HashMap<>();
	        map.put("EmpCode", row[0]);
	        map.put("Fullname", row[1]);
	        map.put("employeeid", row[2]);
	        map.put("designationid", row[3]);
	        map.put("DESIGNATION", row[4]);
	        map.put("departmentid", row[5]);
	        map.put("DEPT", row[6]);
	        map.put("Divisionid", row[7]);
	        map.put("DIVISION", row[8]);
	        map.put("employmenttypeid", row[9]);
	        map.put("BANKIFC", row[10]);
	        map.put("BANKACC", row[11]);
	        map.put("bankName", row[12]);
	        response.add(map);
	    }
	    
	    return response;
	}

	
	@Override
	@Transactional
	public ResponseEntity<SuccessResponse> senderRegistration(CourierSenderTrackingDTO dto) {
		String docketNo = dto.getDocketNo();
		if (docketNo != null && !docketNo.trim().isEmpty()) {
			int existingCount = courierTrackingRepository.countByDocketNo(docketNo);

			if (existingCount > 0) {
				throw new DocketAlreadyFoundException("Docket No '" + docketNo + "' already exists.");
			}
			courierTrackingRepository.insertCourierSenderWithDocket(dto.getRegisterDate(), dto.getCourierTypeId(),
					docketNo, dto.getSenderName(), dto.getSenderContactNo(), dto.getReceiverName(),
					dto.getReceiverContactNo(), dto.getFromLocation(), dto.getToLocation(),
					 dto.getMaterial(), dto.getComment(), dto.getCreatedBy(), dto.getUpdatedBy());
		} else {
			courierTrackingRepository.insertCourierSenderWithoutDocket(dto.getRegisterDate(), dto.getCourierTypeId(),
					docketNo, dto.getSenderName(), dto.getSenderContactNo(), dto.getReceiverName(),
					dto.getReceiverContactNo(), dto.getFromLocation(), dto.getToLocation(),
					 dto.getMaterial(), dto.getComment(), dto.getCreatedBy());
		}

		SuccessResponse response = new SuccessResponse(200, "Courier Sender Data inserted successfully!");
		return ResponseEntity.ok(response);
	}

	@Override
	@Transactional
	public ResponseEntity<SuccessResponse> receiverRegistration(CourierReceiverTrackingDTO dto) {
		int existingCount = courierTrackingRepository.countByReceiverDocketNo(dto.getDocketNo());
		if (existingCount > 0) {
			throw new DocketAlreadyFoundException("Docket No '" + dto.getDocketNo() + "' already exists.");
		}

		courierTrackingRepository.insertCourierReceiverTracking(dto.getReceivedDate(), dto.getReceiverName(),dto.getCourierTypeId(),
				dto.getReceiverContactNo(), dto.getDocketNo(), dto.getSenderDetails(), 
				dto.getReceiverLocation(), dto.getMaterialReceived(), dto.getComment(), dto.getCreatedBy());

		SuccessResponse response = new SuccessResponse(200, "Receiver tracking saved successfully.");
		return ResponseEntity.ok(response);
	}

	@Override
	@Transactional
	public ResponseEntity<SuccessResponse> editSenderRegistration(CourierSenderTrackingDTO dto) {
		String oldDocketNo = courierTrackingRepository.findExistingDocketBySenderTrackingId(dto.getSenderTrackingId());
		String newDocketNo = dto.getDocketNo();

		if (newDocketNo != null && !newDocketNo.equalsIgnoreCase(oldDocketNo)) {
			int count = courierTrackingRepository.countSenderDocketNoExistsExceptCurrent(newDocketNo,
					dto.getSenderTrackingId());
			if (count > 0) {
				throw new DocketAlreadyFoundException("Docket No '" + newDocketNo + "' already exists.");
			}

			courierTrackingRepository.updateCourierSenderTrackingWithDocket(dto.getRegisterDate(),
					dto.getCourierTypeId(), newDocketNo, dto.getSenderName(), dto.getSenderContactNo(),
					dto.getReceiverName(), dto.getReceiverContactNo(), dto.getFromLocation(),
					dto.getToLocation(), dto.getMaterial(), dto.getComment(), dto.getUpdatedBy(),
					dto.getSenderTrackingId());

		} else {

			courierTrackingRepository.updateCourierSenderTrackingWithoutDocket(dto.getRegisterDate(),
					dto.getCourierTypeId(), dto.getSenderName(), dto.getSenderContactNo(), dto.getReceiverName(),
					dto.getReceiverContactNo(), dto.getFromLocation(), dto.getToLocation(),
				 dto.getMaterial(), dto.getComment(), dto.getSenderTrackingId());
		}

		return ResponseEntity.ok(new SuccessResponse(200, "Courier Sender Data edited successfully!"));
	}

	@Override
	@Transactional
	public ResponseEntity<SuccessResponse> editReceiverRegistration(CourierReceiverTrackingDTO dto) {
		if (dto.getDocketNo() != null && !dto.getDocketNo().isEmpty()) {
			int count = courierTrackingRepository.countReceiverDocketNoExistsExceptCurrent(dto.getDocketNo(),
					dto.getReceiverTrackingId());
			if (count > 0) {
				throw new DocketAlreadyFoundException("Docket No '" + dto.getDocketNo() + "' already exists.");
			}
		}
		courierTrackingRepository.updateCourierReceiverTracking(dto.getReceivedDate(), dto.getReceiverName(),dto.getCourierTypeId(),
				dto.getReceiverContactNo(), dto.getDocketNo(), dto.getSenderDetails(), 
				dto.getReceiverLocation(), dto.getMaterialReceived(), dto.getComment(), dto.getUpdatedBy(),
				 dto.getReceiverTrackingId());
		return ResponseEntity.ok(new SuccessResponse(200, "Receiver tracking updated successfully."));
	}

//	public Page<CourierSenderTrackingDTO> findByAllFields(String search, int page, int size) {
//	    Pageable pageable = new PageRequest(page, size, new Sort(Sort.Direction.DESC, "registerDate"));
//	    Page<Map<String, Object>> pageData = courierTrackingRepository.findByAllFields(search, pageable);
//
//	    return pageData.map(row -> {
//	        CourierSenderTrackingDTO dto = new CourierSenderTrackingDTO();
//	        dto.setSenderTrackingId((Integer) row.get("senderTrackingId"));
//	        dto.setRegisterDate((Date) row.get("registerDate"));
//	        dto.setCourierTypeId((Integer) row.get("courierTypeId"));
//	        dto.setCourierName((String) row.get("courierName"));
//	        dto.setDocketNo((String) row.get("docketNo"));
//	        dto.setTrackingStatusId((Integer) row.get("trackingStatusId"));
//	        dto.setSenderName((String) row.get("senderName"));
//	        dto.setDept((String) row.get("dept"));
//	        dto.setSenderId((String) row.get("senderId"));
//	        dto.setSenderContactNo((String) row.get("senderContactNo"));
//	        dto.setReceiverName((String) row.get("receiverName"));
//	        dto.setReceiverContactNo((String) row.get("receiverContactNo"));
//	        dto.setFromLocation((String) row.get("fromLocation"));
//	        dto.setToLocation((String) row.get("toLocation"));
//	        dto.setMaterial((String) row.get("material"));
//	        dto.setComment((String) row.get("comment"));
//	        return dto;
//	    });
//	}
	
	
	
 

	 
 
//	@Override
//	public Page<CourierReceiverTrackingDTO> getReceiverTrackingList(String search, int page, int size) {
//	    Pageable pageable = new PageRequest(page, size, new Sort(Sort.Direction.DESC, "receivedDate"));
//	    Page<Map<String, Object>> resultPage = courierTrackingRepository.findReceiverTrackingList(search, pageable);
//
//	    return resultPage.map(row -> {
//	        CourierReceiverTrackingDTO dto = new CourierReceiverTrackingDTO();
//	        dto.setReceiverTrackingId((Integer) row.get("receiverTrackingId"));
//	        dto.setReceivedDate((Date) row.get("receivedDate"));
//	        dto.setReceiverId((String) row.get("receiverId"));
//	        dto.setReceiverName((String) row.get("receiverName"));
//	        dto.setCourierTypeId((Integer) row.get("courierTypeId"));
//	        dto.setCourierName((String) row.get("courierName"));
//	        dto.setDept((String) row.get("dept"));
//	        dto.setReceiverContactNo((String) row.get("receiverContactNo"));
//	        dto.setDocketNo((String) row.get("docketNo"));
//	        dto.setSenderDetails((String) row.get("senderDetails"));
//	        dto.setReceiverLocation((String) row.get("receiverLocation"));
//	        dto.setMaterialReceived((String) row.get("materialReceived"));
//	        dto.setComment((String) row.get("comment"));
//	        dto.setCreatedBy((String) row.get("createdBy"));
//	        dto.setUpdatedBy((String) row.get("updatedBy"));
//	        return dto;
//	    });
//	}

  

	public Page<CourierReceiverTrackingDTO> getReceiverTrackingList(String search, int page, int size,int loginid) {
	    int offset = page * size;
 
	    String baseSql = "SELECT a.receiver_tracking_id, a.received_date, a.receiver_name, ep.callname, d.name, " +
	        "a.receiver_contact_no, a.receiver_location, a.docket_no, a.sender_details, a.material_received, a.comment, " +
	        "a.created_by, a.updated_by, a.courier_type_id, ct.courier_name " +
	        "FROM courier_tracking.tbl_courier_receiver_tracking a " +
	        "LEFT JOIN courier_tracking.tbl_master_courier_types ct ON a.courier_type_id = ct.courier_type_id " +
	        "LEFT JOIN hclhrm_prod.tbl_employee_primary ep ON ep.employeesequenceno = a.receiver_name " +
	        "LEFT JOIN hclhrm_prod.tbl_employee_professional_details pro ON ep.employeeid = pro.employeeid " +
	        "LEFT JOIN hcladm_prod.tbl_department d ON pro.departmentid = d.departmentid " +
	        "WHERE a.status = 1001 AND a.created_by = :loginid ";
 
	    String searchCondition = "";
	    if (search != null && !search.trim().isEmpty()) {
	        searchCondition = "AND (" +
	            "ep.callname LIKE :search OR d.name LIKE :search OR a.receiver_contact_no LIKE :search OR a.receiver_location LIKE :search OR " +
	            "a.docket_no LIKE :search OR a.sender_details LIKE :search OR a.material_received LIKE :search OR a.comment LIKE :search OR " +
	            "ct.courier_name LIKE :search OR CAST(a.received_date AS CHAR) LIKE :search) ";
	    }
 
	    String orderLimit = "ORDER BY a.received_date DESC LIMIT :limit OFFSET :offset";
 
	    String finalSql = baseSql + searchCondition + orderLimit;
 
	    String countSql = "SELECT COUNT(*) FROM courier_tracking.tbl_courier_receiver_tracking a " +
	        "LEFT JOIN courier_tracking.tbl_master_courier_types ct ON a.courier_type_id = ct.courier_type_id " +
	        "LEFT JOIN hclhrm_prod.tbl_employee_primary ep ON ep.employeesequenceno = a.receiver_name " +
	        "LEFT JOIN hclhrm_prod.tbl_employee_professional_details pro ON ep.employeeid = pro.employeeid " +
	        "LEFT JOIN hcladm_prod.tbl_department d ON pro.departmentid = d.departmentid " +
	        "WHERE  a.status = 1001 AND a.created_by = :loginid " + searchCondition;
 
	    Query countQuery = entityManager.createNativeQuery(countSql);
	    Query dataQuery = entityManager.createNativeQuery(finalSql);
	    countQuery.setParameter("loginid", loginid);
        dataQuery.setParameter("loginid", loginid);
	    if (!searchCondition.isEmpty()) {
	        String searchPattern = "%" + search.trim() + "%";
	        countQuery.setParameter("search", searchPattern);
	        dataQuery.setParameter("search", searchPattern);
	    }
 
	    dataQuery.setParameter("limit", size);
	    dataQuery.setParameter("offset", offset);
 
	    Number total = (Number) countQuery.getSingleResult();
 
	    @SuppressWarnings("unchecked")
	    List<Object[]> results = dataQuery.getResultList();
 
	    List<CourierReceiverTrackingDTO> dtos = new ArrayList<>();
	    for (Object[] row : results) {
	        CourierReceiverTrackingDTO dto = new CourierReceiverTrackingDTO();
	        dto.setReceiverTrackingId(((Number) row[0]).intValue());
	        dto.setReceivedDate((Date) row[1]);
	        dto.setReceiverId((String) row[2]);
	        dto.setReceiverName((String) row[3]);
	        dto.setDept((String) row[4]);
	        dto.setReceiverContactNo((String) row[5]);
	        dto.setReceiverLocation((String) row[6]);
	        dto.setDocketNo((String) row[7]);
	        dto.setSenderDetails((String) row[8]);
	        dto.setMaterialReceived((String) row[9]);
	        dto.setComment((String) row[10]);
	        dto.setCreatedBy((String) row[11]);
	        dto.setUpdatedBy((String) row[12]);
	        dto.setCourierTypeId(row[13] != null ? ((Number) row[13]).intValue() : null);
	        dto.setCourierName((String) row[14]);
	        dtos.add(dto);
	    }
 
	    Pageable pageable = new PageRequest(page, size, new Sort(Sort.Direction.DESC, "receivedDate"));
	    return new PageImpl<>(dtos, pageable, total.longValue());
	}
	
	
	
	 public Page<CourierSenderTrackingDTO> findByAllFields(String search, int page, int size, int loginid) {
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Page must be non-negative and size must be positive");
        }
 
        try {
            int offset = page * size;
            String baseSql = "SELECT a.sender_tracking_id, a.register_date, a.courier_type_id, b.courier_name, a.docket_no, " +
                    "a.tracking_status_id, ep.callname, d.name, a.sender_name AS senderId, a.sender_contact_no, " +
                    "a.receiver_name, a.receiver_contact_no, a.from_location, a.to_location, a.material, a.comment " +
                    "FROM courier_tracking.tbl_courier_sender_tracking a " +
                    "LEFT JOIN courier_tracking.tbl_master_courier_types b ON a.courier_type_id = b.courier_type_id " +
                    "LEFT JOIN hclhrm_prod.tbl_employee_primary ep ON ep.employeesequenceno = a.sender_name " +
                    "LEFT JOIN hclhrm_prod.tbl_employee_professional_details pro ON ep.employeeid = pro.employeeid " +
                    "LEFT JOIN hcladm_prod.tbl_department d ON pro.departmentid = d.departmentid " +
                    "WHERE a.status = 1001 AND a.created_by = :loginid " +
                    (search != null && !search.isEmpty() ? 
                        "AND (a.docket_no LIKE :search OR ep.callname LIKE :search OR d.name LIKE :search " +
                        "OR a.sender_contact_no LIKE :search OR a.receiver_name LIKE :search " +
                        "OR a.receiver_contact_no LIKE :search OR a.from_location LIKE :search " +
                        "OR a.to_location LIKE :search OR a.material LIKE :search OR b.courier_name LIKE :search " +
                        "OR a.tracking_status_id LIKE :search) " : "") +
                    "ORDER BY a.register_date DESC LIMIT :limit OFFSET :offset";
 
            String countSql = "SELECT COUNT(*) FROM courier_tracking.tbl_courier_sender_tracking a " +
                    "LEFT JOIN courier_tracking.tbl_master_courier_types b ON a.courier_type_id = b.courier_type_id " +
                    "LEFT JOIN hclhrm_prod.tbl_employee_primary ep ON ep.employeesequenceno = a.sender_name " +
                    "LEFT JOIN hclhrm_prod.tbl_employee_professional_details pro ON ep.employeeid = pro.employeeid " +
                    "LEFT JOIN hcladm_prod.tbl_department d ON pro.departmentid = d.departmentid " +
                    "WHERE a.status = 1001 AND a.created_by = :loginid " +
                    (search != null && !search.isEmpty() ? 
                        "AND (a.docket_no LIKE :search OR ep.callname LIKE :search OR d.name LIKE :search " +
                        "OR a.sender_contact_no LIKE :search OR a.receiver_name LIKE :search " +
                        "OR a.receiver_contact_no LIKE :search OR a.from_location LIKE :search " +
                        "OR a.to_location LIKE :search OR a.material LIKE :search OR b.courier_name LIKE :search " +
                        "OR a.tracking_status_id LIKE :search) " : "");
 
            Query countQuery = entityManager.createNativeQuery(countSql);
            Query dataQuery = entityManager.createNativeQuery(baseSql);
            countQuery.setParameter("loginid", loginid);
            dataQuery.setParameter("loginid", loginid);
            if (search != null && !search.isEmpty()) {
                String searchPattern = "%" + search + "%";
                countQuery.setParameter("search", searchPattern);
                dataQuery.setParameter("search", searchPattern);
            }
            dataQuery.setParameter("limit", size);
            dataQuery.setParameter("offset", offset);
            Number totalCount = (Number) countQuery.getSingleResult();
            @SuppressWarnings("unchecked")
            List<Object[]> results = dataQuery.getResultList();
            List<CourierSenderTrackingDTO> dtoList = new ArrayList<>();
            for (Object[] row : results) {
                CourierSenderTrackingDTO dto = new CourierSenderTrackingDTO();
                dto.setSenderTrackingId(row[0] != null ? ((Number) row[0]).intValue() : null);
                dto.setRegisterDate(row[1] != null ? (Date) row[1] : null);
                dto.setCourierTypeId(row[2] != null ? ((Number) row[2]).intValue() : null);
                dto.setCourierName((String) row[3]);
                dto.setDocketNo((String) row[4]);
                dto.setTrackingStatusId(row[5] != null ? ((Number) row[5]).intValue() : null);
                dto.setSenderName((String) row[6]);
                dto.setDept((String) row[7]);
                dto.setSenderId((String) row[8]);
                dto.setSenderContactNo((String) row[9]);
                dto.setReceiverName((String) row[10]);
                dto.setReceiverContactNo((String) row[11]);
                dto.setFromLocation((String) row[12]);
                dto.setToLocation((String) row[13]);
                dto.setMaterial((String) row[14]);
                dto.setComment((String) row[15]);
                dtoList.add(dto);
            }
            Pageable pageable = new PageRequest(page, size, new Sort(Sort.Direction.DESC, "registerDate"));
            return new PageImpl<CourierSenderTrackingDTO>(dtoList, pageable, totalCount != null ? totalCount.longValue() : 0);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching courier tracking data: " + e.getMessage(), e);
        }
    }


//	@Override
//	public Object getSearch(String name) {
//		return courierTrackingRepository.searchEmployeesByName(name);
//	}
	 
	@Override
	public List<Map<String, Object>> getSearch(String name) {
	    List<Object[]> results = courierTrackingRepository.searchEmployeesByName(name);
	    List<Map<String, Object>> list = new ArrayList<>();
	    
	    for (Object[] row : results) {
	        Map<String, Object> map = new HashMap<>();
	        map.put("id", row[0]);
	        map.put("name", row[1]);
	        list.add(map);
	    }
	    
	    return list;
	}

	@Override
	public synchronized byte[] courierTrackingService(int loginid) {
	    List<Object[]> data = courierTrackingRepository.findAllSenderDataForExport(loginid);

	    if (data == null || data.isEmpty()) {
	        throw new EmptyDataNotFoundException("No sender tracking data found.");
	    }

	    XSSFWorkbook workbook = new XSSFWorkbook();
	    ByteArrayOutputStream out = new ByteArrayOutputStream();

	    try {
	        XSSFSheet sheet = workbook.createSheet("Sender Tracking");

	        XSSFCellStyle headerStyle = workbook.createCellStyle(); 
	        XSSFFont font = workbook.createFont();
	        font.setBold(true);
	        headerStyle.setFont(font);
	        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
	        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

	        String[] headers = new String[] {
	            "Registered Date", "Courier Name", "Docket No", "Sender Name", "Department",
	            "Sender ContactNo", "Receiver Name", "Receiver ContactNo",
	            "From Location", "To Location", "Material", "Comment"
	        };

	        // Create header row
	        Row headerRow = sheet.createRow(0);
	        for (int i = 0; i < headers.length; i++) {
	            Cell cell = headerRow.createCell(i);
	            cell.setCellValue(headers[i]);
	            cell.setCellStyle(headerStyle);
	        }    

	        int rowIndex = 1;
	        for (Object[] record : data) { 
	            Row row = sheet.createRow(rowIndex++);
	            for (int col = 0; col < headers.length; col++) {
	                Object value = (col < record.length) ? record[col] : null;
	                String cellValue = (value != null) ? value.toString() : "";
	                row.createCell(col).setCellValue(cellValue);
	            }
	        }

	        workbook.write(out);
	        return out.toByteArray();

	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    } finally {
	        try {
	            workbook.close();
	            out.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}

  
	
//	@Override
//	public synchronized byte[] courierTrackingService() {
//	    List<Map<String, Object>> data = courierTrackingRepository.findAllSenderDataForExport();
//	    if (data == null || data.isEmpty()) {
//	        throw new EmptyDataNotFoundException("No sender tracking data found.");
//	    }
//	    try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
//	        XSSFSheet sheet = workbook.createSheet("Sender Tracking");
//	        XSSFCellStyle headerStyle = workbook.createCellStyle();
//	        XSSFFont font = workbook.createFont();
//	        font.setBold(true);
//	        headerStyle.setFont(font);
//	        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
//	        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//
//	        String[] headers = {
//	            "Registered Date", "Courier Name", "Department", "Docket No", "Sender Name",
//	            "Sender ContactNo", "Receiver Name", "Receiver ContactNo",
//	            "From Location", "To Location", "Material", "Comment"
//	        };
//	        Row headerRow = sheet.createRow(0);
//	        IntStream.range(0, headers.length).forEach(i -> {
//	            Cell cell = headerRow.createCell(i);
//	            cell.setCellValue(headers[i]);
//	            cell.setCellStyle(headerStyle);
//	        });
//	        AtomicInteger rowIndex = new AtomicInteger(1);
//	        data.forEach(record -> {
//	            Row row = sheet.createRow(rowIndex.getAndIncrement());
//	            IntStream.range(0, headers.length).forEach(col -> {
//	                Object value = record.get(headers[col]);
//	                row.createCell(col).setCellValue(value != null ? value.toString() : "");
//	            });
//	        });
//
//	        workbook.write(out);
//	        return out.toByteArray();
//
//	    } catch (IOException e) {
//	        e.printStackTrace();
//	        return null;
//	    }
//	}
//	@Override
//	public synchronized byte[] receiverTrackingReport() {
//	    List<Map<String, Object>> data = courierTrackingRepository.findAllReceiverDataForExport();
//	    if (data == null || data.isEmpty()) {
//	        throw new EmptyDataNotFoundException("No Receiver tracking data found.");
//	    }
//	    try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
//	        XSSFSheet sheet = workbook.createSheet("Receiver Tracking");
//	        XSSFCellStyle headerStyle = workbook.createCellStyle();
//	        XSSFFont font = workbook.createFont();
//	        font.setBold(true);
//	        headerStyle.setFont(font);
//	        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
//	        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//
//	        String[] headers = {
//	            "Received Date", "Receiver Name", "Department", "Receiver ContactNo",
//	            "Receiver Location", "Docket No", "Sender Details",
//	            "Material Received", "comment"
//	        };
//	        Row headerRow = sheet.createRow(0);
//	        IntStream.range(0, headers.length).forEach(i -> {
//	            Cell cell = headerRow.createCell(i);
//	            cell.setCellValue(headers[i]);
//	            cell.setCellStyle(headerStyle);
//	        });
//	        AtomicInteger rowIndex = new AtomicInteger(1);
//	        data.forEach(record -> {
//	            Row row = sheet.createRow(rowIndex.getAndIncrement());
//	            IntStream.range(0, headers.length).forEach(col -> {
//	                Object value = record.get(headers[col]);
//	                row.createCell(col).setCellValue(value != null ? value.toString() : "");
//	            });
//	        });
//  
//	        workbook.write(out);
//	        return out.toByteArray();
//
//	    } catch (IOException e) {
//	        e.printStackTrace();
//	        return null;
//	    }
//	}
	
	public synchronized byte[] receiverTrackingReport(int loginid) {
	    List<Object[]> data = courierTrackingRepository.findAllReceiverDataForExport(loginid);

	    if (data == null || data.isEmpty()) {
	        throw new EmptyDataNotFoundException("No Receiver tracking data found.");
	    }

	    try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
	        XSSFSheet sheet = workbook.createSheet("Receiver Tracking");

	        XSSFCellStyle headerStyle = workbook.createCellStyle();
	        XSSFFont font = workbook.createFont();
	        font.setBold(true);
	        headerStyle.setFont(font);
	        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
	        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

	        String[] headers = {
	            "Received Date", "Receiver Name", "Department", "Receiver ContactNo",
	            "Receiver Location", "Docket No", "Sender Details",
	            "Material Received", "Comment"
	        };

	        // Header Row
	        Row headerRow = sheet.createRow(0);
	        for (int i = 0; i < headers.length; i++) {
	            Cell cell = headerRow.createCell(i);
	            cell.setCellValue(headers[i]);
	            cell.setCellStyle(headerStyle);
	        }

	        int rowIndex = 1;
	        for (Object[] record : data) {
	            Row row = sheet.createRow(rowIndex++);
	            for (int col = 0; col < headers.length; col++) {
	                Object value = (col < record.length) ? record[col] : null;
	                row.createCell(col).setCellValue(value != null ? value.toString() : "");
	            }
	        }

	        workbook.write(out);
	        return out.toByteArray();

	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	}




}
