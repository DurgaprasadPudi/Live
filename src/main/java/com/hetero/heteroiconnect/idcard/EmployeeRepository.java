package com.hetero.heteroiconnect.idcard;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

 

@Repository
public interface EmployeeRepository extends JpaRepository<DummyEntity, Long> {
	 

	  
	 

	@Query(value = "SELECT quantity,sum(usedqty+hold) Total FROM hclhrm_prod_others.tbl_emp_leave_quota t WHERE employeeid = ?1 AND leavetypeid = ?2 AND year = ?3", nativeQuery = true)
	List<Object[]> findLeaveQuotasByEmployeeIdAndLeaveTypeIdAndYear(int employeeId, int leaveTypeId, int year);

 

	// id cards
//	@Query(value = "SELECT " + "p.CALLNAME AS 'NAME', " + "IFNULL(DES.NAME, 'NA') AS 'DESIGNATION', "
//			+ "IFNULL(DEP.NAME, 'NA') AS 'DEPARTMENT', " + "IFNULL(bb.BLOODGROUPNAME, 'NA') AS 'BLOODGROUPNAME' "
//			+ "FROM hclhrm_prod.tbl_employee_primary p "
//			+ "LEFT JOIN hclhrm_prod.tbl_employee_professional_details DD ON p.employeeid = dd.employeeid "
//			+ "LEFT JOIN HCLADM_PROD.TBL_DEPARTMENT DEP ON DD.DEPARTMENTID = DEP.DEPARTMENTID "
//			+ "LEFT JOIN HCLADM_PROD.TBL_DESIGNATION DES ON DD.DESIGNATIONID = DES.DESIGNATIONID "
//			+ "LEFT JOIN hclhrm_prod.tbl_employee_personalinfo per ON p.employeeid = per.employeeid "
//			+ "LEFT JOIN hcladm_prod.tbl_bloodgroups bb ON per.bloodgroupid = bb.bloodgroupid "
//			+ "WHERE p.employeesequenceno = :empId", nativeQuery = true)
//	List<Object[]> getEmployeeIdCards(@Param("empId") String empId);
	
	@Query(value = "SELECT " +
	        "P.employeesequenceno AS EMPLOYEEID, " +
	        "P.callname AS NAME, " +
	        "BU.NAME AS BUNAME, " +
	        "PRO.DATEOFJOIN, " +
	        "COST.NAME AS COSTCENTER, " +
	        "CASE \r\n" + 
	        "    WHEN BU.CODE = 'HHC' THEN 'HHC'\r\n" + 
	        "    WHEN BU.BUSINESSUNITID = 42 THEN 'AZISTACOMPOSITES'\r\n" + 
	        "    WHEN BU.CODE = 'AZISTA' THEN 'AZISTA'\r\n" + 
	        "    WHEN BU.BUSINESSUNITID = 44 THEN 'AZISTABST'\r\n" + 
	        "    ELSE 'DEFAULT'\r\n" + 
	        "END AS CODE , " +
	        "IF(IFNULL(D.DESIGNATIONID, '0') != '0', 'TRUE', 'FALSE') AS DESIGNATIONCHECK, " +
	        "IF(IFNULL(D.DEPARTMENTID, '0') != '0', 'TRUE', 'FALSE') AS DEPARTMENTCHECK, " +
	        "IFNULL(dep.NAME, 'NA') AS DEPARTMENT_NAME, " +
	        "IFNULL(des.name, 'NA') AS DESIGNATION_NAME, " +
	        "IFNULL(id.addressId, 1) AS ADDRESSID, " +
	        "IFNULL(id.BloodGroupId, 0) AS BLOODGROUPID, " +
	        "IFNULL(id.employee_image, 'NA') AS IMAGEPATH, " +
	        "IFNULL(CASE \r\n" + 
	        "    WHEN P.COMPANYID NOT IN (14,17,18,24,27,28,29,36,41,42,44) THEN 'HETERO HEALTHCARE LIMITED'\r\n" + 
	        "    WHEN P.COMPANYID IN (14,17,18,24,27,28,29,36,41) THEN 'AZISTA INDUSTRIES PVT.LTD'\r\n" + 
	        "    WHEN P.COMPANYID = 42 THEN 'AZISTA BST AEROSPACE PRIVATE LIMITED'\r\n" + 
	        "    WHEN P.COMPANYID = 44 THEN 'AZISTA BST AEROSPACE PRIVATE LIMITED'\r\n" + 
	        "END, 'NA') AS COMPANYNAME,\r\n"  +
	        "IFNULL(address.ADDRESS, 'NA') AS ADDRESS, " +
	        "IFNULL(bb.BLOODGROUPNAME, 'NA') AS BLOODGROUPNAME, " +
	        " IF(BU.CODE = 'HHC', 'HHC.png',\r\n" + 
	        "    IF(BU.BUSINESSUNITID = 42, 'AZISTACOMPOSITES.png',\r\n" + 
	        "        IF(BU.BUSINESSUNITID = 44 , 'AZISTABST.png',\r\n" + 
	        "            IF(BU.CODE = 'AZISTA', 'AZISTA.png', 'DEFAULT-LOGO.JPG')))) AS BackgroundImagename ,id.employee_image as Profile " + 
	        "FROM hclhrm_prod.tbl_employee_primary P " +
	        "LEFT JOIN hclhrm_prod.tbl_employee_profile PRO ON PRO.EMPLOYEEID = P.EMPLOYEEID " +
	        "LEFT JOIN hclhrm_prod.tbl_employee_professional_details D ON P.EMPLOYEEID = D.EMPLOYEEID " +
	        "LEFT JOIN hcladm_prod.tbl_costcenter COST ON P.COSTCENTERID = COST.COSTCENTERID " +
	        "LEFT JOIN hcladm_prod.tbl_businessunit BU ON BU.BUSINESSUNITID = P.COMPANYID " +
	        "LEFT JOIN hclhrm_prod.tbl_employee_personal_contact ADDRESS ON P.EMPLOYEEID = ADDRESS.EMPLOYEEID " +
	        "LEFT JOIN test.tbl_employee_idcard_list id ON id.employeeid = P.employeesequenceno " +
	        "LEFT JOIN HCLADM_PROD.TBL_DESIGNATION des ON D.designationid = des.designationid " +
	        "LEFT JOIN HCLADM_PROD.TBL_DEPARTMENT dep ON D.departmentid = dep.departmentid " +
	        "LEFT JOIN hcladm_prod.tbl_bloodgroups bb ON bb.BLOODGROUPID = id.BloodGroupId " +
	        "LEFT JOIN test.tbl_idcard_address address ON address.ID = id.addressId " +
	        "WHERE BU.CALLNAME = 'HYD' AND BU.BUSINESSUNITID != 25 AND P.employeesequenceno = :empId " +
	        "GROUP BY P.employeesequenceno, P.callname, BU.NAME, PRO.DATEOFJOIN, COST.NAME, BU.CODE, D.DESIGNATIONID, D.DEPARTMENTID " +
	        "ORDER BY PRO.DATEOFJOIN ASC", nativeQuery = true)
	List<Object[]> getEmployeeIdCards(@Param("empId") String empId);



	@Query(value = "select ID,COMPANYNAME,ADDRESS from test.tbl_idcard_address where status=1001", nativeQuery = true)
	List<Object[]> getAddress();

	@Query(value = "SELECT BLOODGROUPID,BLOODGROUPNAME FROM hcladm_prod.tbl_bloodgroups t", nativeQuery = true)
	List<Object[]> getBloodGroups();

	@Query(value = "SELECT P.employeesequenceno AS EMPLOYEEID, " + 
            "P.callname AS NAME, " + 
            "BU.NAME AS BUNAME, " + 
            "PRO.DATEOFJOIN, " + 
            "COST.NAME AS COSTCENTER, " +   
            "CASE " + 
            "    WHEN BU.CODE = 'HHC' THEN 'HHC' " + 
            "    WHEN BU.BUSINESSUNITID = 42 THEN 'AZISTACOMPOSITES' " + 
            "    WHEN BU.CODE = 'AZISTA' THEN 'AZISTA' " + 
            "    WHEN BU.BUSINESSUNITID = 44 THEN 'AZISTABST' " + 
            "    ELSE 'DEFAULT' " + 
            "END AS CODE, " + 
            "IF(IFNULL(D.DESIGNATIONID, '0') != '0', 'TRUE', 'FALSE') AS DESIGNATIONCHECK, " + 
            "IF(IFNULL(D.DEPARTMENTID, '0') != '0', 'TRUE', 'FALSE') AS DEPARTMENTCHECK, " + 
            "IFNULL(dep.NAME, 'NA') AS DEPARTMENT_NAME, " + 
            "IFNULL(des.name, 'NA') AS DESIGNATION_NAME, " + 
            "IFNULL(id.addressId, 1) AS ADDRESSID, " + 
            "IFNULL(id.BloodGroupId, 0) AS BLOODGROUPID, " + 
            "IFNULL(id.employee_image, 'NA') AS IMAGEPATH, " + 
            "IFNULL(CASE \r\n" + 
	        "    WHEN P.COMPANYID NOT IN (14,17,18,24,27,28,29,36,41,42,44) THEN 'HETERO HEALTHCARE LIMITED'\r\n" + 
	        "    WHEN P.COMPANYID IN (14,17,18,24,27,28,29,36,41) THEN 'AZISTA INDUSTRIES PVT.LTD'\r\n" + 
	        "    WHEN P.COMPANYID = 42 THEN 'AZISTA BST AEROSPACE PRIVATE LIMITED'\r\n" + 
	        "    WHEN P.COMPANYID = 44 THEN 'AZISTA BST AEROSPACE PRIVATE LIMITED'\r\n" + 
	        "END, 'NA') AS COMPANYNAME,\r\n"  +
            "IFNULL(address.ADDRESS, 'NA') AS ADDRESS, " + 
            "IFNULL(bb.BLOODGROUPNAME, 'NA') AS BLOODGROUPNAME, " + 
            " IF(BU.CODE = 'HHC', 'HHC.png',\r\n" + 
	        "    IF(BU.BUSINESSUNITID = 42, 'AZISTACOMPOSITES.png',\r\n" + 
	        "        IF(BU.BUSINESSUNITID = 44, 'AZISTABST.png',\r\n" + 
	        "            IF(BU.CODE = 'AZISTA' , 'AZISTA.png', 'DEFAULT-LOGO.JPG')))) AS BackgroundImagename  " + 
	 
            "FROM hclhrm_prod.tbl_employee_primary P " + 
            "LEFT JOIN hclhrm_prod.tbl_employee_profile PRO ON PRO.EMPLOYEEID = P.EMPLOYEEID " + 
            "LEFT JOIN hclhrm_prod.tbl_employee_professional_details D ON P.EMPLOYEEID = D.EMPLOYEEID " + 
            "LEFT JOIN hcladm_prod.tbl_costcenter COST ON P.COSTCENTERID = COST.COSTCENTERID " + 
            "LEFT JOIN hcladm_prod.tbl_businessunit BU ON BU.BUSINESSUNITID = P.COMPANYID " + 
            "LEFT JOIN hclhrm_prod.tbl_employee_personal_contact ADDRESS ON P.EMPLOYEEID = ADDRESS.EMPLOYEEID " + 
            "LEFT JOIN test.tbl_employee_idcard_list id ON id.employeeid = P.employeesequenceno " + 
            "LEFT JOIN HCLADM_PROD.TBL_DESIGNATION des ON D.designationid = des.designationid " + 
            "LEFT JOIN HCLADM_PROD.TBL_DEPARTMENT dep ON D.departmentid = dep.departmentid " + 
            "LEFT JOIN hcladm_prod.tbl_bloodgroups bb ON bb.BLOODGROUPID = id.BloodGroupId " + 
            "LEFT JOIN test.tbl_idcard_address address ON address.ID = id.addressId " + 
            "WHERE BU.CALLNAME = 'HYD' " + 
            "AND BU.BUSINESSUNITID != 25 " + 
            "AND P.status = 1001 " + 
            "GROUP BY P.employeesequenceno, P.callname, BU.NAME, PRO.DATEOFJOIN, COST.NAME, " + 
            "BU.CODE, D.DESIGNATIONID, D.DEPARTMENTID " + 
            "ORDER BY PRO.DATEOFJOIN ASC", 
    nativeQuery = true)
List<Object[]> getEmployeeDetails();

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO test.tbl_employee_idcard_list (employeeid, addressId, BloodGroupId, employee_image, status, lastupdate) "
			+ "VALUES (:employeeId, :addressId, :bloodGroupId, :employeeImagePath, 1001, CURRENT_TIMESTAMP) "
			+ "ON DUPLICATE KEY UPDATE addressId = VALUES(addressId), " + "BloodGroupId = VALUES(BloodGroupId), "
			+ "employee_image = CASE WHEN :employeeImagePath IS NOT NULL THEN VALUES(employee_image) ELSE employee_image END, "
			+ "status = VALUES(status), lastupdate = CURRENT_TIMESTAMP", nativeQuery = true)
	void insertOrUpdateEmployeeDetails(@Param("employeeId") String employeeId, @Param("addressId") String addressId,
			@Param("bloodGroupId") String bloodGroupId, @Param("employeeImagePath") String employeeImagePath);

}