package com.hetero.heteroiconnect.touradvance;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.hetero.heteroiconnect.promotion.exception.EmployeeNotFoundException;
import com.hetero.heteroiconnect.zonedetails.EmptyDataNotFoundException;

@Service
public class TourServiceImpl {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Value("${tour.advancebill.upload.path}")
	private String advanceBillUploadPath;

	@Value("${tour.utilizationbil.upload.path}")
	private String utilizationUploadPath;

	@Value("${tour.conveyance_bill.upload.path}")
	private String billUploadPath;

	@Value("${tour.conveyance_cheque.upload.path}")
	private String chequeUploadPath;

	public Object getModeOfTravel() {
		String sql = "select id,name from touradvance.tbl_master_mode_of_travel where status=1001";
		return jdbcTemplate.queryForList(sql);
	}

	public Object getTourType() {
		String sql = "select id,name from touradvance.tbl_master_tour_type where status=1001";
		return jdbcTemplate.queryForList(sql);
	}

	public Object getAmountMode() {
		String sql = "select id,mode_name from touradvance.tbl_master_amount_mode where status=1001";
		return jdbcTemplate.queryForList(sql);
	}

	public boolean isOverlappingTour(int employeeId, LocalDate from, LocalDate to) {
		String sql = "SELECT COUNT(*) FROM touradvance.tbl_employee_tour_advance "
				+ "WHERE employeeid = ? AND (Date_of_travel <= ? AND Date_of_return >= ?)";
		Integer count = jdbcTemplate.queryForObject(sql, Integer.class, employeeId, to, from);
		return count != null && count > 0;
	}

	public Integer getExistingTourId(int employeeId, LocalDate Date_of_travel) {
		String sql = "SELECT tour_id FROM touradvance.tbl_employee_tour_advance WHERE employeeid = ? AND Date_of_travel = ?";
		List<Integer> ids = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("tour_id"), employeeId, Date_of_travel);
		return ids.isEmpty() ? null : ids.get(0);
	}

	public Object getBookingsMode() {
		String sql = "select booking_id,name from touradvance.tbl_master_modeofbooking where status=1001";
		return jdbcTemplate.queryForList(sql);
	}

	public Object getSettlementMode() {
		String sql = "select settlement_id,name from  touradvance.tbl_master_modeofsettlement where status=1001";
		return jdbcTemplate.queryForList(sql);
	}

	public Object getBillStatus() {
		String sql = "select * from  touradvance.bill_status";
		return jdbcTemplate.queryForList(sql);
	}

	public int insertOrGetTourAdvance(EmployeesTourDetailsDTO dto) {
		String sql = "INSERT INTO touradvance.tbl_employee_tour_advance "
				+ "(employeeid, employee_name, Department, Designation, mode_of_travel, purpose_of_travel, travel_from, travel_to, Date_of_travel, Date_of_return, Requested_amount, current_location, contact_no, email, bank_account_no, bank_name, ifsc_code, cash_mode,fare_amount, cash_recipient_name, cheque_number, hod_empid, tour_type, createdBy,Mode_of_booking,Adhar_no,service_charge,prev_bill_status,advance_bill_file) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, dto.getEmployeeId());
			ps.setString(2, dto.getEmployeeName());
			ps.setString(3, dto.getDepartment());
			ps.setString(4, dto.getDesignation());
			ps.setString(5, dto.getModeOfTravel());
			ps.setString(6, dto.getPurposeOfTravel());
			ps.setString(7, dto.getTravelFrom());
			ps.setString(8, dto.getTravelTo());
			ps.setDate(9, java.sql.Date.valueOf(dto.getDateOfTravel()));
			ps.setDate(10, java.sql.Date.valueOf(dto.getDateOfReturn()));
			ps.setBigDecimal(11, dto.getRequestedAmount());
			ps.setString(12, dto.getCurrentLocation());
			ps.setString(13, dto.getContactNo());
			ps.setString(14, dto.getEmail());
			ps.setString(15, dto.getBankAccountNo());
			ps.setString(16, dto.getBankName());
			ps.setString(17, dto.getIfscCode());
			ps.setString(18, dto.getCashMode());
			ps.setBigDecimal(19, dto.getFareAmount());
			ps.setString(20, dto.getCashRecipientName());
			ps.setString(21, dto.getChequeNumber());
			ps.setInt(22, dto.getHodEmpid());
			ps.setString(23, dto.getTourType());
			ps.setString(24, dto.getCreatedBy());
			ps.setString(25, dto.getModeOfBooking());
			ps.setString(26, dto.getAdharNo());
			ps.setBigDecimal(27, dto.getServiceCharge());
			ps.setString(28, dto.getBillStatus());
			ps.setString(29, dto.getAdvanceBillFile());

			return ps;
		}, keyHolder);

		return keyHolder.getKey().intValue();
	}

	public void insertOrUpdateTourTeamMembers(List<EmployeeTourTeamMemberDTO> members, int tourId) {
		String sql = "INSERT INTO touradvance.tbl_employee_tour_team_members (employeeid, employeeName, Designation, Department, tour_id) "
				+ "VALUES (?, ?, ?, ?, ?) " + "ON DUPLICATE KEY UPDATE " + "employeeName = VALUES(employeeName), "
				+ "Designation = VALUES(Designation), " + "Department = VALUES(Department)";

		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				EmployeeTourTeamMemberDTO member = members.get(i);
				ps.setInt(1, member.getEmployeeId());
				ps.setString(2, member.getEmployeeName());
				ps.setString(3, member.getDesignation());
				ps.setString(4, member.getDepartment());
				ps.setInt(5, tourId);
			}

			@Override
			public int getBatchSize() {
				return members.size();
			}
		});
	}

	public int saveTourDetails(EmployeeTourDTO tourDTO, boolean forceInsert, MultipartFile advance_bill_file) {
		EmployeesTourDetailsDTO dto = tourDTO.getEmployeesTourDetailsDTO();

		if (!forceInsert && isOverlappingTour(dto.getEmployeeId(), dto.getDateOfTravel(), dto.getDateOfReturn())) {
			return -1;
		}

		if (advance_bill_file != null && !advance_bill_file.isEmpty()) {
			try {
				File directory = new File(advanceBillUploadPath);
				if (!directory.exists()) {
					directory.mkdirs();
				}
				String originalFilename = advance_bill_file.getOriginalFilename();
				String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
				String fileName = "tour_" + dto.getEmployeeId() + "_" + System.currentTimeMillis() + fileExtension;
				Path filePath = Paths.get(advanceBillUploadPath, fileName);
				Files.write(filePath, advance_bill_file.getBytes());
				dto.setAdvanceBillFile(filePath.toString());

			} catch (IOException e) {
				throw new RuntimeException("Failed to store advance bill file", e);
			}
		}

		int tourId = insertOrGetTourAdvance(dto);

		List<EmployeeTourTeamMemberDTO> members = tourDTO.getEmployeeTourTeamMemberDTO();
		if (members != null && !members.isEmpty()) {
			insertOrUpdateTourTeamMembers(members, tourId);
		}

		return tourId;
	}

	public Object getByEmpid(int employeeseq) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ").append("a.employeesequenceno AS EmpCode, ").append("a.callname AS Fullname, ")
				.append("a.employeeid, ").append("b.designationid, ").append("e.name AS DESIGNATION, ")
				.append("b.departmentid, ").append("d.name AS DEPT, ").append("a.companyid AS Divisionid, ")
				.append("f.name AS DIVISION, ").append("g.employmenttypeid, ").append("h.BANKIFC, ")
				.append("h.BANKACC, ").append("i.bankName, ").append("j.email, ").append("j.mobile, ") // Added missing
																										// comma
				.append("k.AADHAARCARDNO ").append("FROM hclhrm_prod.tbl_employee_primary a ")
				.append("LEFT JOIN hclhrm_prod.tbl_employee_professional_details b ON a.employeeid = b.employeeid ")
				.append("LEFT JOIN hclhrm_prod.tbl_employee_profile_businessunit c ON c.employeeid = b.employeeid ")
				.append("LEFT JOIN hcladm_prod.tbl_department d ON d.departmentid = b.departmentid ")
				.append("LEFT JOIN hcladm_prod.tbl_designation e ON e.designationid = b.designationid ")
				.append("LEFT JOIN hcladm_prod.tbl_businessunit f ON f.businessunitid = a.companyid ")
				.append("LEFT JOIN hclhrm_prod.tbl_employment_types g ON g.employmenttypeid = a.employmenttypeid ")
				.append("LEFT JOIN hclhrm_prod_others.tbl_emp_bank_ifc h ON h.empid = b.employeeid ")
				.append("LEFT JOIN hcladm_prod.tbl_bank_details i ON i.bankid = h.bankid ")
				.append("LEFT JOIN hclhrm_prod.tbl_employee_professional_contact j ON j.employeeid = a.employeeid ")
				.append("LEFT JOIN hclhrm_prod.tbl_employee_personalinfo k ON k.employeeid = a.employeeid ")
				.append("WHERE a.employeesequenceno = ? AND a.status = 1001");

		List<Map<String, Object>> result = jdbcTemplate.queryForList(query.toString(), employeeseq);

		if (result.isEmpty()) {
			throw new EmployeeNotFoundException("Employee not found for employee sequence number: " + employeeseq);
		}

		return result;
	}

	public List<Master> getHodName(String name) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT employeesequenceno, callname ").append("FROM hclhrm_prod.tbl_employee_primary ")
				.append("WHERE employeeid NOT IN (0, 1) ").append("AND status IN (1001, 1061, 1092, 1401) ")
				.append("AND (callname LIKE ? OR employeesequenceno LIKE ?)").append("LIMIT 10");
		return jdbcTemplate.query(query.toString(), (rs, rowNum) -> {
			Master dependentName = new Master();
			dependentName.setId(rs.getInt("employeesequenceno"));
			dependentName.setName(rs.getString("callname"));
			return dependentName;
		}, "%" + name + "%", "%" + name + "%");
	}
//
//	public Map<String, Object> getTourEmployees(int page, int size, String searchTerm,int loginid) {
//		int offset = page * size;
//
//		String baseSql = "FROM touradvance.tbl_employee_tour_advance WHERE status = 1001";
//		List<Object> params = new ArrayList<>();
//
//		if (searchTerm != null && !searchTerm.trim().isEmpty()) {
//			baseSql += " AND (employeeid LIKE ? OR employee_name LIKE ?)";
//			String likePattern = "%" + searchTerm.trim() + "%";
//			params.add(likePattern);
//			params.add(likePattern);
//		}
//
//		String dataSql = "SELECT tour_id, employeeid, employee_name, Requested_amount " + baseSql
//				+ " ORDER BY createdDate DESC LIMIT ? OFFSET ?";
//		params.add(size);
//		params.add(offset);
//
//		List<Map<String, Object>> content = jdbcTemplate.queryForList(dataSql, params.toArray());
//		String countSql = "SELECT COUNT(*) " + baseSql;
//		List<Object> countParams = new ArrayList<>(params);
//		if (countParams.size() >= 2) {
//			countParams = countParams.subList(0, countParams.size() - 2);
//		}
//
//		int total = jdbcTemplate.queryForObject(countSql, countParams.toArray(), Integer.class);
//
//		Map<String, Object> result = new HashMap<>();
//		result.put("content", content);
//		result.put("page", page);
//		result.put("size", size);
//		result.put("totalElements", total);
//		result.put("totalPages", (int) Math.ceil((double) total / size));
//
//		return result;
//	}
	public Map<String, Object> getTourEmployees(int page, int size, String searchTerm, int loginid) {
	    int offset = page * size;

	    String baseSql = "FROM touradvance.tbl_employee_tour_advance WHERE status = 1001 AND createdBy = ?";
	    List<Object> params = new ArrayList<>();
	    params.add(loginid);

	    if (searchTerm != null && !searchTerm.trim().isEmpty()) {
	        baseSql += " AND (employeeid LIKE ? OR employee_name LIKE ?)";
	        String likePattern = "%" + searchTerm.trim() + "%";
	        params.add(likePattern);
	        params.add(likePattern);
	    }

	    String dataSql = "SELECT tour_id, employeeid, employee_name, Requested_amount "
	                   + baseSql
	                   + " ORDER BY createdDate DESC LIMIT ? OFFSET ?";
	    params.add(size);
	    params.add(offset);

	    List<Map<String, Object>> content = jdbcTemplate.queryForList(dataSql, params.toArray());

	    String countSql = "SELECT COUNT(*) " + baseSql;
	    List<Object> countParams = new ArrayList<>(params);
	    if (countParams.size() >= 2) {
	        countParams = countParams.subList(0, countParams.size() - 2);
	    }

	    int total = jdbcTemplate.queryForObject(countSql, countParams.toArray(), Integer.class);

	    Map<String, Object> result = new HashMap<>();
	    result.put("content", content);
	    result.put("page", page);
	    result.put("size", size);
	    result.put("totalElements", total);
	    result.put("totalPages", (int) Math.ceil((double) total / size));

	    return result;
	}

	public ResponseEntity<ApiResponse> saveOrUpdateSettlement(TourUtilizationDTO dto, MultipartFile utilizationbill) {
		try {
			String fileName = null;
			if (utilizationbill != null && !utilizationbill.isEmpty()) {
				String originalFileName = utilizationbill.getOriginalFilename();
				String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
				String fileNameGen = "utilization_" + dto.getTourId() + "_" + System.currentTimeMillis()
						+ fileExtension;
				Path filePath = Paths.get(utilizationUploadPath, fileNameGen);

				// Ensure directory exists using mkdirs()
				File uploadDir = new File(utilizationUploadPath);
				if (!uploadDir.exists()) {
					boolean dirCreated = uploadDir.mkdirs();
					if (!dirCreated) {
						throw new IOException("Failed to create directory: " + utilizationUploadPath);
					}
				}

				Files.copy(utilizationbill.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
				fileName = filePath.toString();
			} else {
				System.out.println("No file uploaded or file is empty.");
			}

			String sql = "INSERT INTO touradvance.tbl_tour_advance_settlement "
					+ "(tour_id, utilized_amount, adjustment, mode_of_settlement, receipt_file, comment, uploadedBy, status) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, 1002) " + "ON DUPLICATE KEY UPDATE "
					+ "utilized_amount = VALUES(utilized_amount), " + "adjustment = VALUES(adjustment), "
					+ "receipt_file = VALUES(receipt_file), " + "comment = VALUES(comment), "
					+ "uploadedBy = VALUES(uploadedBy), " + "status = VALUES(status), "
					+ "uploadedDate = CURRENT_TIMESTAMP";

			jdbcTemplate.update(sql, dto.getTourId(), dto.getUtilizedAmount(), dto.getAdjustment(),
					dto.getModeOfSettlement(), fileName, dto.getComment(), dto.getUploadedBy());

			String updateAdvanceStatus = "UPDATE touradvance.tbl_employee_tour_advance SET status = 1002 WHERE tour_id = ? AND status = 1001";
			jdbcTemplate.update(updateAdvanceStatus, dto.getTourId());

			return ResponseEntity
					.ok(new ApiResponse("success", "Settlement saved or updated successfully", dto.getTourId()));

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiResponse("error", "Database/File error: " + e.getMessage(), null));
		}
	}

	public ResponseEntity<Object> getFetchById(int tourid) {
		String tourQuery = "SELECT a.tour_id, a.employeeid, a.employee_name, a.Department, a.Designation,a.Adhar_no,a.service_charge, f.booking_id,"
				+ "b.name AS ModeOfTravel, a.purpose_of_travel, a.travel_from, a.travel_to, a.fare_amount, "
				+ "a.Date_of_travel, a.Date_of_return, a.Requested_amount, a.current_location, a.contact_no, "
				+ "a.email, a.bank_account_no, a.bank_name, a.ifsc_code, a.cash_recipient_name, a.hod_empid, "
				+ "e.utilized_amount, e.adjustment, e.comment, e.uploadedBy, c.mode_name as cash_mode,a.cash_mode as cash_modeid, "
				+ "d.name as tour_type, a.createdBy, IFNULL(a.Mode_of_booking, 0) as Mode_of_booking, "
				+ "g.name as settlement ,f.name as Mode_of_bookingName,h.name as billStatus "
				+ "FROM touradvance.tbl_employee_tour_advance a "
				+ "LEFT JOIN touradvance.tbl_master_mode_of_travel b ON a.mode_of_travel = b.id "
				+ "LEFT JOIN touradvance.tbl_master_amount_mode c ON a.cash_mode = c.id "
				+ "LEFT JOIN touradvance.tbl_master_tour_type d ON a.tour_type = d.id "
				+ "LEFT JOIN touradvance.tbl_tour_advance_settlement e ON a.tour_id = e.tour_id "
				+ "LEFT JOIN touradvance.tbl_master_modeofbooking f ON a.Mode_of_booking = f.booking_id "
				+ "LEFT JOIN touradvance.tbl_master_modeofsettlement g ON e.mode_of_settlement = g.settlement_id "
				+ "LEFT JOIN touradvance.bill_status h ON h.id= a.prev_bill_status " + "WHERE a.tour_id = ?";

		List<EmployeesTourDetailsDTO> advanceList = jdbcTemplate.query(tourQuery,
				new BeanPropertyRowMapper<>(EmployeesTourDetailsDTO.class), tourid);
		if (advanceList.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No tour found for ID: " + tourid);
		}
		EmployeesTourDetailsDTO advanceDTO = advanceList.get(0);
		String teamQuery = "SELECT member_id, employeeid, employeeName, Designation, Department, tour_id "
				+ "FROM touradvance.tbl_employee_tour_team_members WHERE tour_id = ?";
		List<EmployeeTourTeamMemberDTO> teamList = jdbcTemplate.query(teamQuery,
				new BeanPropertyRowMapper<>(EmployeeTourTeamMemberDTO.class), tourid);
		EmployeeTourDTO result = new EmployeeTourDTO();
		result.setEmployeesTourDetailsDTO(advanceDTO);
		result.setEmployeeTourTeamMemberDTO(teamList);
		return ResponseEntity.ok(result);
	}

	public byte[] getExcelDownload(String fromDate, String toDate,int loginid) throws IOException {
		String tourQuery = "SELECT a.tour_id, a.employeeid, a.employee_name, a.Department, a.Designation, "
				+ "b.name AS modeOfTravel, a.purpose_of_travel, a.travel_from, a.travel_to, "
				+ "a.Date_of_travel, a.Date_of_return, a.Requested_amount, a.current_location, "
				+ "a.contact_no, a.email, a.hod_empid, e.utilized_amount, e.adjustment, "
				+ "c.mode_name as cash_mode, a.fare_amount, d.name as tour_type, " + "h.name as billStatus, e.comment "
				+ "FROM touradvance.tbl_employee_tour_advance a "
				+ "LEFT JOIN touradvance.tbl_master_mode_of_travel b ON a.mode_of_travel = b.id "
				+ "LEFT JOIN touradvance.tbl_master_amount_mode c ON a.cash_mode = c.id "
				+ "LEFT JOIN touradvance.tbl_master_tour_type d ON a.tour_type = d.id "
				+ "LEFT JOIN touradvance.tbl_tour_advance_settlement e ON a.tour_id = e.tour_id "
				+ "LEFT JOIN touradvance.bill_status h ON h.id = a.prev_bill_status "
				+ "WHERE a.createdDate >= ? AND a.createdDate <= ? AND a.createdBy=?";

		List<Map<String, Object>> tourList = jdbcTemplate.queryForList(tourQuery, fromDate, toDate,loginid);

		if (tourList.isEmpty()) {
			throw new EmptyDataNotFoundException("No tour data found for the selected period.");
		}

		Workbook workbook = new XSSFWorkbook();
		org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Tour Report");

		String[] headers = { "employeeid", "employee_name", "Department", "Designation", "modeOfTravel",
				"purpose_of_travel", "travel_from", "travel_to", "Date_of_travel", "Date_of_return", "Requested_amount",
				"current_location", "contact_no", "email", "hod_empid", "utilized_amount", "adjustment", "cash_mode",
				"fare_amount", "tour_type", "billStatus", "comment", "TeamMemberEmpid", "TeamMemberName" };

		Row headerRow = ((org.apache.poi.ss.usermodel.Sheet) sheet).createRow(0);
		IntStream.range(0, headers.length).forEach(i -> headerRow.createCell(i).setCellValue(headers[i]));

		AtomicInteger rowIndex = new AtomicInteger(1);
		tourList.stream().forEach(tour -> {
			Row row = sheet.createRow(rowIndex.getAndIncrement());
			IntStream.range(0, headers.length - 2).forEach(i -> {
				Object value = tour.getOrDefault(headers[i], "");
				row.createCell(i).setCellValue(value != null ? value.toString() : "");
			});
			Integer tourId = (Integer) tour.get("tour_id");
			List<Map<String, Object>> teamRows = jdbcTemplate.queryForList(
					"SELECT employeeid, employeeName FROM touradvance.tbl_employee_tour_team_members WHERE tour_id = ?",
					tourId);

			String teamEmpIds = teamRows.stream().map(member -> String.valueOf(member.getOrDefault("employeeid", "")))
					.collect(Collectors.joining(","));

			String teamNames = teamRows.stream().map(member -> String.valueOf(member.getOrDefault("employeeName", "")))
					.collect(Collectors.joining(","));

			row.createCell(headers.length - 2).setCellValue(teamEmpIds);
			row.createCell(headers.length - 1).setCellValue(teamNames);
		});
		IntStream.range(0, headers.length).forEach(sheet::autoSizeColumn);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		workbook.write(out);
		workbook.close();
		return out.toByteArray();
	}
//
//	public Map<String, Object> getFinalSettlementEmployees(int page, int size, String search) {
//		int offset = page * size;
//
//		String baseSql = "FROM touradvance.tbl_employee_tour_advance a "
//				+ "LEFT JOIN touradvance.tbl_master_tour_type b ON b.id = a.tour_type "
//				+ "LEFT JOIN touradvance.tbl_tour_advance_settlement c ON c.tour_id = a.tour_id "
//				+ "WHERE a.status = 1002 ";
//
//		StringBuilder whereClause = new StringBuilder();
//		List<Object> params = new ArrayList<>();
//		List<Object> countParams = new ArrayList<>();
//
//		if (search != null && !search.trim().isEmpty()) {
//			whereClause.append("AND (a.employeeid LIKE ? OR a.employee_name LIKE ?) ");
//			String searchValue = "%" + search + "%";
//			params.add(searchValue);
//			params.add(searchValue);
//			countParams.add(searchValue);
//			countParams.add(searchValue);
//		}
//		String countSql = "SELECT COUNT(*) " + baseSql + whereClause.toString();
//		int totalElements = jdbcTemplate.queryForObject(countSql, Integer.class, countParams.toArray());
//		String adjustmentCountSql = "SELECT "
//				+ "SUM(CASE WHEN c.adjustment > 0 THEN 1 ELSE 0 END) AS reimbursementCount, "
//				+ "SUM(CASE WHEN c.adjustment < 0 THEN 1 ELSE 0 END) AS refundCount, "
//				+ "SUM(CASE WHEN c.adjustment = 0 OR c.adjustment IS NULL THEN 1 ELSE 0 END) AS settledCount " + baseSql
//				+ whereClause.toString();
//
//		Map<String, Object> adjustmentCounts = jdbcTemplate.queryForMap(adjustmentCountSql, countParams.toArray());
//		String dataSql = "SELECT a.tour_id, a.employeeid, a.employee_name, a.Date_of_travel, a.Date_of_return, "
//				+ "a.Requested_amount, c.utilized_amount, c.adjustment, a.tour_type, b.name AS tourName, "
//				+ "c.receipt_file, c.comment, c.uploadedBy, c.uploadedDate " + baseSql + whereClause.toString()
//				+ "ORDER BY a.createdDate DESC LIMIT ? OFFSET ?";
//
//		params.add(size);
//		params.add(offset);
//		List<Map<String, Object>> content = jdbcTemplate.queryForList(dataSql, params.toArray());
//		Map<String, Object> response = new HashMap<>();
//		response.put("content", content);
//		response.put("totalElements", totalElements);
//		response.put("page", page);
//		response.put("size", size);
//		response.put("reimbursementCount", adjustmentCounts.get("reimbursementCount"));
//		response.put("refundCount", adjustmentCounts.get("refundCount"));
//		response.put("settledCount", adjustmentCounts.get("settledCount"));
//
//		return response; 
//	}
	public Map<String, Object> getFinalSettlementEmployees(int page, int size, String search, int loginid) {
	    int offset = page * size;

	    String baseSql = "FROM touradvance.tbl_employee_tour_advance a "
	            + "LEFT JOIN touradvance.tbl_master_tour_type b ON b.id = a.tour_type "
	            + "LEFT JOIN touradvance.tbl_tour_advance_settlement c ON c.tour_id = a.tour_id "
	            + "WHERE a.status = 1002 AND c.uploadedBy = ? ";

	    StringBuilder whereClause = new StringBuilder();
	    List<Object> params = new ArrayList<>();
	    List<Object> countParams = new ArrayList<>();

	    // Add loginid first for createdBy filter
	    params.add(loginid);
	    countParams.add(loginid);

	    if (search != null && !search.trim().isEmpty()) {
	        whereClause.append("AND (a.employeeid LIKE ? OR a.employee_name LIKE ?) ");
	        String searchValue = "%" + search + "%";
	        params.add(searchValue);
	        params.add(searchValue);
	        countParams.add(searchValue);
	        countParams.add(searchValue);
	    }

	    // --- Count Query ---
	    String countSql = "SELECT COUNT(*) " + baseSql + whereClause.toString();
	    int totalElements = jdbcTemplate.queryForObject(countSql, countParams.toArray(), Integer.class);

	    // --- Adjustment Counts ---
	    String adjustmentCountSql = "SELECT "
	            + "SUM(CASE WHEN c.adjustment > 0 THEN 1 ELSE 0 END) AS reimbursementCount, "
	            + "SUM(CASE WHEN c.adjustment < 0 THEN 1 ELSE 0 END) AS refundCount, "
	            + "SUM(CASE WHEN c.adjustment = 0 OR c.adjustment IS NULL THEN 1 ELSE 0 END) AS settledCount "
	            + baseSql + whereClause.toString();

	    Map<String, Object> adjustmentCounts = jdbcTemplate.queryForMap(adjustmentCountSql, countParams.toArray());

	    // --- Data Query ---
	    String dataSql = "SELECT a.tour_id, a.employeeid, a.employee_name, a.Date_of_travel, a.Date_of_return, "
	            + "a.Requested_amount, c.utilized_amount, c.adjustment, a.tour_type, b.name AS tourName, "
	            + "c.receipt_file, c.comment, c.uploadedBy, c.uploadedDate "
	            + baseSql + whereClause.toString()
	            + "ORDER BY a.createdDate DESC LIMIT ? OFFSET ?";

	    params.add(size);
	    params.add(offset);

	    List<Map<String, Object>> content = jdbcTemplate.queryForList(dataSql, params.toArray());

	    // --- Response ---
	    Map<String, Object> response = new HashMap<>();
	    response.put("content", content);
	    response.put("totalElements", totalElements);
	    response.put("page", page);
	    response.put("size", size);
	    response.put("reimbursementCount", adjustmentCounts.get("reimbursementCount"));
	    response.put("refundCount", adjustmentCounts.get("refundCount"));
	    response.put("settledCount", adjustmentCounts.get("settledCount"));

	    return response;
	}

	public Map<String, Object> saveConveyanceBill(EmployeeConveyanceBillDTO dto, MultipartFile billFile,
			MultipartFile chequeFile, String createdBy) throws IOException {

		String billPath = (billFile != null && !billFile.isEmpty()) ? saveBillFile(billFile) : "";
		String chequePath = (chequeFile != null && !chequeFile.isEmpty()) ? saveChequeFile(chequeFile) : "";
		String description = (dto.getDescription() != null) ? dto.getDescription() : "";
		Integer prevBillStatus = (dto.getPrevBillStatus() != null) ? dto.getPrevBillStatus() : 0;
		String amountTransfrType = (dto.getAmountTransfrType() != null) ? dto.getAmountTransfrType() : "";

		String sql = "INSERT INTO touradvance.tbl_employee_conveyance_bill "
				+ "(employee_id, name, dept_hod, contact_no, email, location, adhar_no, bill_amount, "
				+ "bill_submitted_date, bank_account_no, bank_ifsc_no, cheque_no, bank_branch, name_of_bank, amount_transfer_type, "
				+ "description, conveyance_bill_path, conveyance_cheque_path, prev_bill_status, created_by, created_date) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())";

		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, new String[] { "conveyance_id" });
			ps.setString(1, dto.getEmployeeId());
			ps.setString(2, dto.getName());
			ps.setString(3, dto.getDepthod());
			ps.setString(4, dto.getContactNo());
			ps.setString(5, dto.getEmail());
			ps.setString(6, dto.getLocation());
			ps.setString(7, dto.getAdharNo());
			ps.setBigDecimal(8, dto.getBillAmount());
			ps.setObject(9, LocalDate.parse(dto.getBillSubmittedDate()));
			ps.setString(10, dto.getBankAccountNo());
			ps.setString(11, dto.getBankIfscNo());
			ps.setString(12, dto.getChequeNo());
			ps.setString(13, dto.getBankBranch());
			ps.setString(14, dto.getNameOfBank());
			ps.setString(15, amountTransfrType);
			ps.setString(16, description);
			ps.setString(17, billPath);
			ps.setString(18, chequePath);
			ps.setInt(19, prevBillStatus);
			ps.setString(20, createdBy);
			return ps;
		}, keyHolder);

		int convienceId = keyHolder.getKey() != null ? keyHolder.getKey().intValue() : 0;

		Map<String, Object> response = new HashMap<>();
		if (convienceId > 0) {
			response.put("status", "success");
			response.put("ConveyanceId", convienceId);
			response.put("message", "Conveyance  Bill saved successfully");
		} else {
			response.put("status", "failure");
			response.put("message", "Failed to save Conveyance  Bill");
		}

		return response;
	}

	public String saveBillFile(MultipartFile file) throws IOException {
		return saveFile(file, billUploadPath);
	}

	public String saveChequeFile(MultipartFile file) throws IOException {
		return saveFile(file, chequeUploadPath);
	}

	private String saveFile(MultipartFile file, String basePath) throws IOException {
		if (file == null || file.isEmpty()) {
			return "";
		}
		File dir = new File(basePath);
		if (!dir.exists())
			dir.mkdirs();

		String filePath = basePath + File.separator + System.currentTimeMillis() + "_" + file.getOriginalFilename();
		File dest = new File(filePath);
		file.transferTo(dest);
		return filePath;
	}
//
//	public List<Map<String, Object>> getConveyanceBillEmployees(int loginid) {
//	
//		StringBuffer sql = new StringBuffer();
//		sql.append("SELECT ").append("a.conveyance_id, ").append("a.employee_id, ").append("a.name, ")
//				.append("a.bill_amount, ").append("a.bill_submitted_date, ").append("d.name AS department, ")
//				.append("e.name AS designation, ").append("f.name AS division, ").append("h.mode_name AS amount_mode, ")
//				.append("i.name AS bill_status, ").append("a.amount_transfer_type as transferType ")
//				.append("FROM touradvance.tbl_employee_conveyance_bill a ")
//				.append("LEFT JOIN hclhrm_prod.tbl_employee_primary b ON a.employee_id = b.employeesequenceno ")
//				.append("LEFT JOIN hclhrm_prod.tbl_employee_professional_details c ON c.employeeid = b.employeeid ")
//				.append("LEFT JOIN hcladm_prod.tbl_department d ON d.departmentid = c.departmentid ")
//				.append("LEFT JOIN hcladm_prod.tbl_designation e ON e.designationid = c.designationid ")
//				.append("LEFT JOIN hcladm_prod.tbl_businessunit f ON f.businessunitid = b.companyid ")
//				.append("LEFT JOIN touradvance.tbl_master_amount_mode h ON h.id = a.amount_transfer_type ")
////				.append("LEFT JOIN touradvance.bill_status i ON i.id = a.prev_bill_status where ORDER BY a.created_date DESC");
//				.append(" LEFT JOIN touradvance.bill_status i ON i.id = a.prev_bill_status ")
//				.append(" ORDER BY a.created_date DESC");
//		return jdbcTemplate.queryForList(sql.toString());
//	}

	public List<Map<String, Object>> getConveyanceBillEmployees(int loginid) {

	    StringBuffer sql = new StringBuffer();
	    sql.append("SELECT ")
	            .append("a.conveyance_id, ")
	            .append("a.employee_id, ")
	            .append("a.name, ")
	            .append("a.bill_amount, ")
	            .append("a.bill_submitted_date, ")
	            .append("d.name AS department, ")
	            .append("e.name AS designation, ")
	            .append("f.name AS division, ")
	            .append("h.mode_name AS amount_mode, ")
	            .append("i.name AS bill_status, ")
	            .append("a.amount_transfer_type as transferType ")
	            .append("FROM touradvance.tbl_employee_conveyance_bill a ")
	            .append("LEFT JOIN hclhrm_prod.tbl_employee_primary b ON a.employee_id = b.employeesequenceno ")
	            .append("LEFT JOIN hclhrm_prod.tbl_employee_professional_details c ON c.employeeid = b.employeeid ")
	            .append("LEFT JOIN hcladm_prod.tbl_department d ON d.departmentid = c.departmentid ")
	            .append("LEFT JOIN hcladm_prod.tbl_designation e ON e.designationid = c.designationid ")
	            .append("LEFT JOIN hcladm_prod.tbl_businessunit f ON f.businessunitid = b.companyid ")
	            .append("LEFT JOIN touradvance.tbl_master_amount_mode h ON h.id = a.amount_transfer_type ")
	            .append("LEFT JOIN touradvance.bill_status i ON i.id = a.prev_bill_status ")
	            .append("WHERE a.created_by = ? ")
	            .append("ORDER BY a.created_date DESC");

	    return jdbcTemplate.queryForList(sql.toString(), loginid);
	}

	public Map<String, Object> getConveyanceFilesById(int id) {
		String sql = "SELECT conveyance_bill_path, conveyance_cheque_path FROM touradvance.tbl_employee_conveyance_bill WHERE conveyance_id = ?";
		try {
			return jdbcTemplate.queryForObject(sql, new Object[] { id }, (rs, rowNum) -> {
				Map<String, Object> filesMap = new HashMap<>();
				try {
					String billPath = rs.getString("conveyance_bill_path");
					filesMap.put("bill",
							billPath != null && !billPath.isEmpty() && Files.exists(Paths.get(billPath))
									? Files.readAllBytes(Paths.get(billPath))
									: null);
					String chequePath = rs.getString("conveyance_cheque_path");
					filesMap.put("cheque",
							chequePath != null && !chequePath.isEmpty() && Files.exists(Paths.get(chequePath))
									? Files.readAllBytes(Paths.get(chequePath))
									: null);

				} catch (IOException e) {

					filesMap.put("bill", null);
					filesMap.put("cheque", null);
				}
				return filesMap;
			});
		} catch (Exception e) {

			return new HashMap<>();
		}
	}

	public Map<String, Object> getTourById(int tourid) {
	    String sql = "SELECT a.advance_bill_file, c.receipt_file " +
	                 "FROM touradvance.tbl_employee_tour_advance a " +
	                 "LEFT JOIN touradvance.tbl_master_tour_type b ON b.id = a.tour_type " +
	                 "LEFT JOIN touradvance.tbl_tour_advance_settlement c ON c.tour_id = a.tour_id " +
	                 "WHERE a.tour_id = ?";

	    try {
	        return jdbcTemplate.queryForObject(sql, new Object[]{tourid}, (rs, rowNum) -> {
	            Map<String, Object> filesMap = new HashMap<>();
	            String billPath = rs.getString("advance_bill_file");
	            try {
	                filesMap.put("advancebill",
	                        (billPath != null && !billPath.isEmpty()) ? Files.readAllBytes(Paths.get(billPath)) : null);
	            } catch (IOException ignored) {
	                filesMap.put("advancebill", null);
	            }
	            String receiptPath = rs.getString("receipt_file");
	            try {
	                filesMap.put("receipt",
	                        (receiptPath != null && !receiptPath.isEmpty()) ? Files.readAllBytes(Paths.get(receiptPath)) : null);
	            } catch (IOException ignored) {
	                filesMap.put("receipt", null);
	            }

	            return filesMap;
	        });
	    } catch (Exception e) {
	        return new HashMap<>();
	    }
	}


}
