package com.hetero.heteroiconnect.servicebilltracking;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hetero.heteroiconnect.iconnectrights.BadRequestException;

 

@Service
public class ServiceBillServiceImpl implements ServiceBill {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Value("${service.bill.upload.path}")
	private String uploadBasePath;

	@Override
	public String saveServiceBill(ServiceBillDTO dto, MultipartFile invoiceFile, MultipartFile chequeFile) {
		String invoicePath = null;
		String chequePath = null;

		try {
			if (invoiceFile != null && !invoiceFile.isEmpty()) {
				File invoiceDir = new File(uploadBasePath, "invoice");
				invoiceDir.mkdirs();
				invoicePath = invoiceDir.getAbsolutePath() + File.separator + invoiceFile.getOriginalFilename();
				invoiceFile.transferTo(new File(invoicePath));
			}

			if (chequeFile != null && !chequeFile.isEmpty()) {
				File chequeDir = new File(uploadBasePath, "cheque");
				chequeDir.mkdirs();
				chequePath = chequeDir.getAbsolutePath() + File.separator + chequeFile.getOriginalFilename();
				chequeFile.transferTo(new File(chequePath));
			}
		} catch (IOException e) {
			throw new BadRequestException("File upload failed");
		}

		String sql = "INSERT IGNORE INTO test.tbl_service_bill "
				+ "(service_id, vendor_name, location, country_id, state_id, city_id, sap_code, bill_submitted_date,bill_received_date, "
				+ "invoice_amount, invoice_date, business_unit_id, invoice, cheque, remark, status, created_by, created_date) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, NOW())";

		int rows = jdbcTemplate.update(sql, dto.getServiceId(), dto.getVendorName(), dto.getLocation(),
				dto.getCountryId(), dto.getStateId(), dto.getCityId(), dto.getSapCode(),
				dto.getBillSubmittedDate() != null ? new java.sql.Date(dto.getBillSubmittedDate().getTime()) : null,
				dto.getBillReceivedDate() != null ? new java.sql.Date(dto.getBillReceivedDate().getTime()) : null,
				dto.getInvoiceAmount(),
				dto.getInvoiceDate() != null ? new java.sql.Date(dto.getInvoiceDate().getTime()) : null,
				dto.getBusinessUnitId(), invoicePath, chequePath, dto.getRemark(),
				dto.getStatus() != null ? dto.getStatus() : 1001, dto.getCreatedBy());

		return rows > 0 ? "Inserted Successfully" : "Insertion Failed";
	}

	@Override
	public List<Map<String, Object>> getStates() {
		String sql = "SELECT * FROM test.state WHERE country_id = 1";
		return jdbcTemplate.queryForList(sql);
	}

	public List<Map<String, Object>> getCities(int stateId) {
		String sql = "SELECT * FROM test.city WHERE state_id = ?";
		return jdbcTemplate.queryForList(sql, stateId);
	}

	public Object getBusinessUnit() {
		String sql = "select BUSINESSUNITID,NAME from  hcladm_prod.tbl_businessunit where status=1001";
		return jdbcTemplate.queryForList(sql);
	}

	@Override
	public List<Map<String, Object>> getServiceBillData(int loginId, String invoiceYearMonth) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append("a.service_id AS serviceId, ");
		sql.append("a.vendor_name AS vendorName, ");
		sql.append("a.location, ");
		sql.append("a.country_id AS countryId, ");
		sql.append("b.country_name AS countryName, ");
		sql.append("a.state_id AS stateId, ");
		sql.append("c.state_name AS stateName, ");
		sql.append("a.city_id AS cityId, ");
		sql.append("d.city_name AS cityName, ");
		sql.append("a.sap_code AS sapCode, ");
		sql.append("a.invoice_amount, ");
		sql.append("a.invoice_date, ");
		sql.append("a.business_unit_id as buId, ");
		sql.append("e.name as buName, ");
		sql.append("a.bill_submitted_date AS billSubmittedDate, ");
		sql.append("a.bill_received_date AS billReceivedDate, ");
		sql.append("a.invoice, ");
		sql.append("a.cheque, ");
		sql.append("a.remark, ");
		sql.append("a.status, ");
		sql.append("a.created_by AS createdBy, ");
		sql.append("a.created_date AS createdDate, ");
		sql.append("a.updated_by AS updatedBy ");
		sql.append("FROM test.tbl_service_bill a ");
		sql.append("LEFT JOIN test.country b ON b.country_id = a.country_id ");
		sql.append("LEFT JOIN test.state c   ON c.state_id = a.state_id ");
		sql.append("LEFT JOIN test.city d    ON d.city_id  = a.city_id ");
		sql.append("LEFT JOIN hcladm_prod.tbl_businessunit e ON e.businessunitid=a.business_unit_id ");
		sql.append(" WHERE a.created_by = ? AND a.status=1001 ");

		List<Object> params = new ArrayList<>();
		params.add(loginId);

		if (invoiceYearMonth != null && !invoiceYearMonth.isEmpty()) {
			sql.append("AND DATE_FORMAT(a.invoice_date, '%Y-%m') = ? ");
			params.add(invoiceYearMonth);
		}

		sql.append("ORDER BY a.created_date DESC");

		return jdbcTemplate.queryForList(sql.toString(), params.toArray());
	}

	public Map<String, Object> getFilesById(int id) {
		String sql = "select invoice,cheque from test.tbl_service_bill where service_id=?";
		try {
			return jdbcTemplate.queryForObject(sql, new Object[] { id }, (rs, rowNum) -> {
				Map<String, Object> filesMap = new HashMap<>();
				try {
					String billPath = rs.getString("invoice");
					filesMap.put("invoicebill",
							billPath != null && !billPath.isEmpty() && Files.exists(Paths.get(billPath))
									? Files.readAllBytes(Paths.get(billPath))
									: null);
					String chequePath = rs.getString("cheque");
					filesMap.put("cheque",
							chequePath != null && !chequePath.isEmpty() && Files.exists(Paths.get(chequePath))
									? Files.readAllBytes(Paths.get(chequePath))
									: null);

				} catch (IOException e) {

					filesMap.put("invoicebill", null);
					filesMap.put("cheque", null);
				}
				return filesMap;
			});
		} catch (Exception e) {

			return new HashMap<>();
		}
	}

	@Override
	public String updateServiceBill(ServiceBillDTO dto, MultipartFile invoiceFile, MultipartFile chequeFile) {
		String invoicePath = null;
		String chequePath = null;

		try {
			if (invoiceFile != null && !invoiceFile.isEmpty()) {
				File invoiceDir = new File(uploadBasePath, "invoice");
				invoiceDir.mkdirs();
				invoicePath = invoiceDir.getAbsolutePath() + File.separator + invoiceFile.getOriginalFilename();
				invoiceFile.transferTo(new File(invoicePath));
			}

			if (chequeFile != null && !chequeFile.isEmpty()) {
				File chequeDir = new File(uploadBasePath, "cheque");
				chequeDir.mkdirs();
				chequePath = chequeDir.getAbsolutePath() + File.separator + chequeFile.getOriginalFilename();
				chequeFile.transferTo(new File(chequePath));
			}
		} catch (IOException e) {
			throw new BadRequestException("File upload failed");
		}

		String sql = "UPDATE test.tbl_service_bill SET "
				+ "vendor_name = ?, location = ?, country_id = ?, state_id = ?, city_id = ?, "
				+ "sap_code = ?, bill_submitted_date = ?,bill_received_date=?, invoice_amount = ?, invoice_date = ?, business_unit_id = ?, "
				+ "invoice = ?, cheque = ?, remark = ?, " + "updated_by = ?, updated_date = NOW() "
				+ "WHERE service_id = ?";

		int rows = jdbcTemplate.update(sql, dto.getVendorName(), dto.getLocation(), dto.getCountryId(),
				dto.getStateId(), dto.getCityId(), dto.getSapCode(),
				dto.getBillSubmittedDate() != null ? new java.sql.Date(dto.getBillSubmittedDate().getTime()) : null,
				dto.getBillReceivedDate() != null ? new java.sql.Date(dto.getBillReceivedDate().getTime()) : null,
				dto.getInvoiceAmount(),
				dto.getInvoiceDate() != null ? new java.sql.Date(dto.getInvoiceDate().getTime()) : null, // added
				dto.getBusinessUnitId(), invoicePath, chequePath, dto.getRemark(), dto.getUpdatedBy(),
				dto.getServiceId());

		return rows > 0 ? "Updated Successfully" : "Update Failed";
	}

}
