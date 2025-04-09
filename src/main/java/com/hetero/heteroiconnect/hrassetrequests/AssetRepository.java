
package com.hetero.heteroiconnect.hrassetrequests;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends JpaRepository<DummyEntity, Long> {

	@Query(value = "select businessunitid, name\r\n" + "FROM hcladm_prod.tbl_businessunit a\r\n"
			+ "LEFT JOIN test.tbl_asset_hr_bu b on a.businessunitid=b.bu\r\n"
			+ "where b.emp_id = ?1 and a.status=1001 and b.status=1001", nativeQuery = true)
	List<Object[]> getBu(String loginId);

	@Query(value = "SELECT PARENTDEPARTMENTID AS departmentid, name " + "FROM hcladm_prod.tbl_department "
			+ "WHERE parentdepartmentid != 0 AND status = 1001 "
			+ "ORDER BY PARENTDEPARTMENTID ASC", nativeQuery = true)
	List<Object[]> getDepartments();

	@Query(value = "SELECT designationid,name FROM hcladm_prod.tbl_designation where status=1001", nativeQuery = true)
	List<Object[]> getDesignations();

	@Query(value = "SELECT asset_type_id,asset_name  FROM asset_management.tbl_asset_types t where status=1001 and  is_for_user=1 ", nativeQuery = true)
	List<Object[]> getAssetTypes();

	@Query(value = "SELECT sno,domain FROM asset_management.tbl_mail_domains t where status =1001", nativeQuery = true)
	List<Object[]> getDomain();

	@Query(value = "SELECT DISTINCT b.employeesequenceno,b.callname\r\n"
			+ "FROM hclhrm_prod.tbl_employee_professional_details a\r\n"
			+ "LEFT JOIN hclhrm_prod.tbl_employee_primary b\r\n" + "    ON a.managerid = b.employeeid\r\n"
			+ "WHERE b.status IN (1001, 1092, 1401) ", nativeQuery = true)
	List<Object[]> getManagers();

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO test.tbl_hr_asset_requests ("
			+ "emp_id, emp_name, department, designation, contact_num, "
			+ "reporting_manager, tentative_joining_date, created_date_time, raised_by, bu, work_location) "
			+ "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, NOW(), ?8, ?9, ?10)", nativeQuery = true)
	void raiseEmployeeAssetRequest(Integer empId, String empName, String department, String designation,
			String contactNum, String reportingManager, Date date, Integer loginId, Integer bu, String workLocation);

	@Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
	Integer getLastInsertedRequestId();

	@Query(value = " select count(*) from test.tbl_asset_hr_bu where emp_id = ?1 and bu = ?2 and status=1001 ", nativeQuery = true)
	Integer getActiveHrBu(int loginId, int bu);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO test.tbl_hr_asset_request_items  ("
			+ "asset_type_id, count, created_date_time, request_id, remarks) "
			+ "VALUES (?1, ?2, NOW(), ?3, ?4)", nativeQuery = true)
	void raiseAssetRequest(Integer assetTypeId, Integer count, Integer requestId, String remarks);

	@Query(value = "SELECT a.request_id, a.emp_id, a.emp_name, b.name AS dept_name, "
			+ "c.name AS des_name, a.contact_num, IFNULL(NULLIF(a.reporting_manager,''),'--') AS reporting_manager, "
			+ "a.tentative_joining_date, d.name AS bu_name, p.callname as call_name, DATE(a.created_date_time) AS raised_date, "
			+ "CASE WHEN a.status = 1001 THEN 'In Progress' WHEN a.status = 1002 THEN 'Acknowledged' END AS status, "
			+ "a.raised_by, rb.callname as rb_callname,a.work_location, IFNULL(NULLIF(rc.callname,''),'--') AS rc_callname, IFNULL(NULLIF(a.acknowledge_id,''),'--') AS acknowledge_id, DATE(a.acknowledge_date_time) AS acknowledge_date_time, "
			+ "GROUP_CONCAT(ai.asset_name ORDER BY ai.asset_name SEPARATOR ', ') AS asset_names, "
			+ "GROUP_CONCAT(ari.count ORDER BY ai.asset_name SEPARATOR ', ') AS asset_counts, "
			+ "GROUP_CONCAT(IFNULL(ari.remarks, '--') ORDER BY ai.asset_name SEPARATOR ', ') AS asset_remarks, "
			+ "(SELECT COUNT(DISTINCT a1.request_id) FROM test.tbl_hr_asset_requests a1 "
			+ "LEFT JOIN test.tbl_hr_asset_request_items ari1 ON a1.request_id = ari1.request_id "
			+ "LEFT JOIN asset_management.tbl_asset_types ai1 ON ari1.asset_type_id = ai1.asset_type_id "
			+ "WHERE a1.raised_by = :loginId " + "AND a1.status IN (1001, 1002) "
			+ "AND (:dept IS NULL OR a1.department = :dept) " + "AND (:bu IS NULL OR a1.bu = :bu) "
			+ "AND (:fromDate IS NULL OR DATE(a1.created_date_time) BETWEEN :fromDate AND :toDate) "
			+ "AND (:status IS NULL OR a1.status = :status) "
			+ "AND (:assetType IS NULL OR FIND_IN_SET(ai1.asset_type_id, :assetType)) " + ") AS totalcount "
			+ "FROM test.tbl_hr_asset_requests a " + "LEFT JOIN hcladm_prod.tbl_department b ON a.department = b.departmentid "
			+ "LEFT JOIN hcladm_prod.tbl_designation c ON a.designation = c.designationid "
			+ "LEFT JOIN hcladm_prod.tbl_businessunit d ON a.bu = d.businessunitid "
			+ "LEFT JOIN hclhrm_prod.tbl_employee_primary p ON a.reporting_manager = p.employeesequenceno "
			+ "LEFT JOIN hclhrm_prod.tbl_employee_primary rb ON a.raised_by = rb.employeesequenceno "
			+ "LEFT JOIN hclhrm_prod.tbl_employee_primary rc ON a.acknowledge_id = rc.employeesequenceno "
			+ "LEFT JOIN test.tbl_hr_asset_request_items ari ON a.request_id = ari.request_id "
			+ "LEFT JOIN asset_management.tbl_asset_types ai ON ari.asset_type_id = ai.asset_type_id "
			+ "WHERE a.raised_by = :loginId " + "AND a.status IN (1001, 1002) "
			+ "AND (:dept IS NULL OR a.department = :dept) " + "AND (:bu IS NULL OR a.bu = :bu) "
			+ "AND (:fromDate IS NULL OR DATE(a.created_date_time) BETWEEN :fromDate AND :toDate) "
			+ "AND (:status IS NULL OR a.status = :status) " + "AND ari.status = 1001 "
			+ "AND (:assetType IS NULL OR FIND_IN_SET(ai.asset_type_id, :assetType)) "
			+ "GROUP BY a.request_id, a.emp_id, a.emp_name, b.name, c.name, a.contact_num, "
			+ "a.reporting_manager, a.tentative_joining_date, d.name, p.callname, a.created_date_time, "
			+ "a.status, a.raised_by, rb.callname, a.work_location, rc.callname,a.acknowledge_id, a.acknowledge_date_time "
			+ "LIMIT :pageSize OFFSET :pageNo", nativeQuery = true)
	List<Object[]> getRaisedAssetsWithItems(@Param("loginId") Integer loginId, @Param("dept") Integer dept,
			@Param("bu") Integer bu, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate,
			@Param("status") Integer status, @Param("assetType") String assetType, @Param("pageSize") Integer pageSize,
			@Param("pageNo") Integer pageNo);

	@Query(value = "SELECT b.asset_name, a.count, IFNULL(NULLIF(a.remarks, ''),'--') AS remarks "
			+ "FROM test.tbl_hr_asset_request_items a "
			+ "LEFT JOIN asset_management.tbl_asset_types b ON a.asset_type_id = b.asset_type_id "
			+ "WHERE a.request_id = :requestId  AND a.status = 1001 "
			+ "AND (:assetType IS NULL OR b.asset_type_id = :assetType)", nativeQuery = true)
	List<Object[]> getRaisedAssetsItems(@Param("requestId") Integer requestId, @Param("assetType") String assetType);

	@Modifying
	@Transactional
	@Query(value = "UPDATE test.tbl_hr_asset_requests "
			+ "SET status = 1002, acknowledge_id = ?1, acknowledge_date_time = now() "
			+ "WHERE request_id = ?2", nativeQuery = true)
	int getAknowledgeStatus(String acknowledgeId, String requestId);

	@Query(value = "SELECT request_id FROM test.tbl_hr_asset_requests WHERE request_id = :requestId AND status != 1002 FOR UPDATE", nativeQuery = true)
	Optional<Integer> findByRequestIdForUpdate(@Param("requestId") String requestId);

	@Query(value = "SELECT a.bu FROM test.tbl_hr_asset_requests a " + "WHERE a.request_id = ?2 "
			+ "AND a.status in  (1001,1002) " + "AND EXISTS (SELECT 1 " + "FROM test.tbl_asset_user_bu b "
			+ "WHERE b.bu = a.bu " + "AND b.emp_id = ?1 " + "AND b.status = 1001)", nativeQuery = true)
	Optional<Integer> findByBU(String acknowledgeId, String requestId);

	@Query(value = "SELECT a.request_id, a.emp_id, a.emp_name, b.name AS dept_name, "
			+ "c.name AS des_name, a.contact_num, IFNULL(NULLIF(a.reporting_manager, ''), '--') AS reporting_manager, "
			+ "a.tentative_joining_date, d.name AS bu_name, p.callname as call_name, DATE(a.created_date_time) AS raised_date, "
			+ "CASE WHEN a.status = 1001 THEN 'In Progress' WHEN a.status = 1002 THEN 'Acknowledged' END AS status, "
			+ "a.raised_by, rb.callname as rd_callname, a.work_location, IFNULL(NULLIF(rc.callname,''),'--') AS rc_callname, IFNULL(NULLIF(a.acknowledge_id,''),'--') AS acknowledge_id, DATE(a.acknowledge_date_time) AS acknowledge_date_time, "
			+ "GROUP_CONCAT(ai.asset_name ORDER BY ai.asset_name SEPARATOR ', ') AS asset_names, "
			+ "GROUP_CONCAT(ari.count ORDER BY ai.asset_name SEPARATOR ', ') AS asset_counts, "
			+ "GROUP_CONCAT(IFNULL(ari.remarks, '') ORDER BY ai.asset_name SEPARATOR ', ') AS asset_remarks, "
			+ "(SELECT COUNT(DISTINCT a1.request_id) " + "FROM test.tbl_hr_asset_requests a1 "
			+ "LEFT JOIN test.tbl_hr_asset_request_items ari1 ON a1.request_id = ari1.request_id "
			+ "LEFT JOIN asset_management.tbl_asset_types ai1 ON ari1.asset_type_id = ai1.asset_type_id "
			+ "WHERE a1.status IN (1001) AND  a1.bu IN (:bumap) " + "AND (:dept IS NULL OR a1.department = :dept) "
			+ "AND (:bu IS NULL OR a1.bu = :bu) "
			+ "AND (:fromDate IS NULL OR DATE(a1.created_date_time) BETWEEN :fromDate AND :toDate) "
			+ "AND (:assetType IS NULL OR FIND_IN_SET(ai1.asset_type_id, :assetType)) " + ") AS totalcount "
			+ "FROM test.tbl_hr_asset_requests a " + "LEFT JOIN hcladm_prod.tbl_department b ON a.department = b.departmentid "
			+ "LEFT JOIN hcladm_prod.tbl_designation c ON a.designation = c.designationid "
			+ "LEFT JOIN hcladm_prod.tbl_businessunit d ON a.bu = d.businessunitid "
			+ "LEFT JOIN hclhrm_prod.tbl_employee_primary p ON a.reporting_manager = p.employeesequenceno "
			+ "LEFT JOIN hclhrm_prod.tbl_employee_primary rb ON a.raised_by = rb.employeesequenceno "
			+ "LEFT JOIN hclhrm_prod.tbl_employee_primary rc ON a.acknowledge_id = rc.employeesequenceno "
			+ "LEFT JOIN test.tbl_hr_asset_request_items ari ON a.request_id = ari.request_id "
			+ "LEFT JOIN asset_management.tbl_asset_types ai ON ari.asset_type_id = ai.asset_type_id "
			+ "WHERE a.status IN (1001) AND a.bu IN (:bumap) " + "AND (:dept IS NULL OR a.department = :dept) "
			+ "AND (:bu IS NULL OR a.bu = :bu) "
			+ "AND (:fromDate IS NULL OR DATE(a.created_date_time) BETWEEN :fromDate AND :toDate) "
			+ "AND ari.status = 1001 " + "AND (:assetType IS NULL OR FIND_IN_SET(ai.asset_type_id, :assetType)) "
			+ "GROUP BY a.request_id, a.emp_id, a.emp_name, b.name, c.name, a.contact_num, "
			+ "a.reporting_manager, a.tentative_joining_date, d.name, p.callname, a.created_date_time, "
			+ "a.status, a.raised_by, rb.callname, a.work_location, rc.callname, a.acknowledge_id, a.acknowledge_date_time "
			+ "LIMIT :pageSize OFFSET :pageNo", nativeQuery = true)
	List<Object[]> getITPendingApprovals(@Param("bumap") List<Integer> bumap, @Param("dept") Integer dept,
			@Param("bu") Integer bu, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate,
			@Param("assetType") String assetType, @Param("pageSize") Integer pageSize, @Param("pageNo") Integer pageNo);

	@Query(value = "SELECT bu FROM test.tbl_asset_user_bu WHERE emp_id = :empId AND status = 1001", nativeQuery = true)
	List<Integer> mappingBuForPendingApproval(@Param("empId") String empId);

	@Query(value = "SELECT a.request_id, a.emp_id, a.emp_name, b.name AS dept_name, "
			+ "c.name AS des_name, a.contact_num, IFNULL(NULLIF(a.reporting_manager,''),'--') AS reporting_manager, "
			+ "a.tentative_joining_date, d.name AS bu_name, p.callname as call_name, DATE(a.created_date_time) AS raised_date, "
			+ "CASE WHEN a.status = 1001 THEN 'In Progress' WHEN a.status = 1002 THEN 'Acknowledged' END AS status, "
			+ "a.raised_by, rb.callname as rb_callname, a.work_location, IFNULL(NULLIF(rc.callname,''),'--') AS rc_callname, IFNULL(NULLIF(a.acknowledge_id,''),'--') AS acknowledge_id, DATE(a.acknowledge_date_time) AS acknowledge_date_time, "
			+ "GROUP_CONCAT(ai.asset_name ORDER BY ai.asset_name SEPARATOR ', ') AS asset_names, "
			+ "GROUP_CONCAT(ari.count ORDER BY ai.asset_name SEPARATOR ', ') AS asset_counts, "
			+ "GROUP_CONCAT(IFNULL(ari.remarks, '') ORDER BY ai.asset_name SEPARATOR ', ') AS asset_remarks, "
			+ "(SELECT COUNT(DISTINCT a1.request_id) FROM test.tbl_hr_asset_requests a1 "
			+ "LEFT JOIN test.tbl_hr_asset_request_items ari1 ON a1.request_id = ari1.request_id "
			+ "LEFT JOIN asset_management.tbl_asset_types ai1 ON ari1.asset_type_id = ai1.asset_type_id "
			//+ "WHERE a1.acknowledge_id = :loginId " + "AND a1.status IN (1002) "
			+ "WHERE 1 = :loginId " + "AND a1.status IN (1002) "
			+ "AND (:dept IS NULL OR a1.department = :dept) " + "AND (:bu IS NULL OR a1.bu = :bu) "
			+ "AND (:fromDate IS NULL OR DATE(a1.created_date_time) BETWEEN :fromDate AND :toDate) "
			+ "AND (:status IS NULL OR a1.status = :status) "
			+ "AND (:assetType IS NULL OR FIND_IN_SET(ai1.asset_type_id, :assetType)) " + ") AS totalcount "
			+ "FROM test.tbl_hr_asset_requests a " + "LEFT JOIN hcladm_prod.tbl_department b ON a.department = b.departmentid "
			+ "LEFT JOIN hcladm_prod.tbl_designation c ON a.designation = c.designationid "
			+ "LEFT JOIN hcladm_prod.tbl_businessunit d ON a.bu = d.businessunitid "
			+ "LEFT JOIN hclhrm_prod.tbl_employee_primary p ON a.reporting_manager = p.employeesequenceno "
			+ "LEFT JOIN hclhrm_prod.tbl_employee_primary rb ON a.raised_by = rb.employeesequenceno "
			+ "LEFT JOIN hclhrm_prod.tbl_employee_primary rc ON a.acknowledge_id = rc.employeesequenceno "
			+ "LEFT JOIN test.tbl_hr_asset_request_items ari ON a.request_id = ari.request_id "
			+ "LEFT JOIN asset_management.tbl_asset_types ai ON ari.asset_type_id = ai.asset_type_id "
			//+ "WHERE a.acknowledge_id = :loginId " + "AND a.status IN (1002) "
			+ "WHERE 1 = :loginId " + "AND a.status IN (1002) "
			+ "AND (:dept IS NULL OR a.department = :dept) " + "AND (:bu IS NULL OR a.bu = :bu) "
			+ "AND (:fromDate IS NULL OR DATE(a.created_date_time) BETWEEN :fromDate AND :toDate) "
			+ "AND (:status IS NULL OR a.status = :status) " + "AND ari.status = 1001 "
			+ "AND (:assetType IS NULL OR FIND_IN_SET(ai.asset_type_id, :assetType)) "
			+ "GROUP BY a.request_id, a.emp_id, a.emp_name, b.name, c.name, a.contact_num, "
			+ "a.reporting_manager, a.tentative_joining_date, d.name, p.callname, a.created_date_time, "
			+ "a.status, a.raised_by, rb.callname, a.work_location, rc.callname, a.acknowledge_id, a.acknowledge_date_time "
			+ "LIMIT :pageSize OFFSET :pageNo", nativeQuery = true)
	List<Object[]> getITApprovalData(@Param("loginId") Integer loginId, @Param("dept") Integer dept,
			@Param("bu") Integer bu, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate,
			@Param("status") Integer status, @Param("assetType") String assetType, @Param("pageSize") Integer pageSize,
			@Param("pageNo") Integer pageNo);

	@Query(value = "SELECT b.asset_name, " + "IFNULL(NULLIF(a.remarks, ''), '--') AS remarks "
			+ "FROM test.tbl_hr_asset_request_items a "
			+ "LEFT JOIN asset_management.tbl_asset_types b ON a.asset_type_id = b.asset_type_id "
			+ "WHERE a.request_id = :requestId AND a.status = 1001 " + "ORDER BY a.asset_type_id", nativeQuery = true)
	List<Object[]> getAssetNames(@Param("requestId") Integer requestId);

	@Modifying
	@Transactional
	@Query(value = "UPDATE test.tbl_hr_asset_requests SET email_status = :emailStatus, email_response = :emailResponse WHERE request_id = :requestId", nativeQuery = true)
	void updateMailStatus(@Param("emailStatus") String emailStatus, @Param("emailResponse") String emailResponse,
			@Param("requestId") Integer requestId);

	@Query(value = "SELECT " + "    a.bu, " + "    b.it_recipient_mail AS TO_EMAIL, " + "    d.callname AS HR_NAME, "
			+ "    NULLIF( " + "        CONCAT_WS(',', " + "            NULLIF(h.email, ''), "
			+ "            NULLIF(b.it_admin_mail, ''), " + "            NULLIF(e.email, '') " + "        ), '' "
			+ "    ) AS CC_EMAILS, " + "    c.FROM_EMAIL, " + "    c.PASSWORD, " + "    c.HOST, "
			+ "    CASE WHEN c.TLS = 1 THEN 'true' ELSE 'false' END AS TLS, " + "    c.PROTOCOL, " + "    c.PORT, "
			+ "    f.emp_name AS empName, " + "    i.name AS DEPT_NAME, "
			+ "    f.reporting_manager as reportingId, g.callname AS reportingName, "
			+ "    DATE_FORMAT(f.tentative_joining_date, '%Y-%b-%d') AS tentativeJoiningDate "
			+ "FROM test.tbl_asset_hr_bu a " + "LEFT JOIN asset_management.tbl_bu_request_emails b ON a.bu = b.bu_id "
			+ "LEFT JOIN asset_management.tbl_smtp_emails c ON b.email_sno = c.email_sno "
			+ "LEFT JOIN hclhrm_prod.tbl_employee_primary d ON a.emp_id = d.employeesequenceno "
			+ "LEFT JOIN hclhrm_prod.tbl_employee_professional_contact e ON d.employeeid = e.employeeid "
			+ "LEFT JOIN test.tbl_hr_asset_requests f ON a.bu = f.bu "
			+ "LEFT JOIN hclhrm_prod.tbl_employee_primary g ON f.reporting_manager = g.employeesequenceno "
			+ "LEFT JOIN hclhrm_prod.tbl_employee_professional_contact h ON g.employeeid = h.employeeid "
			+ "LEFT JOIN hcladm_prod.tbl_department i ON f.department = i.departmentid " + "WHERE a.emp_id = :loginId "
			+ "AND a.bu = :bu " + "AND f.request_id = :requestId " + "AND a.status = 1001 " + "AND b.status = 1001 "
			+ "AND c.status = 1001 " + "AND d.status = 1001 ", nativeQuery = true)
	List<Object[]> getEmailAddress(@Param("loginId") Integer loginId, @Param("bu") Integer bu,
			@Param("requestId") Integer requestId);

	@Query(value = "SELECT a.bu, g.email AS HR_TO_EMAIL, f.callname AS HR_NAME, "
			+ "NULLIF(CONCAT_WS(',', NULLIF(c.email, ''), NULLIF(d.it_admin_mail, ''), NULLIF(d.it_recipient_mail, '')), '') AS CC_EMAILS, "
			+ "e.FROM_EMAIL, e.PASSWORD, e.HOST, " + "CASE WHEN e.TLS = 1 THEN 'true' ELSE 'false' END AS TLS, "
			+ "e.PROTOCOL, e.PORT, h.employeesequenceno as ack_id, h.callname AS ack_name,a.emp_name,i.name as dept_name,DATE_FORMAT(a.tentative_joining_date, '%Y-%b-%d') AS tentativeJoiningDate,b.employeesequenceno as repId,b.callname as repName "
			+ "FROM test.tbl_hr_asset_requests a "
			+ "LEFT JOIN hclhrm_prod.tbl_employee_primary b ON a.reporting_manager = b.employeesequenceno "
			+ "LEFT JOIN hclhrm_prod.tbl_employee_professional_contact c ON b.employeeid = c.employeeid "
			+ "LEFT JOIN asset_management.tbl_bu_request_emails d ON a.bu = d.bu_id "
			+ "LEFT JOIN asset_management.tbl_smtp_emails e ON d.email_sno = e.email_sno "
			+ "LEFT JOIN hclhrm_prod.tbl_employee_primary f ON a.raised_by = f.employeesequenceno "
			+ "LEFT JOIN hclhrm_prod.tbl_employee_professional_contact g ON f.employeeid = g.employeeid "
			+ "LEFT JOIN hclhrm_prod.tbl_employee_primary h ON a.acknowledge_id = h.employeesequenceno "
			+ "LEFT JOIN hcladm_prod.tbl_department i on a.department=i.departmentid "
			+ "WHERE a.request_id = :requestId AND a.status = 1002 AND e.status=1001 AND d.status=1001 ", nativeQuery = true)
	List<Object[]> getHrEmailAddress(@Param("requestId") String requestId);

	@Modifying
	@Transactional
	@Query(value = "UPDATE test.tbl_hr_asset_requests SET acknowledge_email_status = :emailStatus, acknowledge_email_response = :emailResponse WHERE request_id = :requestId", nativeQuery = true)
	void updateHRMailStatus(@Param("emailStatus") String emailStatus, @Param("emailResponse") String emailResponse,
			@Param("requestId") Integer requestId);

}
