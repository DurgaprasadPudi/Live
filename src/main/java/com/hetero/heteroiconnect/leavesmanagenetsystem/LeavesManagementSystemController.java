package com.hetero.heteroiconnect.leavesmanagenetsystem;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/leavemanagementsystem")
public class LeavesManagementSystemController {

	@PersistenceContext
	  EntityManager entityManager;
	@Autowired 
	LeaveManagenetSystemRepository leavemanagementsystemrepository;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	private DataSource dataSource;

	   
       public void setDataSource(DataSource dataSource) {
           this.jdbcTemplate = new JdbcTemplate(dataSource);
       }
// Getting Leaves For 	
	 
	@RequestMapping(value="getleavetypes",method = RequestMethod.GET)
	public Map<String,Object> GetLeaveTypes()
	{
			Map<String, Object> map = new HashMap<String,Object>();
		List<Object[]> data = leavemanagementsystemrepository.getLeavedata();
		List<Leave_types> use1r = new ArrayList<>();
		 System.out.println("the List of data is::"+data);
		 for(int i = 0 ; i < data.size() ; i++){
			 Leave_types user = new Leave_types();
			 user.setLEAVETYPEID((int) data.get(i)[0]);
			 user.setNAME((String) data.get(i)[1]);
			 use1r.add(user);
			 }
		 if(use1r == null){
				String msg = "Server Error wait some time.";
				map.put("response",use1r);
				map.put("message",msg);
				map.put("status",2);
			}else{
				map.put("response",use1r);
				map.put("message","data retrieved success");
				map.put("status",1);
			}
		return map;
		
	}
	
// Monthly Leaves Data Getting	by Leave type id
	 
	@RequestMapping(value="getleavedatabyid",method = RequestMethod.POST)
	public Map<String,Object> GetLeavesByTypeId(@RequestBody Map<String, Object> data)
	{
			Map<String, Object> map = new HashMap<String,Object>();
			
			String id = "";
		    String Leave = "";
		    
		    String leaveid= (String) data.get("leaveid");
			String leavename = (String) data.get("leavename");
			 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
			   LocalDateTime now = LocalDateTime.now();  
			   System.out.println(dtf.format(now));  
			 
			   int year = getYearFromDate(new Date());
			   
			  String yy = String.valueOf(year);
			   String Sfull = yy+"-01-"+"01";
			   String Efulldate = yy+"-12-"+"31";
			   System.out.println("the Year Is Getting For ::"+leaveid);
			   System.out.println("the Year Is Getting For ::"+Efulldate);
			   
// 2021-12-31
// 2021-01-01		
			   
			   Sfull ="2021-01-01";   
			   Efulldate ="2021-12-31";
			   year = 2021;
			   List<Leaves_Registration> obj = new ArrayList();
			   
			   
			   List<String> objdd = new ArrayList();
			   
			   System.out.println("the Year Is Getting For ::"+leaveid);
			   System.out.println("the Year Is Getting For ::"+Efulldate);
			   System.out.println("the Year Is Getting For ::"+year);
			   System.out.println("the Year Is Getting For ::"+Sfull);
	if(leaveid.equalsIgnoreCase("1"))
	{
	
		List<Object[]> eligibleemps = leavemanagementsystemrepository.getEligibleEmployeesForCl(leaveid,Sfull,year,Efulldate);
		
		 for(int i = 0 ; i < eligibleemps.size() ; i++){
			 Leaves_Registration user = new Leaves_Registration();
			 // employeeid, 1, 2021, QUANTITY, AVAILABLEQTY, USEDQTY, MAXLEAVE, BACKDATE
			 
			 user.setEMPLOYEEID((int) eligibleemps.get(i)[0]);
			 user.setLEAVETYPEID((BigInteger) eligibleemps.get(i)[1]);
			 user.setYEAR((BigInteger) eligibleemps.get(i)[2]);
			 user.setQUANTITY((BigDecimal) eligibleemps.get(i)[3]);
			 user.setAVAILABLEQTY((BigDecimal) eligibleemps.get(i)[4]);
			 user.setUSEDQTY((BigInteger) eligibleemps.get(i)[5]);
			 user.setMAXLEAVE((BigInteger) eligibleemps.get(i)[6]);
			 user.setBACKDATE((BigInteger) eligibleemps.get(i)[7]);
			 user.setCOUNT_WOFF((int) eligibleemps.get(i)[8]);
			 user.setCOUNT_HOLIDAY((int) eligibleemps.get(i)[9]);
			 
			 
			 obj.add(user);
			 }
	
		
		map.put("CLData", obj);
	}
	else if(leaveid.equalsIgnoreCase("2"))
	{
		//Leaves_Registration sl  = leavemanagementsystemrepository.GetEligibilityEmployeesForSL(leaveid,leavename,Sfulldate,Efulldate,year);
		//map.put("SL Data", sl);
		
		List<Object[]> eligibleemps = leavemanagementsystemrepository.getEligiblesl(leaveid,Sfull,year,Efulldate);
		
		 for(int i = 0 ; i < eligibleemps.size() ; i++){
			 Leaves_Registration user = new Leaves_Registration();
			 // employeeid, 1, 2021, QUANTITY, AVAILABLEQTY, USEDQTY, MAXLEAVE, BACKDATE
			 
			 user.setEMPLOYEEID((int) eligibleemps.get(i)[0]);
			 user.setLEAVETYPEID((BigInteger) eligibleemps.get(i)[1]);
			 user.setYEAR((BigInteger) eligibleemps.get(i)[2]);
			 user.setQUANTITY((BigDecimal) eligibleemps.get(i)[3]);
			 user.setAVAILABLEQTY((BigDecimal) eligibleemps.get(i)[4]);
			 user.setUSEDQTY((BigInteger) eligibleemps.get(i)[5]);
			 user.setMAXLEAVE((BigInteger) eligibleemps.get(i)[6]);
			 user.setBACKDATE((BigInteger) eligibleemps.get(i)[7]);
			 BigInteger b1,b2;
			  b1 = (BigInteger) eligibleemps.get(i)[8];
			  b2 = (BigInteger) eligibleemps.get(i)[9];
			 user.setCOUNT_WOFF(b1.intValue());
			 user.setCOUNT_HOLIDAY(b2.intValue());
			 
			 
			 obj.add(user);
		 }
		
		System.out.println(obj);
		map.put("SLData", obj);
		
		
	}
	else
	{
		
	}
	
	
	return map;
	}
	
// 	Get By Employee id Data Primary Data
	
@RequestMapping(value="get_employee_details",method = RequestMethod.POST)	
public 	Map<String, Object> Get_Employee_Primary_data(@RequestBody Map<String, Object> data)
{
	Map<String,Object> obj = new HashMap<String,Object>();
	 String empid= (String) data.get("Empid");
	 String leaveid= (String) data.get("leavetype");
	 // NAME, DOJ, DESI, DEPT
	 int year = getYearFromDate(new Date());
	 year=2021;
 List<Object[]> leavedata = leavemanagementsystemrepository.get_employee_leave_data(empid,year,leaveid);
	 
	 Leave_types lv = new Leave_types();
	 System.out.println("the Leave Size ::"+leavedata.size());
	 
	 if(leavedata.size()!=0)
	 {
	 for(int i=0;i<leavedata.size();i++)
	 {
		 
		 System.out.println("Obulesu"+leavedata.get(i)[5]);
		
		 lv.setQUANTITY((BigDecimal)leavedata.get(i)[0]);
		 lv.setAVAILABLEQTY((BigDecimal)leavedata.get(i)[1]);
		 lv.setUSEDQTY((BigDecimal)leavedata.get(i)[2]);
		 lv.setHold((BigDecimal)leavedata.get(i)[3]);
		 lv.setDAYMODE((short)leavedata.get(i)[4]);
		 lv.setMAXLEAVE((int)leavedata.get(i)[5]);
	 
	 }
	 
	 } 
	 else
	 {
		 double b2=0.0;
		 //BigDecimal b1;
		 BigDecimal b = new BigDecimal(b2);
		
		 lv.setQUANTITY(b);
		 lv.setAVAILABLEQTY(b);
		 lv.setUSEDQTY(b);
		 lv.setHold(b);
		 lv.setDAYMODE((short) 0);
		 lv.setMAXLEAVE(0);
	 }
	 List<Object[]> empdata = leavemanagementsystemrepository.get_employee_data(empid);
	 
	 Emp_primary_details em = new Emp_primary_details();
	 
	 for(int i = 0 ; i < empdata.size() ; i++)
	 {
		
		 System.out.println("Name"+(String)empdata.get(i)[0]);
		 
		 em.setNAME((String)empdata.get(i)[0]);
		 em.setDOJ((String)empdata.get(i)[1]);
		 em.setDESI((String)empdata.get(i)[2]);
		 em.setDEPT((String)empdata.get(i)[3]);
		 em.setEMPLOYMENTTYPE((String)empdata.get(i)[4]);
		 
	 }
	 
	
	 
	 System.out.println("the Leave Size ::"+year);
	 // QUANTITY, AVAILABLEQTY, USEDQTY, HOLD, DAYMODE, MAXLEAVE
	
	 
	 
	 System.out.println("the Leave Data Is From Here :::"+leavedata.size()+"======"+empdata.size());
	 
	if(leavedata.size()==1 || empdata.size()==1 || empdata.size()==0  )
	{
		obj.put("empdata",em);
		obj.put("leavedata", lv);
		obj.put("message","data retrieved success");
		
	}
	else
	{
		obj.put("message","Server Error please try ofter some time");
	}
	 
	 
	 
	return obj;
}



/*
 * @RequestMapping(value="processedleaves") public String
 * LeaveSaveMethod(@RequestBody List<Leaves_Registration> data) {
 * 
 * 
 * 
 * 
 * System.out.println("the Data Is ::"+data.toString());
 * //System.out.println("the Data Is ::"+data.toArray()); String da =null;
 * 
 * List<Leaves_Registration> list = new ArrayList<>();
 * 
 * 
 * Iterator<Map<String, String>> iterator = data.iterator();
 * 
 * while(iterator.hasNext()) { // System.out.println("\nIndia Region - " +
 * regionIndex); Map<String, String> region = iterator.next(); Set<Entry<String,
 * String>> entrySet = region.entrySet();
 * 
 * // for-each loop
 * 
 * for(int i=0;i<data.size();i++){ Leaves_Registration obj = new
 * Leaves_Registration(); for(Entry<String, String> entry : entrySet) {
 * 
 * System.out.println("State : " + entry.getKey() + "\tCapital : " +
 * entry.getValue());
 * 
 * if(entry.getKey().equalsIgnoreCase("EMPLOYEEID")) { String empid =
 * entry.getValue(); obj.setEMPLOYEEID(Integer.valueOf(empid));
 * 
 * list.add(obj); } if(entry.getKey().equalsIgnoreCase("LEAVETYPEID")) { String
 * empid = entry.getValue();
 * 
 * obj.setLEAVETYPEID(new BigInteger(empid));
 * 
 * list.add(obj); } if(entry.getKey().equalsIgnoreCase("YEAR")) { String empid =
 * entry.getValue(); obj.setYEAR(new BigInteger(empid));
 * 
 * list.add(obj); } if(entry.getKey().equalsIgnoreCase("QUANTITY")) { String
 * empid = entry.getValue(); obj.setQUANTITY(new BigDecimal(empid));
 * 
 * list.add(obj); } if(entry.getKey().equalsIgnoreCase("AVAILABLEQTY")) { String
 * empid = entry.getValue(); obj.setAVAILABLEQTY(new BigDecimal(empid));
 * 
 * list.add(obj); } if(entry.getKey().equalsIgnoreCase("USEDQTY")) { String
 * empid = entry.getValue(); obj.setUSEDQTY(new BigInteger(empid));
 * 
 * list.add(obj); } if(entry.getKey().equalsIgnoreCase("MAXLEAVE")) { String
 * empid = entry.getValue(); obj.setMAXLEAVE(new BigInteger(empid));
 * 
 * //list.add(obj); } if(entry.getKey().equalsIgnoreCase("BACKDATE")) { String
 * empid = entry.getValue(); obj.setBACKDATE(new BigInteger(empid));
 * 
 * //list.add(obj); } } list.add(obj);
 * System.out.println("the List data ::"+list); } }
 * System.out.println("the Obulesu Is Comming From ::"+list.toString());
 * if(data.size()!=0) {
 * 
 * for(Map<String, String> emp : data){
 * 
 * Leaves_Registration li = new Leaves_Registration();
 * 
 * System.out.println("The Obulesu From ::"+emp);
 * 
 * 
 * // li.setEMPLOYEEID(emp.getKey(list));
 * 
 * 
 * 
 * } //for(int i=0;i<data.size();i++){
 * 
 * data.get(0); System.out.println("the Data Is ::"+data.get(0));
 * Leaves_Registration emplvs = new Leaves_Registration();
 * 
 * //emplvs.setEMPLOYEEID(i);
 * //System.out.println("the Data is For The Processing ::"+data.get(i)[0]);
 * 
 * //emplvs.add(userskill); //}
 * 
 * da="Not Empty"; } else { da = "Processed Data is Empty"; }
 * 
 * 
 * 
 * return da; }
 */
	
// 

// For CL and SL Records
@RequestMapping(value="processedleaves")
public int LeaveSaveMethod(@RequestBody List<Leaves_Registration>  data) throws ParseException
{
	
	Date date = Calendar.getInstance().getTime();  
    DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");  
    String strDate = dateFormat.format(date); 
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // your template here
    java.util.Date dateStr = formatter.parse(strDate);
    java.sql.Date dateDB = new java.sql.Date(dateStr.getTime());
		String sql="insert into hclhrm_prod_others.tbl_emp_leave_quota(EMPLOYEEID, LEAVETYPEID, YEAR, QUANTITY, AVAILABLEQTY, "
				+ "USEDQTY, HOLD, DAYMODE, MAXLEAVE, BACKDATE, COUNT_WOFF, COUNT_HOLIDAY, STATUS, LOGID, CREATEDBY, "
				+ "DATECREATED, MODIFIEDBY, DATEMODIFIED, VERIFIEDBY, DATEVERIFIED, LUPDATE, MINIMU_LEAVE, FOR_MONTH, LEAVE_MODE)"
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),?,now(),?,now(),now(),?,?,?)";
		List<Object[]> batchlist = new ArrayList<Object[]>();
		for(Leaves_Registration s : data){
			Object[] arraybtc = {s.getEMPLOYEEID(),s.getLEAVETYPEID(),s.getYEAR(),s.getQUANTITY(),s.getAVAILABLEQTY(),s.getUSEDQTY(),
					BigInteger.valueOf(0),BigInteger.valueOf(1),s.getMAXLEAVE(),s.getBACKDATE(),0,0,1001,"12004","12004",
					//strDate,
					"0",
					//strDate,
					"0",
					//strDate,
					//strDate,
					"0","0",
					"OP"};
			batchlist.add(arraybtc);
		}
		int result[] =  jdbcTemplate.batchUpdate(sql,batchlist);
		System.out.println("the Pathc Updated Count Is::"+result);
	//String da="cs";
	return result.length;
}

// WFH,OD,COFF,
@RequestMapping(value="addleavesbyid")
public String SaveEmployeeLave(@RequestBody Leaves_Registration  s) throws ParseException
{

	Date now = new Date();
    String pattern = "yyyy-MM-dd  HH:mm:ss";
    SimpleDateFormat formatter = new SimpleDateFormat(pattern);
    String mysqlDateString = formatter.format(now);
    System.out.println("Mysql's Default Date Format: " + mysqlDateString);
		Leaves_Registration obj = new Leaves_Registration();
		obj.setEMPLOYEEID(s.getEMPLOYEEID());
		obj.setLEAVETYPEID(s.getLEAVETYPEID());
		obj.setYEAR(s.getYEAR());
		obj.setQUANTITY(s.getQUANTITY());
		obj.setAVAILABLEQTY(s.getAVAILABLEQTY());
		obj.setUSEDQTY(s.getUSEDQTY());
		obj.setHOLD(BigInteger.valueOf(0));
		obj.setDAYMODE(BigInteger.valueOf(1));
		obj.setMAXLEAVE(s.getMAXLEAVE());
		obj.setBACKDATE(s.getBACKDATE());
		obj.setCOUNT_WOFF(0);
		obj.setCOUNT_HOLIDAY(0);
		obj.setSTATUS("1001");
		obj.setLOGID("12004");
		obj.setCREATEDBY(s.getCREATEDBY());
		obj.setMODIFIEDBY("0");
		obj.setDATEMODIFIED(mysqlDateString);
		obj.setVERIFIEDBY("0");
		obj.setDATEVERIFIED(mysqlDateString);
		obj.setLUPDATE(mysqlDateString);
		obj.setMINIMU_LEAVE("0");
		obj.setFOR_MONTH("1");obj.setLEAVE_MODE("OP");
	Leaves_Registration count =	leavemanagementsystemrepository.save(obj);
	return "Created";
}


	
	
	
	
// Method For Getting Year 
	public static int getYearFromDate(Date date) {
	    int result = -1;
	    if (date != null) {
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(date);
	        result = cal.get(Calendar.YEAR);
	    }
	    return result;
	}
	
	
	
	
}
