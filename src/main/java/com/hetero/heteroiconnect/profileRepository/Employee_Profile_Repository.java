package com.hetero.heteroiconnect.profileRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import net.sf.json.JSONArray;

@Repository
public class Employee_Profile_Repository {
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	private DataSource dataSource;

	   
       public void setDataSource(DataSource dataSource) {
           this.jdbcTemplate = new JdbcTemplate(dataSource);
       }
	 
	@SuppressWarnings("rawtypes")
	public int EmployeeRequest(LinkedHashMap map) throws SQLException
	{
	  
		
		int count = 0;
		boolean flag=false;
		 
			 String RID="0";
			try {
				String EMPINFO="SELECT IFNULL(MAX(RID)+1,'1') RID FROM hclhrm_prod_others.tbl_employee_profile_request" ;
				 List<Map<String, Object>>  rows = jdbcTemplate.queryForList(EMPINFO.toString());
				// STATUS, DISPLAYNAME, HRMSEMPLOYEEID, EmpCode, Fullname, DIVISION, DEPT, DESIGNATION, STATUS, EmploymentType
				  for (@SuppressWarnings("rawtypes") Map row : rows) {
					  
					  RID= row.get("RID").toString();
					  flag=true;
		        }
				}catch(Exception err){
					System.out.println("Exception at reverse" +err);
				}
		if(flag)
		{
		 
		String Employeeid=map.get("EMPLOYEEID").toString();
		String REQUESTTYPE=map.get("REQUESTTYPE").toString();
	 
     String Query = "INSERT  INTO hclhrm_prod_others.tbl_employee_profile_request (RID,EMPLOYEEID, REQUESTTYPE, FIELD_NAME, FIELD_VALUE, FIELD_DISPLAY,EMPLOYEE_REQUEST_DATE) SELECT "+RID+",?,?,?,?,?,NOW() FROM DUAL ";
     PreparedStatement preparedStatement = null;
     Connection connection = dataSource.getConnection();
     preparedStatement =connection.prepareStatement(Query);
 
		  Set set = map.entrySet();
	      // Get an iterator
	      Iterator i = set.iterator();
	      // Display elements
	      while(i.hasNext()) {
	         Map.Entry me = (Map.Entry)i.next();
	          
	          if(!me.getKey().toString().equalsIgnoreCase("requesttype")&&!me.getKey().toString().equalsIgnoreCase("employeeid"))
		        {
		         
		        	String FIELD_NAME=me.getKey().toString();
		        	String FIELD_VALUE=me.getValue().toString();
		        	String FIELD_DISPLAY=me.getValue().toString();
		        	
		        	if(FIELD_NAME.equalsIgnoreCase("COMMUNICATION_STATE_ID"))
		        	{
		        		FIELD_DISPLAY=map.get("COMMUNICATION_STATE_NAME").toString();
		        	}
		        	
		        	if(FIELD_NAME.equalsIgnoreCase("COMMUNICATION_CITY_ID"))
		        	{
		        		FIELD_DISPLAY=map.get("COMMUNICATION_CITY_NAME").toString();
		        	}
		        	 
		        	 

		        	if(FIELD_NAME.equalsIgnoreCase("PERMANENT_STATE_ID"))
		        	{
		        		FIELD_DISPLAY=map.get("PERMANENT_STATE_NAME").toString();
		        	}
		        	
		        	if(FIELD_NAME.equalsIgnoreCase("PERMANENT_CITY_ID"))
		        	{
		        		FIELD_DISPLAY=map.get("PERMANENT_CITY_NAME").toString();
		        	}
		        	
		        	
		        	
		        	if(FIELD_NAME.equalsIgnoreCase("ICE_STATE_ID"))
		        	{
		        		FIELD_DISPLAY=map.get("ICE_STATE_NAME").toString();
		        	}
		        	
		        	if(FIELD_NAME.equalsIgnoreCase("ICE_CITY_ID"))
		        	{
		        		FIELD_DISPLAY=map.get("ICE_CITY_NAME").toString();
		        	}
		        	
		        	  System.out.println(Employeeid);
	 	              System.out.println(REQUESTTYPE);
		        	  System.out.println(FIELD_NAME);
		     	      System.out.println(FIELD_VALUE);
		     	      System.out.println(FIELD_DISPLAY);
		     	      
		     	   //   System.out.println("<----->");
		        	
		        	//me.getValue()
		     	    
		     	     preparedStatement.setString(1, Employeeid);
		     	     preparedStatement.setString(2, REQUESTTYPE);
		     	     preparedStatement.setString(3, FIELD_NAME);
		     	     preparedStatement.setString(4, FIELD_VALUE);
		     	     preparedStatement.setString(5, FIELD_DISPLAY);
		             
		     	    preparedStatement.addBatch();
		     	   
		        } 
	       
	      }
		
	      int [] affectedRecords  = preparedStatement.executeBatch();
	      
	      count=1;
	      
	    //  System.out.println(affectedRecords);
	      
	      
		}
		
		else
		{
			count=0; 
		}
	      
	      jdbcTemplate.getDataSource().getConnection().close();
		 
		return count;
	}
	
	
	@SuppressWarnings("rawtypes")
	public int first_PAN_Login_EmployeeRequest(LinkedHashMap map) throws SQLException
	{
	  
		int count = 0;
		boolean flag=false;
		
		        boolean check=false;
			    String RID="0";
			    int q=0;
			    String FLAG="";
				String PANTYPE=map.get("PANTYPE").toString();
				String Employeeid=map.get("EMPLOYEEID").toString();
				String REQUESTTYPE=map.get("REQUESTTYPE").toString();
				String PAN=map.get("PAN").toString();
				//String EXPECTEDDATE=map.get("EXPECTEDDATE").toString();
				
				String EXPECTEDDATE = map.get("EXPECTEDDATE") != null ? map.get("EXPECTEDDATE").toString() : "";

				if (EXPECTEDDATE.trim().isEmpty()) {
				    // Handle case where EXPECTEDDATE is null, empty, or contains only spaces
				    System.out.println("EXPECTEDDATE is null, empty, or contains only spaces.");
				    EXPECTEDDATE="0000-00-00";
				    
				} else {
				    // Handle case where EXPECTEDDATE has a valid value
				    System.out.println("EXPECTEDDATE is valid: " + EXPECTEDDATE);
				}
	           
	           if(PANTYPE.equalsIgnoreCase("NO"))
	           {
	        	   FLAG="N";
	        	   check=true;
	           }
	           else if(PANTYPE.equalsIgnoreCase("NEW"))
	           {
	        	   FLAG="P";
	        	   check=true;
	           }
	           else if(PANTYPE.equalsIgnoreCase("YES"))
	           {
	        	   FLAG="A";
	        	  // check=true;
	        	   q=1;
	           }
			 
			 
	          if(check) {
			 
			try {
				String EMPINFO="SELECT CONVERT(IFNULL(MAX(RID)+1,'1'),CHAR) RID FROM hclhrm_prod_others.tbl_employee_profile_request" ;
				 List<Map<String, Object>>  rows = jdbcTemplate.queryForList(EMPINFO.toString());
				// STATUS, DISPLAYNAME, HRMSEMPLOYEEID, EmpCode, Fullname, DIVISION, DEPT, DESIGNATION, STATUS, EmploymentType
				  for (@SuppressWarnings("rawtypes") Map row : rows) {
					  
					  RID= row.get("RID").toString();
					  flag=true;
		        }
				}catch(Exception err){
					System.out.println("Exception at reverse" +err);
				}
		  
		if(flag)
		{
		    
//     String Query = "INSERT  INTO hclhrm_prod_others.tbl_employee_profile_request"
//     		+ " (RID,EMPLOYEEID, REQUESTTYPE, FIELD_NAME, FIELD_VALUE, FIELD_DISPLAY,EMPLOYEE_REQUEST_DATE) "
//     		+ "SELECT '"+RID+"','"+Employeeid+"','"+REQUESTTYPE+"','PAN','"+PAN+"','"+PAN+"',NOW() FROM DUAL ";
//          // q=jdbcTemplate.update(Query);
			
			 String Query = "INSERT  INTO hclhrm_prod_others.tbl_employee_profile_request (RID,EMPLOYEEID, REQUESTTYPE, FIELD_NAME, FIELD_VALUE, FIELD_DISPLAY,EMPLOYEE_REQUEST_DATE) SELECT "+RID+",?,?,?,?,?,NOW() FROM DUAL ";
		     PreparedStatement preparedStatement = null;
		     Connection connection = dataSource.getConnection();
		     preparedStatement =connection.prepareStatement(Query);
		      
		     
            Set set = map.entrySet();
  	      // Get an iterator
  	      Iterator i = set.iterator();
  	      // Display elements
  	      while(i.hasNext()) {
  	         Map.Entry me = (Map.Entry)i.next();
  	          
  	          if(!me.getKey().toString().equalsIgnoreCase("requesttype")&&!me.getKey().toString().equalsIgnoreCase("employeeid")&&!me.getKey().toString().equalsIgnoreCase("PANTYPE")&&!me.getKey().toString().equalsIgnoreCase("EXPECTEDDATE"))
  		        {
  		         
  		        	String FIELD_NAME=me.getKey().toString();
  		        	String FIELD_VALUE=me.getValue().toString();
  		        	String FIELD_DISPLAY=me.getValue().toString();
  		        	 
  		        	  System.out.println(Employeeid);
  	 	              System.out.println(REQUESTTYPE);
  		        	  System.out.println(FIELD_NAME);
  		     	      System.out.println(FIELD_VALUE);
  		     	      System.out.println(FIELD_DISPLAY);
  		     	      
  		     	   //   System.out.println("<----->");
  		        	
  		        	//me.getValue()
  		     	    
  		     	     preparedStatement.setString(1, Employeeid);
  		     	     preparedStatement.setString(2, REQUESTTYPE);
  		     	     preparedStatement.setString(3, FIELD_NAME);
  		     	     preparedStatement.setString(4, FIELD_VALUE);
  		     	     preparedStatement.setString(5, FIELD_DISPLAY);
  		             
  		     	    preparedStatement.addBatch();
  		     	   
  		        } 
  	       
  	      }
  		
  	      int [] affectedRecords  = preparedStatement.executeBatch();
  	      
           
  	       q=1;            
           
          
		}
           
	          }
           
	      String PANQUERY = "INSERT  INTO hclhrm_prod_others.tbl_employee_pan_verify (RID,EMPLOYEEID,PANTYPE, PAN, FLAG,EXPECTEDDATE) SELECT '"+RID+"','"+Employeeid+"','"+PANTYPE+"','"+PAN+"','"+FLAG+"','"+EXPECTEDDATE+"' FROM DUAL ";
	      
	      int q1=jdbcTemplate.update(PANQUERY);
	      
	      
	      if(q==1&&q1==1)
	      {
	    	  count=1;
	      }
	      
	    //  System.out.println(affectedRecords);
	      
		else
		{
			count=0; 
		}
	      
	      jdbcTemplate.getDataSource().getConnection().close();
		 
		return count;
	}
	
	
	@SuppressWarnings("rawtypes")
	public int Profilepic_Remove(String empid)
	{
		 int count=0;
		 count=jdbcTemplate.update("DELETE FROM hclhrm_prod_others.tbl_iconnect_profile_pics where employeeid='"+empid+"'");
		return count;
	}
	
	
	

	@SuppressWarnings("rawtypes")
	public int Employee_Request_Approve(LinkedHashMap map)
	{
		System.out.println(map);
		
		String APPROVED_BY= (String) map.get("ACTION_BY");
		
		map.remove("ACTION_BY");
		
		//StringBuffer coloums=new StringBuffer();
		StringBuffer params=new StringBuffer();
		 
		 int count=0;
		 
		 
		 String TYPE=map.get("REQUESTTYPE").toString().equalsIgnoreCase("COMMADD")?
				 "COMMADD":map.get("REQUESTTYPE").toString().equalsIgnoreCase("PERADD")?
				 "PERADD":map.get("REQUESTTYPE").toString().equalsIgnoreCase("ICEADD")?
				 "ICEADD":map.get("REQUESTTYPE").toString().equalsIgnoreCase("BANKADD")?"BANKADD":
					 map.get("REQUESTTYPE").toString().equalsIgnoreCase("PANADD")?
					"PANADD":map.get("REQUESTTYPE").toString().equalsIgnoreCase("ADHARADD")?"ADHARADD":"";
		 
		 boolean flag=false;
		 
		try {
			 
			String tables="";
			
			String History="";
			
			String sub_History="";
			
			String tables2="";
			String sub_bank_tables="";
			 
			  Set set = map.entrySet();
		      // Get an iterator
		      Iterator i = set.iterator();
		      // Display elements
		      while(i.hasNext()) {
		         Map.Entry me = (Map.Entry)i.next();
		          
		          if(!me.getKey().toString().equalsIgnoreCase("REQUESTTYPE")&&!me.getKey().toString().equalsIgnoreCase("EMPLOYEEID")&&!me.getKey().toString().equalsIgnoreCase("ACTIONTYPE"))
			        {
			        	String FIELD_NAME=me.getKey().toString();
			        	String FIELD_VALUE=me.getValue().toString();
			        	//String FIELD_DISPLAY=me.getValue().toString();
			          
			        	
			        	if(TYPE.equalsIgnoreCase("COMMADD"))
			        	{
			        		if(FIELD_NAME.equalsIgnoreCase("COMMUNICATION_CITY_ID"))
				        	{
				        		FIELD_NAME="COMMUNICATIONLOCATIONID";
				        		//System.out.println(FIELD_NAME);
				        	}
				        	  if(!FIELD_NAME.equalsIgnoreCase("COMMUNICATION_STATE_ID")&&!FIELD_NAME.equalsIgnoreCase("COMMUNICATION_STATE_NAME")&&!FIELD_NAME.equalsIgnoreCase("COMMUNICATION_CITY_NAME"))
				        	{
				        		params.append(FIELD_NAME+"="+"\""+FIELD_VALUE+"\" ,");
				        	}
				        	  
				        	  flag=true;
			        	}
			        	
			        	
			        	if(TYPE.equalsIgnoreCase("PERADD"))
			        	{
			        	  if(FIELD_NAME.equalsIgnoreCase("PERMANENT_CITY_ID"))
			        	{
			        		FIELD_NAME="PERMANENTLOCATIONID";
			        		//System.out.println(FIELD_NAME);
			        	}
			        	  if(!FIELD_NAME.equalsIgnoreCase("PERMANENT_STATE_ID")&&!FIELD_NAME.equalsIgnoreCase("PERMANENT_STATE_NAME")&&!FIELD_NAME.equalsIgnoreCase("PERMANENT_CITY_NAME"))
			        	{
			        		params.append(FIELD_NAME+"="+"\""+FIELD_VALUE+"\" ,");
			        	}
			        	  
			        	  flag=true;
			        	  
			        	}
			        	if(TYPE.equalsIgnoreCase("ICEADD"))
			        	{
			        		
			        	  if(FIELD_NAME.equalsIgnoreCase("ICE_CITY_ID"))
			        	{
			        		FIELD_NAME="LOCATIONID";
			        		//System.out.println(FIELD_NAME);
			        	}
			        	  if(!FIELD_NAME.equalsIgnoreCase("ICE_STATE_ID")&&!FIELD_NAME.equalsIgnoreCase("ICE_STATE_NAME")&&!FIELD_NAME.equalsIgnoreCase("ICE_CITY_NAME")&&!FIELD_NAME.equalsIgnoreCase("RELATIONNAME"))
			        	 {
			        		params.append(FIELD_NAME+"="+"\""+FIELD_VALUE+"\" ,");
			        	 }
			        	  
			        	  flag=true;
			        	  
			        	}
			        	if(TYPE.equalsIgnoreCase("BANKADD"))
			        	{
			        		  if(!FIELD_NAME.equalsIgnoreCase("BANKNAME")&&!FIELD_NAME.equalsIgnoreCase("FILENAME")&&!FIELD_NAME.equalsIgnoreCase("FILEPATH"))
					        	{
					        		params.append(FIELD_NAME+"="+"\""+FIELD_VALUE+"\" ,");
					        	}
			        		  
			        		 // flag=true;
			        	}
			        	
			        	if(TYPE.equalsIgnoreCase("PANADD")&&!FIELD_NAME.equalsIgnoreCase("FILENAME")&&!FIELD_NAME.equalsIgnoreCase("FILEPATH"))
			        	{
			        		   
					      params.append(FIELD_NAME+"="+"\""+FIELD_VALUE+"\" ,");
					      
					      flag=true;
			        	}
			        	
			        	//params.append(FIELD_NAME+"="+"\""+FIELD_VALUE+"\" ,");
			        } 
		          
		      }
			
		      System.out.println("FINAL----****"+params);
		      
		      if(map.get("REQUESTTYPE").toString().equalsIgnoreCase("COMMADD")||map.get("REQUESTTYPE").toString().equalsIgnoreCase("PERADD")||map.get("REQUESTTYPE").toString().equalsIgnoreCase("ICEADD")||map.get("REQUESTTYPE").toString().equalsIgnoreCase("BANKADD")||map.get("REQUESTTYPE").toString().equalsIgnoreCase("PANADD"))
				{
					if(map.get("ACTIONTYPE").toString().equalsIgnoreCase("A"))
					{
						if(map.get("REQUESTTYPE").toString().equalsIgnoreCase("ICEADD"))
						{
							History="INSERT INTO hclhrm_prod.tbl_employee_ice_contact_history (EMPLOYEEID, FIRSTNAME, LASTNAME, RELATIONID, EMAIL, PHONE, EXT, MOBILE,  ADDRESS, ADDRESS2, ADDRESS3, ADDRESS4, LOCATIONID, ZIP, LOGID, CREATEDBY, DATECREATED,  MODIFIEDBY, DATEMODIFIED, VERIFIEDBY, DATEVERIFIED, LUPDATE)  SELECT  EMPLOYEEID, FIRSTNAME, LASTNAME, RELATIONID, EMAIL, PHONE, EXT, MOBILE, ADDRESS,   ADDRESS2, ADDRESS3, ADDRESS4, LOCATIONID, ZIP, LOGID, CREATEDBY,  DATECREATED, MODIFIEDBY, DATEMODIFIED, VERIFIEDBY, DATEVERIFIED, LUPDATE FROM  hclhrm_prod.tbl_employee_ice_contact where EMPLOYEEID in(SELECT EMPLOYEEID FROM hclhrm_prod.tbl_employee_primary where employeesequenceno in("+map.get("EMPLOYEEID").toString()+"))  ";
							
							tables="UPDATE hclhrm_prod.tbl_employee_ice_contact SET "+params.toString().replaceAll(",$", "")+" WHERE EMPLOYEEID IN(SELECT EMPLOYEEID FROM hclhrm_prod.tbl_employee_primary where employeesequenceno="+map.get("EMPLOYEEID").toString()+") ";
						}
						else if(map.get("REQUESTTYPE").toString().equalsIgnoreCase("COMMADD")||map.get("REQUESTTYPE").toString().equalsIgnoreCase("PERADD"))
						{
							History="INSERT INTO hclhrm_prod.tbl_employee_personal_contact_history (EMPLOYEEID, EMAIL, PHONE, EXT, MOBILE, PERMANENTADDRESS, PERMANENTADDRESS2, PERMANENTADDRESS3, PERMANENTADDRESS4, PERMANENTLOCATIONID, PERMANENTZIP, COMMUNICATIONADDRESS, COMMUNICATIONADDRESS2, COMMUNICATIONADDRESS3, COMMUNICATIONADDRESS4, COMMUNICATIONLOCATIONID, COMMUNICATIONZIP, LOGID, CREATEDBY, DATECREATED, MODIFIEDBY, DATEMODIFIED, VERIFIEDBY, DATEVERIFIED, LUPDATE)  SELECT EMPLOYEEID, EMAIL, PHONE, EXT, MOBILE, PERMANENTADDRESS, PERMANENTADDRESS2, PERMANENTADDRESS3, PERMANENTADDRESS4, PERMANENTLOCATIONID, PERMANENTZIP, COMMUNICATIONADDRESS, COMMUNICATIONADDRESS2, COMMUNICATIONADDRESS3, COMMUNICATIONADDRESS4, COMMUNICATIONLOCATIONID, COMMUNICATIONZIP, LOGID, CREATEDBY, DATECREATED, MODIFIEDBY, DATEMODIFIED, VERIFIEDBY, DATEVERIFIED, LUPDATE FROM hclhrm_prod.tbl_employee_personal_contact where EMPLOYEEID in(SELECT EMPLOYEEID FROM hclhrm_prod.tbl_employee_primary where employeesequenceno in("+map.get("EMPLOYEEID").toString()+")) ";
							
							tables="UPDATE hclhrm_prod.tbl_employee_personal_contact SET "+params.toString().replaceAll(",$", "")+" WHERE EMPLOYEEID IN(SELECT EMPLOYEEID FROM hclhrm_prod.tbl_employee_primary where employeesequenceno="+map.get("EMPLOYEEID").toString()+") ";
						}
						else if(map.get("REQUESTTYPE").toString().equalsIgnoreCase("BANKADD"))
						{
							
							
							if(map.get("BANKID")!=null&&map.get("BANKID").toString().length()!=0||(map.get("BANKACC")!=null&&map.get("BANKACC").toString().length()!=0))
							{
								
								if(map.get("BANKID")!=null&&map.get("BANKACC")!=null)
								{
										
									 tables="UPDATE hclhrm_prod.tbl_employee_other_detail SET BANKID='"+map.get("BANKID").toString()+"',ACCOUNTNO='"+map.get("BANKACC").toString()+"' WHERE EMPLOYEEID IN(SELECT EMPLOYEEID FROM hclhrm_prod.tbl_employee_primary where employeesequenceno="+map.get("EMPLOYEEID").toString()+") ";
								}
								else
								{
									     if((map.get("BANKID")!=null))
											{
									    	 
									    	 tables="UPDATE hclhrm_prod.tbl_employee_other_detail SET BANKID='"+map.get("BANKID").toString()+"'  WHERE EMPLOYEEID IN(SELECT EMPLOYEEID FROM hclhrm_prod.tbl_employee_primary where employeesequenceno="+map.get("EMPLOYEEID").toString()+") ";
												
											}
									     else if(map.get("BANKACC")!=null)
									     {
									    	 	
									    	 tables="UPDATE hclhrm_prod.tbl_employee_other_detail SET ACCOUNTNO='"+map.get("BANKACC").toString()+"' WHERE EMPLOYEEID IN(SELECT EMPLOYEEID FROM hclhrm_prod.tbl_employee_primary where employeesequenceno="+map.get("EMPLOYEEID").toString()+") ";
									     }
								}
							 
								   
								flag=true;
							}
							
							  History="INSERT INTO hclhrm_prod.tbl_employee_other_detail_history (EMPLOYEEID, PFNO, ESINO, PFUAN, INSURANCENO, BANKID, ACCOUNTNO, FILEID, CREATEDBY, DATECREATED, MODIFIEDBY, DATEMODIFIED, VERIFIEDBY, DATEVERIFIED, LOGID, LUPDATE)  SELECT * FROM hclhrm_prod.tbl_employee_other_detail where employeeid in(SELECT EMPLOYEEID FROM hclhrm_prod.tbl_employee_primary where employeesequenceno in("+map.get("EMPLOYEEID").toString()+")) ";
							
							  sub_History="INSERT INTO hclhrm_prod_others.tbl_emp_bank_ifc_history (EMPID, BANKID, BANKIFC, BANKACC, STATUS, LUPDATE)  SELECT EMPID, BANKID, BANKIFC, BANKACC, STATUS, LUPDATE FROM hclhrm_prod_others.tbl_emp_bank_ifc where EMPID in(SELECT EMPLOYEEID FROM hclhrm_prod.tbl_employee_primary where employeesequenceno in("+map.get("EMPLOYEEID").toString()+")) ";
							
							 sub_bank_tables="UPDATE hclhrm_prod_others.tbl_emp_bank_ifc SET "+params.toString().replaceAll(",$", "")+"  WHERE EMPID IN(SELECT EMPLOYEEID FROM hclhrm_prod.tbl_employee_primary where employeesequenceno="+map.get("EMPLOYEEID").toString()+") ";
						
						
						
//							 if(map.get("REQUESTTYPE").toString().equalsIgnoreCase("BANKADD"))
//								{
										//count=jdbcTemplate.update(tables);
									   int history1=jdbcTemplate.update(History);
									   
									   int History2=jdbcTemplate.update(sub_History);
									   
										count=jdbcTemplate.update(sub_bank_tables);
								//}
						
						
						}
						else if(map.get("REQUESTTYPE").toString().equalsIgnoreCase("PANADD"))
						{
								tables="UPDATE hclhrm_prod.tbl_employee_personalinfo SET  "+params.toString().replaceAll(",$", "")+"  WHERE EMPLOYEEID IN(SELECT EMPLOYEEID FROM hclhrm_prod.tbl_employee_primary where employeesequenceno="+map.get("EMPLOYEEID").toString()+") ";
                        
								History="INSERT INTO hclhrm_prod.tbl_employee_personalinfo_history (EMPLOYEEID, PLACEOFBIRTH, DOBASPERTYPEID, DOBASPER, PASSPORTNO, AADHAARCARDNO, AADHAARUID, AADHAARNAME, PLACEOFISSUE, VALIDITY, PAN, DRIVINGLICENSENO, LOCATIONID, NATIONALITYID, RELIGIONID, HEIGHT, WEIGHT, POWEROFGLASSRIGHT, POWEROFGLASSLEFT, ANYMAJORISSUES, IDENTIFICATIONMARKS1, IDENTIFICATIONMARKS2, MOTHERTONGUE, MEMBERSHIP, COURTOFLAW, DATEOFMARRIAGE, BLOODGROUPID, MARITALID, CREATEDBY, DATECREATED, MODIFIEDBY, DATEMODIFIED, VERIFIEDBY, DATEVERIFIED, LOGID, LUPDATE) SELECT  EMPLOYEEID, PLACEOFBIRTH, DOBASPERTYPEID, DOBASPER, PASSPORTNO, AADHAARCARDNO, AADHAARUID, AADHAARNAME, PLACEOFISSUE, VALIDITY, PAN, DRIVINGLICENSENO, LOCATIONID, NATIONALITYID, RELIGIONID, HEIGHT, WEIGHT, POWEROFGLASSRIGHT, POWEROFGLASSLEFT, ANYMAJORISSUES, IDENTIFICATIONMARKS1, IDENTIFICATIONMARKS2, MOTHERTONGUE, MEMBERSHIP, COURTOFLAW, DATEOFMARRIAGE, BLOODGROUPID, MARITALID, CREATEDBY, DATECREATED, MODIFIEDBY, DATEMODIFIED, VERIFIEDBY, DATEVERIFIED, LOGID, LUPDATE FROM hclhrm_prod.tbl_employee_personalinfo where  employeeid in(SELECT EMPLOYEEID FROM hclhrm_prod.tbl_employee_primary where employeesequenceno in("+map.get("EMPLOYEEID").toString()+")) ";
						}
						 if(flag)
			        	{
							  int history1=jdbcTemplate.update(History);
							 
							  count=jdbcTemplate.update(tables);
			        	}
						
						 
					}
					
					String Appendquery="";
					if(map.get("ACTIONTYPE").toString().equalsIgnoreCase("R"))
					{
						Appendquery=" ,REASON='"+map.get("REASON")+"' ";
					}
					
					
					if(map.get("REQUESTTYPE").toString().equalsIgnoreCase("PANADD")){
						
					  String tablespan="UPDATE  hclhrm_prod_others.tbl_employee_pan_verify SET FLAG='"+map.get("ACTIONTYPE")+"' WHERE RID IN(SELECT RID from hclhrm_prod_others.tbl_employee_profile_request  where employeeid='"+map.get("EMPLOYEEID").toString()+"' AND EMP_FLAG='P' AND REQUESTTYPE='"+map.get("REQUESTTYPE").toString()+"'  GROUP BY EMPLOYEEID) ";
					  jdbcTemplate.update(tablespan);	
					}
					
					System.out.println(tables);
					System.out.println(tables2);
					
					tables2="UPDATE hclhrm_prod_others.tbl_employee_profile_request set  EMP_FLAG='"+map.get("ACTIONTYPE")+"', HR_FLAG='"+map.get("ACTIONTYPE")+"',HR_REQUEST_DATE=now(),ACTION_BY='"+APPROVED_BY+"' "+Appendquery+"  where employeeid='"+map.get("EMPLOYEEID").toString()+"' and EMP_FLAG='P' and REQUESTTYPE='"+map.get("REQUESTTYPE").toString()+"' ";
					
				}
		    //  System.out.println(tables);
		      int count1=jdbcTemplate.update(tables2);
			 
			}catch(Exception err){
				System.out.println("Exception at reverse" +err);
			}
		return count;
	}
	
	
	
	
	@SuppressWarnings("rawtypes")
	public JSONArray Request_Status(String Empid)
	{
		JSONArray empid= new JSONArray();
		net.sf.json.JSONObject ADD=new net.sf.json.JSONObject();
		try {
			String EMPINFO="SELECT REQUESTTYPE,EMP_FLAG FLAG  FROM hclhrm_prod_others.tbl_employee_profile_request where EMPLOYEEID='"+Empid+"'  AND EMP_FLAG='P' GROUP BY EMPLOYEEID,REQUESTTYPE ";
			 List<Map<String, Object>>  rows = jdbcTemplate.queryForList(EMPINFO.toString());
			  for (@SuppressWarnings("rawtypes") Map row : rows) {
				  ADD=new net.sf.json.JSONObject();
				  //TYPE, DATE, Available, ENABLE
				  ADD.put("REQUESTTYPE",row.get("REQUESTTYPE"));
				  ADD.put("FLAG",row.get("FLAG"));
				  empid.add(ADD);
	        }
			}catch(Exception err){
				System.out.println("Exception at reverse" +err);
			}
		return empid;
	}
	
	
	@SuppressWarnings("rawtypes")
	public JSONArray Request_Data(String Empid,String Requesttype)
	{
		JSONArray empid= new JSONArray();
		net.sf.json.JSONObject ADD=new net.sf.json.JSONObject();
		try {
			String EMPINFO="SELECT FIELD_NAME, FIELD_VALUE, FIELD_DISPLAY  FROM hclhrm_prod_others.tbl_employee_profile_request where REQUESTTYPE='"+Requesttype+"'  AND EMP_FLAG='P' AND EMPLOYEEID='"+Empid+"'";
			 List<Map<String, Object>>  rows = jdbcTemplate.queryForList(EMPINFO.toString());
			  for (@SuppressWarnings("rawtypes") Map row : rows) {
				  ADD=new net.sf.json.JSONObject();
				  //TYPE, DATE, Available, ENABLE
				  ADD.put("FIELD_NAME",row.get("FIELD_NAME"));
				  ADD.put("FIELD_VALUE",row.get("FIELD_VALUE"));
				  ADD.put("FIELD_DISPLAY",row.get("FIELD_DISPLAY"));
				  empid.add(ADD);
	        }
			}catch(Exception err){
				System.out.println("Exception at reverse" +err);
			}
		return empid;
	}
	
	public JSONArray HR_View(String Requesttype,String Employeeid)
	{
		JSONArray empid= new JSONArray();
		net.sf.json.JSONObject ADD=new net.sf.json.JSONObject();
		//Employeeid, Employeename, DOJ, BUNAME, Department, Designation
		try {
			//String EMPINFO="SELECT P.employeesequenceno 'Employeeid',p.callname 'Employeename', IFNULL(b.dateofjoin,'0000-00-00') 'DOJ',BU.NAME 'BUNAME',de.name 'Department',des.name 'Designation',IFNULL(a.EMPLOYEE_REQUEST_DATE,'0000-00-00 00:00:00') EMPLOYEE_REQUEST_DATE FROM hclhrm_prod_others.tbl_employee_profile_request a left join HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY P ON P.employeesequenceno=a.employeeid LEFT JOIN hcladm_prod.tbl_businessunit bu ON bu.businessunitid=p.companyid left join HCLHRM_PROD.TBL_EMPLOYEE_PROFILE b ON b.employeeid=p.employeeid LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFESSIONAL_DETAILS d ON p.EMPLOYEEID=d.EMPLOYEEID LEFT JOIN hcladm_prod.tbl_department de ON de.departmentid=d.departmentid LEFT JOIN hcladm_prod.tbl_designation des ON des.designationid=d.designationid  where a.REQUESTTYPE='"+Requesttype+"'  AND a.EMP_FLAG='P'  and p.status=1001 GROUP BY P.EMPLOYEEID,REQUESTTYPE";
			
			String EMPINFO="SELECT P.employeesequenceno 'Employeeid',ST.NAME 'STATUS',p.callname 'Employeename', IFNULL(b.dateofjoin,'0000-00-00') 'DOJ', BU.NAME 'BUNAME',de.name 'Department',des.name 'Designation', IFNULL(a.EMPLOYEE_REQUEST_DATE,'0000-00-00 00:00:00') EMPLOYEE_REQUEST_DATE FROM hclhrm_prod_others.tbl_employee_profile_request a left join HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY P ON P.employeesequenceno=a.employeeid LEFT JOIN HCLHRM_PROD.tbl_status_codes ST ON ST.STATUS=P.STATUS LEFT JOIN hcladm_prod.tbl_businessunit bu ON bu.businessunitid=p.companyid left join HCLHRM_PROD.TBL_EMPLOYEE_PROFILE b ON b.employeeid=p.employeeid LEFT JOIN HCLHRM_PROD.TBL_EMPLOYEE_PROFESSIONAL_DETAILS d ON p.EMPLOYEEID=d.EMPLOYEEID LEFT JOIN hcladm_prod.tbl_department de ON de.departmentid=d.departmentid LEFT JOIN hcladm_prod.tbl_designation des ON des.designationid=d.designationid where a.REQUESTTYPE='"+Requesttype+"'  AND a.EMP_FLAG='P' and p.status in(1001,1401,1092) and bu.callname IN (SElECT BUP.CALLNAME FROM hclhrm_prod.tbl_employee_primary p left join hcladm_prod.tbl_businessunit bup on p.companyid=bup.businessunitid where employeesequenceno='"+Employeeid+"') GROUP BY P.EMPLOYEEID,REQUESTTYPE ORDER BY EMPLOYEE_REQUEST_DATE ASC";
			System.out.println(EMPINFO);
			List<Map<String, Object>>  rows = jdbcTemplate.queryForList(EMPINFO.toString());
			  for (@SuppressWarnings("rawtypes") Map row : rows) {
				  ADD=new net.sf.json.JSONObject();
				  ADD.put("EMPLOYEEID",row.get("Employeeid"));
				  ADD.put("STATUS",row.get("STATUS"));
				  ADD.put("EMPLOYEENAME",row.get("Employeename"));
				  ADD.put("DOJ",row.get("DOJ"));
				  ADD.put("BUNAME",row.get("BUNAME"));
				  ADD.put("DEPARTMENT",row.get("Department"));
				  ADD.put("DESIGNATION",row.get("Designation"));
				  ADD.put("EMPLOYEE_REQUEST_DATE",row.get("EMPLOYEE_REQUEST_DATE"));
				  empid.add(ADD);
	        }
			}catch(Exception err){
				System.out.println("Exception at reverse" +err);
			}
		return empid;
	}
	
	
	public JSONArray Dashboard(String Requesttype,String Employeeid)
	{
		JSONArray empid= new JSONArray();
		net.sf.json.JSONObject ADD=new net.sf.json.JSONObject();
		//Employeeid, Employeename, DOJ, BUNAME, Department, Designation
		try {
			//String EMPINFO="SELECT REQUESTTYPE,COUNT(DISTINCT EMPLOYEEID) COUNT FROM hclhrm_prod_others.tbl_employee_profile_request where  EMP_FLAG='P' GROUP BY REQUESTTYPE";
			String EMPINFO="SELECT REQUESTTYPE,COUNT(DISTINCT a.EMPLOYEEID) COUNT FROM hclhrm_prod_others.tbl_employee_profile_request a left join hclhrm_prod.tbl_employee_primary b on a.employeeid=b.employeesequenceno left join hcladm_prod.tbl_businessunit bu on b.companyid=bu.businessunitid where EMP_FLAG='P' and b.status in(1001,1401,1092) and bu.callname IN ( SElECT BUP.CALLNAME FROM hclhrm_prod.tbl_employee_primary p left join hcladm_prod.tbl_businessunit bup on p.companyid=bup.businessunitid where employeesequenceno='"+Employeeid+"') GROUP BY REQUESTTYPE ";
			
			System.out.println(EMPINFO);
			List<Map<String, Object>>  rows = jdbcTemplate.queryForList(EMPINFO.toString());
			  for (@SuppressWarnings("rawtypes") Map row : rows) {
				  ADD=new net.sf.json.JSONObject();
				  ADD.put("REQUESTTYPE",row.get("REQUESTTYPE"));
				  ADD.put("COUNT",row.get("COUNT"));
				  empid.add(ADD);
				  System.out.println(empid);
	        }
			}catch(Exception err){
				System.out.println("Exception at reverse" +err);
			}
		return empid;
	}
	
	public JSONArray Panverify(String Requesttype)
	{
		JSONArray empid= new JSONArray();
		net.sf.json.JSONObject ADD=new net.sf.json.JSONObject();
		 
		try {
			String EMPINFO="SELECT COUNT(*) COUNT FROM hclhrm_prod.tbl_employee_personalinfo where PAN='"+Requesttype+"'";
			 List<Map<String, Object>>  rows = jdbcTemplate.queryForList(EMPINFO.toString());
			  for (@SuppressWarnings("rawtypes") Map row : rows) {
				  ADD=new net.sf.json.JSONObject();
				  ADD.put("COUNT",row.get("COUNT"));
				  empid.add(ADD);
	        }
			}catch(Exception err){
				System.out.println("Exception at reverse" +err);
			}
		return empid;
	}
	
	
	public JSONArray pan_first_verify(String EMPID)
	{
		JSONArray empid= new JSONArray();
		net.sf.json.JSONObject ADD=new net.sf.json.JSONObject();
		 
		try {
			String EMPINFO="SELECT EMPLOYEEID, SNO, PANTYPE, PAN, FLAG, RID, EXPECTEDDATE,IF(EXPECTEDDATE<=CURDATE(),'EXPIRED','PROCESS') PANCOUNT   FROM hclhrm_prod_others.tbl_employee_pan_verify where SNO=(SELECT MAX(SNO) FROM hclhrm_prod_others.tbl_employee_pan_verify where employeeid="+EMPID+" AND FLAG!='R' );";
			 List<Map<String, Object>>  rows = jdbcTemplate.queryForList(EMPINFO.toString());
			  for (@SuppressWarnings("rawtypes") Map row : rows) {
				  ADD=new net.sf.json.JSONObject();
				  
				  ///YES,NO,NEW
				  
				 // EMPLOYEEID, SNO, PANTYPE, PAN, FLAG, RID, 
				  ADD.put("EMPLOYEEID",row.get("EMPLOYEEID"));
				  ADD.put("SNO",row.get("SNO"));
				  ADD.put("PANTYPE",row.get("PANTYPE"));
				  ADD.put("PAN",row.get("PAN"));
				  ADD.put("FLAG",row.get("FLAG"));
				  ADD.put("RID",row.get("RID"));
				  ADD.put("EXPECTEDDATE",row.get("EXPECTEDDATE"));
				  ADD.put("PANCOUNT",row.get("PANCOUNT"));
				  
				  empid.add(ADD);
	        }
			}catch(Exception err){
				System.out.println("Exception at reverse" +err);
			}
		return empid;
	}
	
	
	public JSONArray EXIST_PAN(String EMPID)
	{
		JSONArray empid= new JSONArray();
		net.sf.json.JSONObject ADD=new net.sf.json.JSONObject();
		
		
		 
		try {
			String EMPINFO=" SELECT IF(PAN=''||PAN='NA'||PAN='0'||IFNULL(PAN,0)='0',0,PAN) PAN FROM hclhrm_prod.tbl_employee_personalinfo  where employeeid in(SELECT EMPLOYEEID FROM hclhrm_prod.tbl_employee_primary  where employeesequenceno in('"+EMPID+"')); ";
			 List<Map<String, Object>>  rows = jdbcTemplate.queryForList(EMPINFO.toString());
			  for (@SuppressWarnings("rawtypes") Map row : rows) {
				  ADD=new net.sf.json.JSONObject();
				 // EMPLOYEEID, SNO, PANTYPE, PAN, FLAG, RID, 
				  ADD.put("PAN",row.get("PAN"));
				  ADD.put("Valid",isValidPanCardNo(row.get("PAN").toString()));
				  empid.add(ADD);
	        }
			}catch(Exception err){
				System.out.println("Exception at reverse" +err);
			}
		return empid;
	}
	 public static boolean isValidPanCardNo(String panCardNo)
	    {
	        // Regex to check valid PAN Card number.
	        String regex = "[A-Z]{5}[0-9]{4}[A-Z]{1}";
	        // Compile the ReGex
	        Pattern p = Pattern.compile(regex);
	        // If the PAN Card number
	        // is empty return false
	        if (panCardNo == null)
	        {
	            return false;
	        }
	        // Pattern class contains matcher() method
	        // to find matching between given
	        // PAN Card number using regular expression.
	        Matcher m = p.matcher(panCardNo);
	        // Return if the PAN Card number
	        // matched the ReGex
	        return m.matches();
	    }
}
