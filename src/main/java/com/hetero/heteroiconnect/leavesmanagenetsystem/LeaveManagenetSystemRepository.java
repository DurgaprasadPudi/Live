package com.hetero.heteroiconnect.leavesmanagenetsystem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface LeaveManagenetSystemRepository extends JpaRepository<Leaves_Registration, Long> {

	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	
	// select LEAVETYPEID,NAME from tbl_leave_type where status='1001' ;
	@Query(value="select LEAVETYPEID,NAME from hclhrm_prod.tbl_leave_type where status='1001' and LEAVETYPEID in(1,2)", nativeQuery = true)
	List<Object[]> getLeavedata();

	/*
	 * @Query(
	 * value="select A.employeeid,1,:year,abs(round(0.0274*DATEDIFF(:efulldate,if(pro.dateofjoin<=:sfulldate,:sfulldate,pro.dateofjoin)+1),0)) as QUANTITY,\r\n"
	 * +
	 * "abs(round(0.0274*DATEDIFF(:efulldate,if(pro.dateofjoin<=:sfulldate,:sfulldate,pro.dateofjoin)+1),0)) as AVAILABLEQTY,0 as USEDQTY\r\n"
	 * + ",1 as MAXLEAVE,3 as BACKDATE\r\n" +
	 * "from hclhrm_prod.tbl_employee_primary A\r\n" +
	 * "left join hclhrm_prod.tbl_employee_ctc B on b.employeeid=A.employeeid\r\n" +
	 * "left join hclhrm_prod.tbl_employee_profile pro on pro.employeeid=A.employeeid\r\n"
	 * +
	 * "left join hcladm_prod.tbl_businessunit bu on bu.businessunitid=a.companyid\r\n"
	 * +
	 * "left join hcladm_prod.tbl_costcenter ce on ce.businessunitid=a.companyid and ce.costcenterid=A.costcenterid\r\n"
	 * +
	 * "left join hclhrm_prod_others.tbl_emp_leave_quota qa on qa.employeeid=a.employeeid\r\n"
	 * + "and qa.year=2021 and qa.leavetypeid=:leaveid\r\n" +
	 * "where pro.dateofjoin between :sfulldate and curdate() and A.status in(1001)\r\n"
	 * +
	 * "and bu.callname in('HYD') and ce.name='OFFICE' and a.companyid not in (25,36) and qa.leavetypeid is  null\r\n"
	 * + "group by bu.name,A.employeesequenceno]", nativeQuery = true)
	 * Leaves_Registration GetEligibilityEmployeesForSL(String leaveid, String
	 * leavename, String sfulldate, String efulldate, int year);
	 */
	@Query(value="select A.employeeid,1,:year,abs(round(0.0274*DATEDIFF(:Efulldate,if(pro.dateofjoin<=:sfulldate,:sfulldate,pro.dateofjoin)+1),0)) as QUANTITY,\r\n"
			+ "	abs(round(0.0274*DATEDIFF(:Efulldate,if(pro.dateofjoin<=:sfulldate,:sfulldate,pro.dateofjoin)+1),0)) as AVAILABLEQTY,0 as USEDQTY\r\n"
			+ "	,1 as MAXLEAVE,3 as BACKDATE,if(a.companyid=15 and a.companyid=16,0,0) as COUNT_WOFF,if(a.companyid=15 and a.companyid=16,0,0) as COUNT_HOLIDAY\r\n"
			+ "	from hclhrm_prod.tbl_employee_primary A\r\n"
			+ "	left join hclhrm_prod.tbl_employee_ctc B on b.employeeid=A.employeeid\r\n"
			+ "	left join hclhrm_prod.tbl_employee_profile pro on pro.employeeid=A.employeeid\r\n"
			+ "	left join hcladm_prod.tbl_businessunit bu on bu.businessunitid=a.companyid\r\n"
			+ "	left join hcladm_prod.tbl_costcenter ce on ce.businessunitid=a.companyid and ce.costcenterid=A.costcenterid\r\n"
			+ "	left join hclhrm_prod_others.tbl_emp_leave_quota qa on qa.employeeid=a.employeeid\r\n"
			+ "	and qa.year=:year and qa.leavetypeid=:leaveid\r\n"
			+ "	where pro.dateofjoin between :sfulldate and curdate() and A.status in(1001)\r\n"
			+ "	and bu.callname in('HYD') and ce.name='OFFICE' and a.companyid not in (25,36) and qa.leavetypeid is  null\r\n"
			+ "	group by bu.name,A.employeesequenceno\r\n"
			+ "", nativeQuery = true)
	List<Object[]> getEligibleEmployeesForCl(@Param(value = "leaveid") String leaveid,@Param(value = "sfulldate") String sfulldate,@Param(value = "year") int year,@Param(value = "Efulldate") String Efulldate );
	

	@Query(value="select A.employeeid,2,:year,abs(round(0.0274*DATEDIFF(:Efulldate,if(pro.dateofjoin<=:sfulldate,:sfulldate,pro.dateofjoin)+1),0)) as QUANTITY,\r\n"
			+ "abs(round(0.0274*DATEDIFF(:Efulldate,if(pro.dateofjoin<=:sfulldate,:sfulldate,pro.dateofjoin)+1),0)) as AVAILABLEQTY,0 as USEDQTY,\r\n"
			+ "1 as MAXLEAVE,3 as BACKDATE, if(a.companyid=15 and a.companyid=16,0,0) as COUNT_WOFF ,if(a.companyid=15 and a.companyid=16,0,0) as COUNT_HOLIDAY\r\n"
			+ "from hclhrm_prod.tbl_employee_primary A\r\n"
			+ "left join hclhrm_prod.tbl_employee_ctc B on b.employeeid=A.employeeid\r\n"
			+ "left join hclhrm_prod.tbl_employee_profile pro on pro.employeeid=A.employeeid\r\n"
			+ "left join hcladm_prod.tbl_businessunit bu on bu.businessunitid=a.companyid\r\n"
			+ "left join hcladm_prod.tbl_costcenter ce on ce.businessunitid=a.companyid and ce.costcenterid=A.costcenterid\r\n"
			+ "left join hclhrm_prod_others.tbl_emp_leave_quota qa on qa.employeeid=a.employeeid\r\n"
			+ "and qa.year=:year and qa.leavetypeid=:leaveid\r\n"
			+ "and B.effectivedate<=date_format(now(),'%Y-%m-%d')\r\n"
			+ "where pro.dateofjoin between :sfulldate and curdate() and A.status in(1001)\r\n"
			+ "and bu.callname in('HYD') and ce.name='OFFICE' and a.companyid not in (25,36) and qa.leavetypeid is   null\r\n"
			+ "and B.gross>21000", nativeQuery = true)
	List<Object[]> getEligiblesl(@Param(value = "leaveid") String leaveid,@Param(value = "sfulldate") String sfulldate,@Param(value = "year") int year,@Param(value = "Efulldate") String Efulldate );

	
	
	
	@Query(value="SELECT A.CALLNAME NAME,DATE_FORMAT(B.DATEOFJOIN,'%d-%m-%Y') DOJ,D.CODE DESI,E.CODE DEPT,EMPLO.NAME 'EMPLOYMENTTYPE'\r\n"
			+ "FROM HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY A\r\n"
			+ "LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFILE B ON A.EMPLOYEEID=B.EMPLOYEEID\r\n"
			+ "LEFT JOIN HCLADM_PROD.TBL_STATUS_CODES STATUS ON A.STATUS=STATUS.STATUS\r\n"
			+ "LEFT JOIN HCLHRM_PROD.tbl_employment_types EMPLO ON A.EMPLOYMENTTYPEID=EMPLO.EMPLOYMENTTYPEID\r\n"
			+ "LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFESSIONAL_DETAILS C ON A.EMPLOYEEID=C.EMPLOYEEID\r\n"
			+ "LEFT JOIN HCLADM_PROD.TBL_DESIGNATION D ON C.DESIGNATIONID=D.DESIGNATIONID\r\n"
			+ "LEFT JOIN HCLADM_PROD.TBL_DEPARTMENT E ON C.DEPARTMENTID=E.DEPARTMENTID\r\n"
			+ "WHERE A.EMPLOYEESEQUENCENO IN (:empid) and a.status in(1001)",nativeQuery = true)
	List<Object[]> get_employee_data(@Param(value = "empid") String empid);

	/*
	 * @Query(value="SELECT C.SHORTNAME,B.QUANTITY\r\n" +
	 * "FROM HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY A\r\n" +
	 * "LEFT JOIN HCLHRM_PROD_OTHERS.TBL_EMP_LEAVE_QUOTA B ON A.EMPLOYEEID=B.EMPLOYEEID\r\n"
	 * + "LEFT JOIN HCLHRM_PROD.TBL_LEAVE_TYPE C ON B.LEAVETYPEID=C.LEAVETYPEID\r\n"
	 * + "WHERE C.STATUS=1001 AND B.YEAR=:year AND A.EMPLOYEESEQUENCENO IN (:empid)"
	 * ,nativeQuery=true) List<Object[]> get_employee_leave_data(String empid,int
	 * year);
	 */
	
	@Query(value="SELECT QUANTITY, AVAILABLEQTY, USEDQTY, HOLD, DAYMODE, MAXLEAVE\r\n"
			+ "FROM hclhrm_prod_others.tbl_emp_leave_quota where employeeid in (select employeeid\r\n"
			+ "from hclhrm_prod.tbl_employee_primary where employeesequenceno=:empid) and leavetypeid=:leaveid and year=:year",nativeQuery=true)
	List<Object[]> get_employee_leave_data(@Param(value = "empid") String empid,@Param(value = "year") int year,@Param(value = "leaveid") String leaveid);
//@Transactional
//int saveAll(List<Leaves_Registration> lst);

	
	
	

	//void saveAll(List<Leaves_Registration> lst);

	//void saveAll(List<Leaves_Registration> lst);
	
	
// SELECT QUANTITY, AVAILABLEQTY, USEDQTY, HOLD, DAYMODE, MAXLEAVE FROM hclhrm_prod_others.tbl_emp_leave_quota where employeeid in (select employeeid
//	from hclhrm_prod.tbl_employee_primary where employeesequenceno='12004') and leavetypeid='14' and year='2022';	
	
	

	

}