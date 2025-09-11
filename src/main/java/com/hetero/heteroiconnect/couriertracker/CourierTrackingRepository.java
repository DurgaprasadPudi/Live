package com.hetero.heteroiconnect.couriertracker;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourierTrackingRepository extends JpaRepository<DummyEntity, Integer> {
//	
//	@Query(value = "select courier_type_id as courierTypeId ,courier_name as CourierName  from courier_tracking.tbl_master_courier_types where status =1001", nativeQuery = true)
//	List<Map<String, Object>> getTypeOfCouriers();
	
	@Query(value = "select courier_type_id, courier_name from courier_tracking.tbl_master_courier_types where status = 1001", nativeQuery = true)
	List<Object[]> getTypeOfCouriers();

	
	@Query(value = "SELECT COUNT(*) FROM courier_tracking.tbl_courier_sender_tracking WHERE docket_no = :docketNo", nativeQuery = true)
	int countByDocketNo(@Param("docketNo") String docketNo);

	@Query(value = "SELECT COUNT(*) FROM courier_tracking.tbl_courier_receiver_tracking WHERE docket_no = :docketNo", nativeQuery = true)
	int countByReceiverDocketNo(@Param("docketNo") String docketNo);

	@Query(value = "SELECT COUNT(*) FROM courier_tracking.tbl_courier_sender_tracking WHERE docket_no = :docketNo AND sender_tracking_id != :senderTrackingId", nativeQuery = true)
	int countSenderDocketNoExistsExceptCurrent(@Param("docketNo") String docketNo, @Param("senderTrackingId") Integer senderTrackingId);

	@Query(value = "SELECT COUNT(*) FROM courier_tracking.tbl_courier_receiver_tracking WHERE docket_no = :docketNo AND receiver_tracking_id != :receiverTrackingId", nativeQuery = true)
	int countReceiverDocketNoExistsExceptCurrent(@Param("docketNo") String docketNo, @Param("receiverTrackingId") Integer receiverTrackingId);

	@Modifying
	@Query(value = "INSERT INTO courier_tracking.tbl_courier_sender_tracking (" +
	        "register_date, courier_type_id, docket_no, sender_name, sender_contact_no, receiver_name, receiver_contact_no, " +
	        "from_location,  to_location, material, comment, created_by, created_timestamp, " +
	        "updated_by, updated_timestamp, last_updated) " +
	        "VALUES (:registerDate, :courierTypeId, :docketNo, :senderName, :senderContactNo, :receiverName, :receiverContactNo, " +
	        ":fromLocation,  :toLocation,  :material, :comment, :createdBy, CURRENT_TIMESTAMP, " +
	        ":updatedBy, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)", nativeQuery = true)
	void insertCourierSenderWithDocket(
	        @Param("registerDate") Date registerDate,
	        @Param("courierTypeId") Integer courierTypeId,
	        @Param("docketNo") String docketNo,
	        @Param("senderName") String senderName,
	        @Param("senderContactNo") String senderContactNo,
	        @Param("receiverName") String receiverName,
	        @Param("receiverContactNo") String receiverContactNo,
	        @Param("fromLocation") String fromLocation,
	        @Param("toLocation") String toLocation,
	        @Param("material") String material,
	        @Param("comment") String comment,
	        @Param("createdBy") String createdBy,
	        @Param("updatedBy") String updatedBy
	);
	@Modifying
	@Query(value = "INSERT INTO courier_tracking.tbl_courier_sender_tracking (" +
	        "register_date, courier_type_id, docket_no, sender_name, sender_contact_no, receiver_name, receiver_contact_no, " +
	        "from_location,to_location, material, comment, created_by, created_timestamp, last_updated) " +
	        "VALUES (:registerDate, :courierTypeId, :docketNo, :senderName, :senderContactNo, :receiverName, :receiverContactNo, " +
	        ":fromLocation,  :toLocation,  :material, :comment, :createdBy, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)", nativeQuery = true)
	void insertCourierSenderWithoutDocket(
	        @Param("registerDate") Date registerDate,
	        @Param("courierTypeId") Integer courierTypeId,
	        @Param("docketNo") String docketNo,
	        @Param("senderName") String senderName,
	        @Param("senderContactNo") String senderContactNo,
	        @Param("receiverName") String receiverName,
	        @Param("receiverContactNo") String receiverContactNo,
	        @Param("fromLocation") String fromLocation,
	        @Param("toLocation") String toLocation,
	        @Param("material") String material,
	        @Param("comment") String comment,
	        @Param("createdBy") String createdBy
	);

	@Modifying
	@Query(value = "INSERT INTO courier_tracking.tbl_courier_receiver_tracking (received_date, receiver_name, courier_type_id, receiver_contact_no, docket_no, sender_details, receiver_location, material_received, comment, created_by, created_timestamp, last_updated, status) VALUES (:receivedDate, :receiverName,:courierTypeId, :receiverContactNo, :docketNo, :senderDetails, :receiverLocation, :materialReceived, :comment, :createdBy, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1001)", nativeQuery = true)
	void insertCourierReceiverTracking(@Param("receivedDate") Date receivedDate,
			@Param("receiverName") String receiverName, @Param("courierTypeId") Integer courierTypeId, @Param("receiverContactNo") String receiverContactNo,
			@Param("docketNo") String docketNo, @Param("senderDetails") String senderDetails,
           @Param("receiverLocation") String receiverLocation,
			@Param("materialReceived") String materialReceived, @Param("comment") String comment,
			@Param("createdBy") String createdBy);

	@Query(value = "SELECT docket_no FROM courier_tracking.tbl_courier_sender_tracking WHERE sender_tracking_id = :senderTrackingId", nativeQuery = true)
	String findExistingDocketBySenderTrackingId(@Param("senderTrackingId") Integer senderTrackingId);

	@Modifying
	@Query(value = "UPDATE courier_tracking.tbl_courier_sender_tracking SET "
	        + "register_date = :registerDate, courier_type_id = :courierTypeId, docket_no = :docketNo, "
	        + "tracking_status_id = 4, sender_name = :senderName, sender_contact_no = :senderContactNo, "
	        + "receiver_name = :receiverName, receiver_contact_no = :receiverContactNo, from_location = :fromLocation, "
	        + "to_location = :toLocation, material = :material, "
	        + "comment = :comment, updated_by = :updatedBy, updated_timestamp = CURRENT_TIMESTAMP, last_updated = CURRENT_TIMESTAMP "
	        + "WHERE sender_tracking_id = :senderTrackingId", nativeQuery = true)
	void updateCourierSenderTrackingWithDocket(@Param("registerDate") Date registerDate,
	        @Param("courierTypeId") Integer courierTypeId, @Param("docketNo") String docketNo,
	        @Param("senderName") String senderName,
	        @Param("senderContactNo") String senderContactNo, @Param("receiverName") String receiverName,
	        @Param("receiverContactNo") String receiverContactNo, @Param("fromLocation") String fromLocation,
	         @Param("toLocation") String toLocation,
	        @Param("material") String material, @Param("comment") String comment,
	        @Param("updatedBy") String updatedBy, @Param("senderTrackingId") Integer senderTrackingId);
	@Modifying
	@Query(value = "UPDATE courier_tracking.tbl_courier_sender_tracking SET "
	        + "register_date = :registerDate, courier_type_id = :courierTypeId, "
	        + "sender_name = :senderName, sender_contact_no = :senderContactNo, "
	        + "receiver_name = :receiverName, receiver_contact_no = :receiverContactNo, from_location = :fromLocation, "
	        + "to_location = :toLocation, material = :material, "
	        + "comment = :comment ,last_updated = CURRENT_TIMESTAMP "
	        + "WHERE sender_tracking_id = :senderTrackingId", nativeQuery = true)
	void updateCourierSenderTrackingWithoutDocket(@Param("registerDate") Date registerDate,
	        @Param("courierTypeId") Integer courierTypeId,
	       @Param("senderName") String senderName,
	        @Param("senderContactNo") String senderContactNo, @Param("receiverName") String receiverName,
	        @Param("receiverContactNo") String receiverContactNo, @Param("fromLocation") String fromLocation,
	         @Param("toLocation") String toLocation,
	        @Param("material") String material, @Param("comment") String comment,
	        @Param("senderTrackingId") Integer senderTrackingId);

	@Modifying
	@Query(value = "UPDATE courier_tracking.tbl_courier_receiver_tracking SET "
			+ "received_date = :receivedDate, receiver_name = :receiverName, courier_type_id = :courierTypeId, receiver_contact_no = :receiverContactNo, "
			+ "docket_no = :docketNo, sender_details = :senderDetails,"
			+ "receiver_location = :receiverLocation, material_received = :materialReceived, comment = :comment, "
			+ "updated_by = :updatedBy, updated_timestamp = CURRENT_TIMESTAMP, "
			+ "last_updated = CURRENT_TIMESTAMP "
			+ "WHERE receiver_tracking_id = :receiverTrackingId", nativeQuery = true)
	void updateCourierReceiverTracking(@Param("receivedDate") Date receivedDate,
			@Param("receiverName") String receiverName, @Param("courierTypeId") Integer courierTypeId, @Param("receiverContactNo") String receiverContactNo,
			@Param("docketNo") String docketNo, @Param("senderDetails") String senderDetails,
			@Param("receiverLocation") String receiverLocation,
			@Param("materialReceived") String materialReceived, @Param("comment") String comment,
			@Param("updatedBy") String updatedBy,
			@Param("receiverTrackingId") Integer receiverTrackingId);
//	
//	@Query(value = "SELECT a.sender_tracking_id AS senderTrackingId, a.register_date AS registerDate, a.courier_type_id AS courierTypeId, b.courier_name AS courierName, a.docket_no AS docketNo, a.tracking_status_id AS trackingStatusId, a.sender_name AS senderId, ep.callname AS senderName, d.name AS dept, a.sender_contact_no AS senderContactNo, a.receiver_name AS receiverName, a.receiver_contact_no AS receiverContactNo, a.from_location AS fromLocation, a.to_location AS toLocation, a.material AS material, a.comment AS comment FROM courier_tracking.tbl_courier_sender_tracking a LEFT JOIN courier_tracking.tbl_master_courier_types b ON a.courier_type_id = b.courier_type_id LEFT JOIN hclhrm_prod.tbl_employee_primary ep ON ep.employeesequenceno = a.sender_name LEFT JOIN hclhrm_prod.tbl_employee_professional_details pro ON ep.employeeid = pro.employeeid LEFT JOIN hcladm_prod.tbl_department d ON pro.departmentid = d.departmentid WHERE a.status = 1001 AND (:search IS NULL OR a.docket_no LIKE CONCAT('%', :search, '%') OR ep.callname LIKE CONCAT('%', :search, '%') OR d.name LIKE CONCAT('%', :search, '%') OR a.sender_contact_no LIKE CONCAT('%', :search, '%') OR a.receiver_name LIKE CONCAT('%', :search, '%') OR a.receiver_contact_no LIKE CONCAT('%', :search, '%') OR a.from_location LIKE CONCAT('%', :search, '%') OR a.to_location LIKE CONCAT('%', :search, '%') OR a.material LIKE CONCAT('%', :search, '%') OR b.courier_name LIKE CONCAT('%', :search, '%') OR a.tracking_status_id LIKE CONCAT('%', :search, '%') OR a.register_date LIKE CONCAT('%', :search, '%'))",
//		    countQuery = "SELECT COUNT(*) FROM courier_tracking.tbl_courier_sender_tracking a LEFT JOIN courier_tracking.tbl_master_courier_types b ON a.courier_type_id = b.courier_type_id LEFT JOIN hclhrm_prod.tbl_employee_primary ep ON ep.employeesequenceno = a.sender_name LEFT JOIN hclhrm_prod.tbl_employee_professional_details pro ON ep.employeeid = pro.employeeid LEFT JOIN hcladm_prod.tbl_department d ON pro.departmentid = d.departmentid WHERE a.status = 1001 AND (:search IS NULL OR a.docket_no LIKE CONCAT('%', :search, '%') OR ep.callname LIKE CONCAT('%', :search, '%') OR d.name LIKE CONCAT('%', :search, '%') OR a.sender_contact_no LIKE CONCAT('%', :search, '%') OR a.receiver_name LIKE CONCAT('%', :search, '%') OR a.receiver_contact_no LIKE CONCAT('%', :search, '%') OR a.from_location LIKE CONCAT('%', :search, '%') OR a.to_location LIKE CONCAT('%', :search, '%') OR a.material LIKE CONCAT('%', :search, '%') OR b.courier_name LIKE CONCAT('%', :search, '%') OR a.tracking_status_id LIKE CONCAT('%', :search, '%') OR a.register_date LIKE CONCAT('%', :search, '%'))",
//		    nativeQuery = true)
//		Page<Map<String, Object>> findByAllFields(@Param("search") String search, Pageable pageable);
//

//	@Query(value = "SELECT a.receiver_tracking_id AS receiverTrackingId, a.received_date AS receivedDate, a.receiver_name AS receiverId, ep.callname AS receiverName, d.name AS dept, a.receiver_contact_no AS receiverContactNo, a.receiver_location AS receiverLocation, a.docket_no AS docketNo, a.sender_details AS senderDetails, a.material_received AS materialReceived, a.comment AS comment, a.created_by AS createdBy, a.updated_by AS updatedBy, a.courier_type_id AS courierTypeId, ct.courier_name AS courierName FROM courier_tracking.tbl_courier_receiver_tracking a LEFT JOIN courier_tracking.tbl_master_courier_types ct ON a.courier_type_id = ct.courier_type_id LEFT JOIN hclhrm_prod.tbl_employee_primary ep ON ep.employeesequenceno = a.receiver_name LEFT JOIN hclhrm_prod.tbl_employee_professional_details pro ON ep.employeeid = pro.employeeid LEFT JOIN hcladm_prod.tbl_department d ON pro.departmentid = d.departmentid WHERE a.status = 1001 AND (:search IS NULL OR :search = '' OR ep.callname LIKE CONCAT('%', :search, '%') OR d.name LIKE CONCAT('%', :search, '%') OR a.receiver_contact_no LIKE CONCAT('%', :search, '%') OR a.receiver_location LIKE CONCAT('%', :search, '%') OR a.docket_no LIKE CONCAT('%', :search, '%') OR a.sender_details LIKE CONCAT('%', :search, '%') OR a.material_received LIKE CONCAT('%', :search, '%') OR a.comment LIKE CONCAT('%', :search, '%') OR ct.courier_name LIKE CONCAT('%', :search, '%') OR a.received_date LIKE CONCAT('%', :search, '%'))",
//		    countQuery = "SELECT COUNT(*) FROM courier_tracking.tbl_courier_receiver_tracking a LEFT JOIN courier_tracking.tbl_master_courier_types ct ON a.courier_type_id = ct.courier_type_id LEFT JOIN hclhrm_prod.tbl_employee_primary ep ON ep.employeesequenceno = a.receiver_name LEFT JOIN hclhrm_prod.tbl_employee_professional_details pro ON ep.employeeid = pro.employeeid LEFT JOIN hcladm_prod.tbl_department d ON pro.departmentid = d.departmentid WHERE a.status = 1001 AND (:search IS NULL OR :search = '' OR ep.callname LIKE CONCAT('%', :search, '%') OR d.name LIKE CONCAT('%', :search, '%') OR a.receiver_contact_no LIKE CONCAT('%', :search, '%') OR a.receiver_location LIKE CONCAT('%', :search, '%') OR a.docket_no LIKE CONCAT('%', :search, '%') OR a.sender_details LIKE CONCAT('%', :search, '%') OR a.material_received LIKE CONCAT('%', :search, '%') OR a.comment LIKE CONCAT('%', :search, '%') OR ct.courier_name LIKE CONCAT('%', :search, '%') OR a.received_date LIKE CONCAT('%', :search, '%'))",
//		    nativeQuery = true)
//
//		Page<Map<String, Object>> findReceiverTrackingList(@Param("search") String search, Pageable pageable);

//	@Query(value = "SELECT employeesequenceno AS id, callname AS name FROM hclhrm_prod.tbl_employee_primary WHERE employeeid NOT IN (0, 1) AND status IN (1001, 1061, 1092, 1401) AND (callname LIKE CONCAT('%', :name, '%') OR employeesequenceno LIKE CONCAT('%', :name, '%')) LIMIT 10", nativeQuery = true)
//		List<Map<String, Object>> searchEmployeesByName(@Param("name") String name);
	
	@Query(value = "SELECT employeesequenceno AS id, callname AS name FROM hclhrm_prod.tbl_employee_primary WHERE employeeid NOT IN (0, 1) AND status IN (1001, 1061, 1092, 1401) AND (callname LIKE CONCAT('%', :name, '%') OR employeesequenceno LIKE CONCAT('%', :name, '%')) LIMIT 10", nativeQuery = true)
	List<Object[]> searchEmployeesByName(@Param("name") String name);


//	@Query(value = "SELECT a.register_date AS `Registered Date`, b.courier_name AS `Courier Name`, a.docket_no AS `Docket No`, ep.callname AS `Sender Name`, d.name AS `Department`, a.sender_contact_no AS `Sender ContactNo`, a.receiver_name AS `Receiver Name`, a.receiver_contact_no AS `Receiver ContactNo`, a.from_location AS `From Location`, a.to_location AS `To Location`, a.material AS `Material`, a.comment AS `Comment` FROM courier_tracking.tbl_courier_sender_tracking a LEFT JOIN courier_tracking.tbl_master_courier_types b ON a.courier_type_id = b.courier_type_id LEFT JOIN hclhrm_prod.tbl_employee_primary ep ON ep.employeesequenceno = a.sender_name LEFT JOIN hclhrm_prod.tbl_employee_professional_details pro ON ep.employeeid = pro.employeeid LEFT JOIN hcladm_prod.tbl_department d ON pro.departmentid = d.departmentid WHERE a.status = 1001", nativeQuery = true)
//	List<Map<String, Object>> findAllSenderDataForExport();
	
	@Query(value = "SELECT a.register_date, b.courier_name, a.docket_no, ep.callname, d.name, " +
            "a.sender_contact_no, a.receiver_name, a.receiver_contact_no, a.from_location, " +
            "a.to_location, a.material, a.comment " +
            "FROM courier_tracking.tbl_courier_sender_tracking a " +
            "LEFT JOIN courier_tracking.tbl_master_courier_types b ON a.courier_type_id = b.courier_type_id " +
            "LEFT JOIN hclhrm_prod.tbl_employee_primary ep ON ep.employeesequenceno = a.sender_name " +
            "LEFT JOIN hclhrm_prod.tbl_employee_professional_details pro ON ep.employeeid = pro.employeeid " +
            "LEFT JOIN hcladm_prod.tbl_department d ON pro.departmentid = d.departmentid " +
            "WHERE a.status = 1001 and a.created_by = :loginid", nativeQuery = true)
List<Object[]> findAllSenderDataForExport(@Param("loginid") int loginid);


@Query(value = "SELECT a.received_date, ep.callname, d.name, a.receiver_contact_no, " +
        "a.receiver_location, a.docket_no, a.sender_details, " +
        "a.material_received, a.comment " +
        "FROM courier_tracking.tbl_courier_receiver_tracking a " +
        "LEFT JOIN hclhrm_prod.tbl_employee_primary ep ON ep.employeesequenceno = a.receiver_name " +
        "LEFT JOIN hclhrm_prod.tbl_employee_professional_details pro ON ep.employeeid = pro.employeeid " +
        "LEFT JOIN hcladm_prod.tbl_department d ON pro.departmentid = d.departmentid " +
        "WHERE a.status = 1001 and a.created_by = :loginid", nativeQuery = true)
List<Object[]> findAllReceiverDataForExport(@Param("loginid") int loginid);


	
//	@Query(value = "SELECT a.received_date AS `Received Date`, a.receiver_name AS `Receiver Id`, ep.callname AS `Receiver Name`, d.name AS `Department`, a.receiver_contact_no AS `Receiver ContactNo`, a.receiver_location AS `Receiver Location`, a.docket_no AS `Docket No`, a.sender_details AS `Sender Details`, a.material_received AS `Material Received`, a.comment AS `Comment` FROM courier_tracking.tbl_courier_receiver_tracking a LEFT JOIN hclhrm_prod.tbl_employee_primary ep ON ep.employeesequenceno = a.receiver_name LEFT JOIN hclhrm_prod.tbl_employee_professional_details pro ON ep.employeeid = pro.employeeid LEFT JOIN hcladm_prod.tbl_department d ON pro.departmentid = d.departmentid WHERE a.status = 1001", nativeQuery = true)
//		List<Map<String, Object>> findAllReceiverDataForExport();
//	
//	@Query(value = "SELECT a.employeesequenceno AS EmpCode, a.callname AS Fullname, a.employeeid, b.designationid, e.name AS DESIGNATION, b.departmentid, d.name AS DEPT, a.companyid AS Divisionid, f.name AS DIVISION, g.employmenttypeid, h.BANKIFC, h.BANKACC, i.bankName FROM hclhrm_prod.tbl_employee_primary a LEFT JOIN hclhrm_prod.tbl_employee_professional_details b ON a.employeeid = b.employeeid LEFT JOIN hclhrm_prod.tbl_employee_profile_businessunit c ON c.employeeid = b.employeeid LEFT JOIN hcladm_prod.tbl_department d ON d.departmentid = b.departmentid LEFT JOIN hcladm_prod.tbl_designation e ON e.designationid = b.designationid LEFT JOIN hcladm_prod.tbl_businessunit f ON f.businessunitid = a.companyid LEFT JOIN hclhrm_prod.tbl_employment_types g ON g.employmenttypeid = a.employmenttypeid LEFT JOIN hclhrm_prod_others.tbl_emp_bank_ifc h ON h.empid = b.employeeid LEFT JOIN hcladm_prod.tbl_bank_details i ON i.bankid = h.bankid WHERE a.employeesequenceno = :employeeseq AND a.status = 1001 GROUP BY a.employeesequenceno", nativeQuery = true)
//	List<Map<String, Object>> getEmployeeDetailsByEmpSeq(@Param("employeeseq") int employeeseq);
//
//	
	@Query(value = "SELECT a.employeesequenceno AS EmpCode, a.callname AS Fullname, a.employeeid, b.designationid, e.name AS DESIGNATION, b.departmentid, d.name AS DEPT, a.companyid AS Divisionid, f.name AS DIVISION, g.employmenttypeid, h.BANKIFC, h.BANKACC, i.bankName FROM hclhrm_prod.tbl_employee_primary a LEFT JOIN hclhrm_prod.tbl_employee_professional_details b ON a.employeeid = b.employeeid LEFT JOIN hclhrm_prod.tbl_employee_profile_businessunit c ON c.employeeid = b.employeeid LEFT JOIN hcladm_prod.tbl_department d ON d.departmentid = b.departmentid LEFT JOIN hcladm_prod.tbl_designation e ON e.designationid = b.designationid LEFT JOIN hcladm_prod.tbl_businessunit f ON f.businessunitid = a.companyid LEFT JOIN hclhrm_prod.tbl_employment_types g ON g.employmenttypeid = a.employmenttypeid LEFT JOIN hclhrm_prod_others.tbl_emp_bank_ifc h ON h.empid = b.employeeid LEFT JOIN hcladm_prod.tbl_bank_details i ON i.bankid = h.bankid WHERE a.employeesequenceno = :employeeseq AND a.status = 1001 GROUP BY a.employeesequenceno", nativeQuery = true)
	List<Object[]> getEmployeeDetailsByEmpSeq(@Param("employeeseq") int employeeseq);


}
