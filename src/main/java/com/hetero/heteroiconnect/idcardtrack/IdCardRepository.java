package com.hetero.heteroiconnect.idcardtrack;

 
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

  
@Repository
public class IdCardRepository {
	private JdbcTemplate jdbcTemplate;

	public IdCardRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	 
	public ApiResponse addIDCardIssuedDetails(UploadDetails details) {
		if (details.getCardType().equals("1")) {
			String checkSql = "SELECT COUNT(*) FROM hclhrm_prod_others.tbl_employee_idcard_tracking "
					+ "WHERE employeeid = ? AND status = 1001";
			Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, details.getEmployeeid());
 
			if (count != null && count > 0) {
				return new ApiResponse(1001, "ID Card Details already added for this employee.");
			}
			String insertSql = "INSERT INTO hclhrm_prod_others.tbl_employee_idcard_tracking "
					+ "(employeeid, idcardissueddate, comment, createdby, status, card_type) "
					+ "VALUES (?, ?, ?, ?, 1001, ?)";
			jdbcTemplate.update(insertSql, details.getEmployeeid(), details.getIdcardissueddate(), details.getComment(),
					details.getCreatedby(), details.getCardType());
			return new ApiResponse(1002, "ID Card Details Added Successfully!");
		} else {
			String checkSql = "SELECT COUNT(*) FROM hclhrm_prod_others.tbl_employee_idcard_tracking "
					+ "WHERE employeeid = ? AND status = 1001";
			Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, details.getEmployeeid());
			if (count != null && count > 0) {
				String sql = "SELECT tracking_id FROM hclhrm_prod_others.tbl_employee_idcard_tracking "
						+ "WHERE employeeid = ? AND status = 1001";
				Integer trackingId = jdbcTemplate.queryForObject(sql, Integer.class, details.getEmployeeid());
				String insertSql = "INSERT INTO hclhrm_prod_others.tbl_employee_idcard_reissued_details "
						+ "(tracking_id, reissued_date, comment, reissued_by, status, created_date_time) "
						+ "VALUES (?, ?, ?, ?, 1001, NOW())";
				jdbcTemplate.update(insertSql, trackingId, details.getIdcardissueddate(), details.getComment(),
						details.getCreatedby());
				return new ApiResponse(1003, "ID Card Reissued Details Added Successfully!");
			} else {
				return new ApiResponse(1004, "No active ID Card record found to reissue.");
			}
		}
 
	}
 
	public List<FetchDetails> getAllIDCardIssuedDetails(int status) {
		StringBuilder sql = new StringBuilder();
 
		sql.append(
				"SELECT a.tracking_id, a.employeeid, DATE_FORMAT(a.idcardissueddate,'%d-%m-%Y') AS idcardissueddate, ")
				.append("IFNULL(NULLIF(a.comment,''),'--') AS comment, a.createdby, ")
				.append("c.callname AS createdbyname, DATE_FORMAT(a.createddate, '%d-%m-%Y') AS createddate, a.status, ")
				.append("CASE ").append("  WHEN a.card_type = 1 THEN 'New ID' ")
				.append("  WHEN a.card_type = 2 THEN 'Reissue ID' ").append("  ELSE 'Unknown' ")
				.append("END AS card_type_name, ")
				.append("b.callname AS employeename, IFNULL(NULLIF(DATE_FORMAT(a.id_submitted_date, '%d-%m-%Y'), ''), '--') AS id_submitted_date, ")
				.append("a.submitted_by, d.callname AS submittedByname, ")
				.append("IFNULL(NULLIF(a.submitted_comment,''),'--') AS submitted_comment, b.status AS final_status ")
				.append("FROM hclhrm_prod_others.tbl_employee_idcard_tracking a ")
				.append("LEFT JOIN hclhrm_prod.tbl_employee_primary b ON a.employeeid = b.employeesequenceno ")
			 	.append("LEFT JOIN hclhrm_prod.tbl_employee_primary c ON a.createdby = c.employeesequenceno ")
				.append("LEFT JOIN hclhrm_prod.tbl_employee_primary d ON a.submitted_by = d.employeesequenceno ")
				.append("WHERE a.status = 1001 ");
 
		if (status == 1001) {
			sql.append("AND b.status = ? ");
			return jdbcTemplate.query(sql.toString(), (rs, rowNum) -> new FetchDetails(rs.getInt("tracking_id"),
					rs.getInt("employeeid"), rs.getString("idcardissueddate"), rs.getString("comment"),
					rs.getInt("createdby"), rs.getString("createdbyname"), rs.getString("createddate"),
					rs.getInt("status"), rs.getString("card_type_name"), rs.getString("employeename"),
					rs.getString("id_submitted_date"), rs.getString("submitted_by"), rs.getString("submitted_comment"),
					rs.getString("submittedByname"), rs.getString("final_status")), status);
		} else {
			sql.append("AND b.status != 1001 ");
			return jdbcTemplate.query(sql.toString(), (rs, rowNum) -> new FetchDetails(rs.getInt("tracking_id"),
					rs.getInt("employeeid"), rs.getString("idcardissueddate"), rs.getString("comment"),
					rs.getInt("createdby"), rs.getString("createdbyname"), rs.getString("createddate"),
					rs.getInt("status"), rs.getString("card_type_name"), rs.getString("employeename"),
					rs.getString("id_submitted_date"), rs.getString("submitted_by"), rs.getString("submitted_comment"),
					rs.getString("submittedByname"), rs.getString("final_status")));
		}
	}
 
	public ApiResponse updateIDDetails(UploadDetails details) {
		String updateSql = "UPDATE hclhrm_prod_others.tbl_employee_idcard_tracking SET "
				+ "id_submitted_date = ?, submitted_comment = ?, submitted_by = ? " + "WHERE employeeid = ?";
		jdbcTemplate.update(updateSql, details.getIdcardissueddate(), details.getComment(), details.getCreatedby(),
				details.getEmployeeid());
		return new ApiResponse(1002, "ID Card Details Submitted Successfully!");
	}
 
	public List<ReissuedDetailsDTO> getReissuedDetails(String trackingId) {
		String sql = "SELECT a.id, a.reissued_date, IFNULL(NULLIF(a.comment, ''), '--') AS comment, a.reissued_by, b.callname "
				+ "FROM hclhrm_prod_others.tbl_employee_idcard_reissued_details a "
				+ "LEFT JOIN hclhrm_prod.tbl_employee_primary b ON a.reissued_by = b.employeesequenceno "
				+ "WHERE a.tracking_id =? and a.status=1001 ";
		return jdbcTemplate
				.query(sql,
						(rs, rowNum) -> new ReissuedDetailsDTO(rs.getInt("id"), rs.getString("reissued_date"),
								rs.getString("comment"), rs.getString("reissued_by"), rs.getString("callname")),
						trackingId);
	}

}
