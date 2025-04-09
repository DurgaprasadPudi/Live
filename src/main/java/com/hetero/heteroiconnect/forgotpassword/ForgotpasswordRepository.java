package com.hetero.heteroiconnect.forgotpassword;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ForgotpasswordRepository   extends JpaRepository<Forgotpassword, Long>{
	 
    @Query(value = "select a.employeesequenceno EmpID,count(*) COUNT,ifnull(b.mobile,0) MOBILE,IF(length(ifnull(b.mobile,0))>=10,length(ifnull(b.mobile,0)),0) MOBLENGTH from hclhrm_prod.tbl_employee_primary a left join hclhrm_prod.tbl_employee_personal_contact b on a.employeeid=b.employeeid where a.employeesequenceno=:empID and a.status=1001", nativeQuery = true)
    Forgotpassword empverify(@Param("empID") String LOGINID);
    
    
    @Transactional
    @Modifying (clearAutomatically = true)
    @Query(value = "insert into test.tbl_employee_forgot_pass (EMPLOYEEID,OLDPASSWORD, NEWPASSWORD, OTP, MOBLE, ALTMOBILE,TIMESTAMP) "
    		+ "select :empID,'NA','NA',:OTP,:mobile,:mobile,now() from Dual ", nativeQuery = true)
    int findbyOTPINSERT(@Param("empID") String LOGINID,@Param("OTP") String OTP,@Param("mobile") String Mobile);
    
    @Query(value = "select count(*) VALID, IF(TIMEDIFF(TIME(now()),TIME(TIMESTAMP))<=TIME(concat('00',VALID,'00')),1,0) OTPEXPIRE,TIMEDIFF(TIME(now()),TIME(TIMESTAMP) ) TIME from test.tbl_employee_forgot_pass where employeeid=:empID and OTP=:OTP and status=1001 ", nativeQuery = true)
    String  otpverify(@Param("empID") String LOGINID,@Param("OTP") String OTP);
    
    
    @Transactional
    @Modifying (clearAutomatically = true)
    @Query(value = "update test.tbl_employee_forgot_pass A  JOIN hclhrm_prod.tbl_employee_login B  on B.employeecode=A.employeeid  SET A.OLDPASSWORD=B.PASSWORD ,  A.NEWPASSWORD=:password ,  A.STATUS=1002 where A.OTP=:OTP AND A.employeeid=:empID and B.employeecode=:empID and b.employeeid!=1 ", nativeQuery = true)
    int findbyforgotstatusclose(@Param("empID") String LOGINID,@Param("OTP") String OTP,@Param("password") String password);
   
    @Transactional
    @Modifying (clearAutomatically = true)
    @Query(value = "insert into hclhrm_prod.tbl_employee_login(EMPLOYEEID, EMPLOYEECODE, PASSWORD, STATUS, LOGID, CREATEDBY, DATEMODIFIED,LUPDATE ) select employeeid,employeesequenceno,md5(:password),'1001',01,1001,now(),now() from hclhrm_prod.tbl_employee_primary where employeesequenceno in(:empID)  ON DUPLICATE KEY UPDATE EMPLOYEECODE=:empID, PASSWORD=md5(:password),STATUS=1001,  LUPDATE=now() ,LOGID=1001 ", nativeQuery = true)
    int findbyforgotpassword(@Param("empID") String LOGINID,@Param("password") String password);
    
}
