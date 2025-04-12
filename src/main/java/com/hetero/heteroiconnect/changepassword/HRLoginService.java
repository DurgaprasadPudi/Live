package com.hetero.heteroiconnect.changepassword;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hetero.heteroiconnect.idcard.EmployeeNotFoundException;

 

@Service
public class HRLoginService {
	@Autowired
	private JdbcTemplate jdbcTemplate;

//	@Transactional
//	public ResponseEntity<Object> passwordChange(ResetPassword resetPassword) {
//		String check = "SELECT Employeeid, employeecode, password, LOGID, createdby, datecreated "
//				+ "FROM hclhrm_prod.tbl_employee_login t WHERE status=1001 AND employeecode=?";
//
//		String sql = "INSERT INTO hclhrm_prod.tbl_employee_login_history "
//				+ "(EMPLOYEEID, EMPLOYEECODE, PASSWORD, LOGID, CREATEDBY, DATECREATED) "
//				+ "VALUES (?, ?, ?, ?, ?, NOW())";
//
//		String insertNewLogin = "INSERT INTO hclhrm_prod.tbl_employee_login "
//				+ "(EMPLOYEEID, EMPLOYEECODE, PASSWORD, LOGID, CREATEDBY, DATECREATED) "
//				+ "VALUES (?, ?, ?, ?, ?, NOW()) " + "ON DUPLICATE KEY UPDATE " + "PASSWORD = VALUES(PASSWORD), "
//				+ "LOGID = VALUES(LOGID), " + "CREATEDBY = VALUES(CREATEDBY), " + "DATECREATED = NOW()";
//
//		int employeeCode = resetPassword.getEmployeeCode();
//		@SuppressWarnings("deprecation")
//		Optional<ResetPassword> existingLoginOpt = jdbcTemplate
//				.query(check, new Object[] { employeeCode }, (rs, rowNum) -> {
//					ResetPassword login = new ResetPassword();
//					login.setEmployeeId(rs.getInt("Employeeid"));
//					login.setEmployeeCode(rs.getInt("employeecode"));
//					login.setPassword(rs.getString("password"));
//					login.setLoginId(rs.getInt("LOGID"));
//					login.setCreatedBy(rs.getInt("createdBy"));
//					return login;
//				}).stream().findFirst();
//
//		if (!existingLoginOpt.isPresent()) {
//			throw new EmployeeNotFoundException("Please Contact Admin: " + employeeCode);
//		}
//
//		ResetPassword existingLogin = existingLoginOpt.get();
//		int rowsAffected = jdbcTemplate.update(sql, existingLogin.getEmployeeId(), existingLogin.getEmployeeCode(),
//				existingLogin.getPassword(), existingLogin.getLoginId(), existingLogin.getCreatedBy());
//
//		if (rowsAffected > 0) {
//			String hashedPassword = hashPassword(resetPassword.getPassword());
//
//			int rowsInserted = jdbcTemplate.update(insertNewLogin, resetPassword.getEmployeeId(),
//					resetPassword.getEmployeeCode(), hashedPassword, resetPassword.getLoginId(),
//					resetPassword.getCreatedBy());
//
//			if (rowsInserted > 0) {
//				return ResponseEntity.status(HttpStatus.OK)
//						.body("Password registration history successfully recorded and new login created/updated.");
//			} else {
//				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to insert or update login.");
//			}
//		} else {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to insert login history.");
//		}
//	}
	 
	
	
	     @Transactional
		public ResponseEntity<Object> passwordChange(ResetPassword resetPassword) {
			String check = "SELECT Employeeid, employeecode, password, LOGID, createdby, datecreated "
					+ "FROM hclhrm_prod.tbl_employee_login t WHERE status=1001 AND employeecode=?";
	 
			String sql = "INSERT INTO hclhrm_prod.tbl_employee_login_history "
					+ "(EMPLOYEEID, EMPLOYEECODE, PASSWORD, CREATEDBY, DATECREATED) "
					+ "VALUES (?, ?,  ?, ?, NOW())";
	 
			String insertNewLogin = "INSERT INTO hclhrm_prod.tbl_employee_login "
					+ "(EMPLOYEEID, EMPLOYEECODE, PASSWORD, CREATEDBY, DATECREATED) "
					+ "VALUES (?, ?, ?, ?, NOW()) " + "ON DUPLICATE KEY UPDATE " + "PASSWORD = VALUES(PASSWORD), "
					+ "CREATEDBY = VALUES(CREATEDBY), " + "DATECREATED = NOW()";
	 
			int employeeCode = resetPassword.getEmployeeCode();
			@SuppressWarnings("deprecation")
			Optional<ResetPassword> existingLoginOpt = jdbcTemplate
					.query(check, new Object[] { employeeCode }, (rs, rowNum) -> {
						ResetPassword login = new ResetPassword();
						login.setEmployeeId(rs.getInt("Employeeid"));
						login.setEmployeeCode(rs.getInt("employeecode"));
						login.setPassword(rs.getString("password"));
						/* login.setLoginId(rs.getInt("LOGID")); */
						login.setCreatedBy(rs.getInt("createdBy"));
						return login;
					}).stream().findFirst();
	 
			if (!existingLoginOpt.isPresent()) {
				throw new EmployeeNotFoundException("Please Contact Admin: " + employeeCode);
			}
	 
			ResetPassword existingLogin = existingLoginOpt.get();
			int rowsAffected = jdbcTemplate.update(sql, existingLogin.getEmployeeId(), existingLogin.getEmployeeCode(),
					existingLogin.getPassword(), existingLogin.getCreatedBy());
	 
			if (rowsAffected > 0) {
				String hashedPassword = hashPassword(resetPassword.getPassword());
	 
				int rowsInserted = jdbcTemplate.update(insertNewLogin, resetPassword.getEmployeeId(),
						resetPassword.getEmployeeCode(), hashedPassword,
						resetPassword.getCreatedBy());
	 
				if (rowsInserted > 0) {
					return ResponseEntity.status(HttpStatus.OK)
							.body("Password registration history successfully recorded and new login created/updated.");
				} else {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to insert or update login.");
				}
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to insert login history.");
			}
		}

	private String hashPassword(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());
			byte[] bytes = md.digest();
			StringBuilder hexString = new StringBuilder();
			for (byte b : bytes) {
				hexString.append(String.format("%02x", b));
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	

}
