package com.hetero.heteroiconnect.zonedetails;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.hetero.heteroiconnect.promotion.exception.EmployeeAlreadyFoundException;
import com.hetero.heteroiconnect.promotion.exception.EmployeeNotFoundException;

 

@Service
public class ZoneServiceImpl {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ZoneEmployeeDTO saveEmployees(ZoneEmployeeDTO dto) {
		String query = "SELECT employee_id FROM test.tbl_zone_employees WHERE employee_id = ?";

//		Optional<String> existingEmployee = Optional
//				.ofNullable(jdbcTemplate.query(query, new Object[] { dto.getEmployeeId() }, rs -> {
//					return rs.next() ? rs.getString("employee_id") : null;
//				}));
		Optional<String> existingEmployee = Optional.ofNullable(
			    jdbcTemplate.query(query, rs -> {
			        return rs.next() ? rs.getString("employee_id") : null;
			    }, dto.getEmployeeId())
			);
		existingEmployee.ifPresent(e -> {
			throw new EmployeeAlreadyFoundException("Employee already exists. Do you want to edit?");
		});
		String insertSql = "INSERT INTO test.tbl_zone_employees ("
				+ "employee_id, city_id, location_id, building_id, floor_id, zone_id, section, sub_section, created_by"
				+ ") VALUES (?, ?, ?,  ?, ?, ?, ?, ?, ?)";

		jdbcTemplate.update(insertSql, dto.getEmployeeId(), dto.getCityId(), dto.getLocationId(), dto.getBuildingId(),
				dto.getFloorId(), dto.getZoneId(), dto.getSection(), dto.getSubSection(), dto.getCreatedby());

		return dto;
	}

	public ZoneEmployeeDTO updateEmployeeZone(ZoneEmployeeDTO dto) {
		String updateSql = "UPDATE test.tbl_zone_employees SET "
				+ "city_id = ?, location_id = ?, building_id = ?, floor_id = ?, zone_id = ?, "
				+ "section = ?, sub_section = ?, updated_by = ?, updated_date = ? " + "WHERE id = ?";
		int rows = jdbcTemplate.update(updateSql, dto.getCityId(), dto.getLocationId(), dto.getBuildingId(),
				dto.getFloorId(), dto.getZoneId(), dto.getSection(), dto.getSubSection(), dto.getUpdateby(),
				java.time.LocalDateTime.now(), dto.getId());
		if (rows == 0) {
			throw new EmployeeNotFoundException("Employee not found. Cannot update.");
		}
		return dto;
	}

	public List<Map<String, Object>> getCities() {
		String sql = "select city_id,city_name from test.tbl_city_master where status=1001";
		return jdbcTemplate.queryForList(sql.toString());
	}

	public List<Map<String, Object>> getLocations(String cityId) {
		String sql = "SELECT location_id, location_name FROM test.tbl_location_master WHERE city_id = ? AND status = 1001";
		return jdbcTemplate.queryForList(sql, cityId);
	}

//	public List<Map<String, Object>> getCompanys(String locationId) {
//		String sql = "SELECT DISTINCT cm.company_id, cm.company_name " + "FROM test.tbl_company_master cm "
//				+ "JOIN test.tbl_company_address ca ON cm.company_id = ca.company_id " + "WHERE ca.location_id = ?";
//		return jdbcTemplate.queryForList(sql, locationId);
//	}

//	public List<Map<String, Object>> getBuilding(String locationId, String companyId) {
//		String sql = "SELECT building_id, building_name FROM test.tbl_building_master "
//				+ "WHERE location_id = ? AND company_id = ? AND status = 1001";
//		return jdbcTemplate.queryForList(sql, locationId, companyId);
//	}
	public List<Map<String, Object>> getBuilding(String locationId) {
		String sql = "SELECT building_id, building_name FROM test.tbl_building_master "
				+ "WHERE location_id = ? AND status = 1001";
		return jdbcTemplate.queryForList(sql, locationId);
	}

	public List<Map<String, Object>> getFloor(String buildingId) {
		String sql = "SELECT floor_id, floor_name FROM test.tbl_floor_master WHERE building_id = ? AND status = 1001";
		return jdbcTemplate.queryForList(sql, buildingId);
	}

	public List<Map<String, Object>> getZone(String floorId) {
		String sql = "SELECT zone_id, zone_name FROM test.tbl_zones_data WHERE floor_id = ? AND status = 1001";
		return jdbcTemplate.queryForList(sql, floorId);
	}

	public Object getFetchEmployees() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT a.id, ");
		sql.append("a.employee_id AS employeeid, ");
		sql.append("b.callname AS name, ");
		sql.append("e.name AS deptName, ");
		sql.append("f.name AS division, ");
//		sql.append("g.dateofjoin, ");
		sql.append("ci.city_name AS cityName, ");
		sql.append("lo.location_name AS locationName, ");
    	sql.append("f.name AS companyName, ");
		sql.append("bu.building_name AS buildingName, ");
		sql.append("fl.floor_name AS floorName, ");
		sql.append("d.zone_name AS zonename, ");
		sql.append("ci.city_id,lo.location_id,bu.building_id,fl.floor_id,d.zone_id,a.section ");
		sql.append("FROM test.tbl_zone_employees a ");
		sql.append("LEFT JOIN hclhrm_prod.tbl_employee_primary b ON a.employee_id = b.employeesequenceno ");
		sql.append("LEFT JOIN hclhrm_prod.tbl_employee_professional_details c ON c.employeeid = b.employeeid ");
		sql.append("LEFT JOIN test.tbl_zones_data d ON d.zone_id = a.zone_id ");
		sql.append("LEFT JOIN hcladm_prod.tbl_department e ON e.departmentid = c.departmentid ");
		sql.append("LEFT JOIN hcladm_prod.tbl_businessunit f ON f.businessunitid = b.companyid ");
		sql.append("LEFT JOIN hclhrm_prod.tbl_employee_profile g ON g.employeeid = b.employeeid ");
		sql.append("LEFT JOIN test.tbl_city_master ci ON ci.city_id = a.city_id ");
		sql.append("LEFT JOIN test.tbl_location_master lo ON lo.location_id = a.location_id ");
		sql.append("LEFT JOIN test.tbl_building_master bu ON bu.building_id = a.building_id ");
		sql.append("LEFT JOIN test.tbl_floor_master fl ON fl.floor_id = a.floor_id ");
//		sql.append("LEFT JOIN test.tbl_company_master cm ON cm.company_id = a.company_id ");
//		sql.append(
//				"LEFT JOIN test.tbl_company_address ca ON ca.company_id = a.company_id AND ca.location_id = a.location_id ");
		sql.append("WHERE b.status = 1001");
		return jdbcTemplate.queryForList(sql.toString());
	}

	public byte[] getExcelDownload() {
	    @SuppressWarnings("unchecked")
	    List<Map<String, Object>> data = (List<Map<String, Object>>) getFetchEmployees();

	    if (data == null || data.isEmpty()) {
	        throw new EmptyDataNotFoundException("No employee data found to export.");
	    }
	    List<String> dbKeys = Arrays.asList("employeeid", "name", "deptName", "division", "cityName", "locationName",
	            "companyName", "buildingName", "floorName", "zonename", "section");

	    List<String> headers = Arrays.asList("Employee ID", "Name", "Department", "Division", "City", "Location",
	            "Company", "Building", "Floor", "Zone", "Section");

	    try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
	        org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Employees");

	        CellStyle headerStyle = workbook.createCellStyle();
	        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
	        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        Font font = workbook.createFont();
	        font.setBold(true);
	        headerStyle.setFont(font);
	        Row headerRow = sheet.createRow(0);
	        IntStream.range(0, headers.size()).forEach(i -> {
	            Cell cell = headerRow.createCell(i);
	            cell.setCellValue(headers.get(i));
	            cell.setCellStyle(headerStyle);
	        });
	        IntStream.range(0, data.size()).forEach(i -> {
	            Map<String, Object> rowMap = data.get(i);
	            Row row = sheet.createRow(i + 1);
	            IntStream.range(0, dbKeys.size()).forEach(j -> {
	                Cell cell = row.createCell(j);
	                String key = dbKeys.get(j);
	                Object value = rowMap.get(key);
	                if ("zonename".equals(key)) {
	                    value = (value != null && !value.toString().trim().isEmpty()) ? value : "NA";
	                }
	                if ("section".equals(key)) {
	                    value = (value != null && !value.toString().trim().isEmpty()) ? value : "NA";
	                }
	                cell.setCellValue(value != null ? value.toString() : "");
	            });
	        });
	        IntStream.range(0, headers.size()).forEach(sheet::autoSizeColumn);
	        workbook.write(outputStream);
	        return outputStream.toByteArray();
	    } catch (Exception e) {
	        throw new RuntimeException("Failed to generate Excel", e);
	    }
	}

}
