package com.hetero.heteroiconnect.flexi;
 
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.util.IOUtils;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hetero.heteroiconnect.user.LoginUserRepository;

import net.sf.json.JSONArray;

@RestController
@RequestMapping("/employeeflexi")
public class EmployeeflexiController {
  
	    @Autowired
	    Flexirepository reposity;
	    
		@Autowired
		JdbcTemplate jdbcTemplate;
		@Autowired
		LoginUserRepository mstUserRepository;
			@PostMapping("employeebusinessunit")
			public LinkedHashMap<String, Object> employeebusinessunit(@RequestBody String login) throws JSONException {
				LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
				org.json.JSONObject object = new org.json.JSONObject(login);
				 
                 JSONArray Values = new JSONArray();
				  
				 Values=reposity.employeebusinessunit(object.getString("empID"));
				 
				 response.put("employeebusinessunit", Values);
				return response;
			}
			
			@PostMapping("flexilist")
			public LinkedHashMap<String, Object> flexilist(@RequestBody String login) throws JSONException {
				LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
				org.json.JSONObject object = new org.json.JSONObject(login);
				 
                 JSONArray Values = new JSONArray();
				 Values=reposity.flexilist(object.getString("userid"),object.getString("businessunitid"),object.getString("reporttype"));
				 response.put("employeebusinessunit", Values);
				return response;
			}
			
			
			@PostMapping("AssignRemove")
		   	public LinkedHashMap<String, Object> AssignRemove(@RequestBody String login) throws Exception {
				LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
				org.json.JSONObject object = new org.json.JSONObject(login);
				 
				  System.out.println(object);
				
				  int count = 0;
				 
				  count= reposity.Assign_Remove(object.getString("buttontype"),object.getString("empID"),object.getString("userid"));
				  response.put("count",count);
				  return response;
			}
			
			
			@PostMapping("saturdayflexilist")
			public LinkedHashMap<String, Object> Sat_Policy(@RequestBody String login) throws JSONException {
				LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
				org.json.JSONObject object = new org.json.JSONObject(login);
				 
                 JSONArray Values = new JSONArray();
				 Values=reposity.Sat_Policy_list(object.getString("userid"),object.getString("businessunitid"),object.getString("reporttype"));
				 response.put("employeebusinessunit", Values);
				return response;
			}
			
			
			
			@PostMapping("saturdaypolicycheck")
			public LinkedHashMap<String, Object> saturdaypolicycheck(@RequestBody String login) throws JSONException {
				LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
				org.json.JSONObject object = new org.json.JSONObject(login);
				 
               //  JSONArray Values = new JSONArray();
					 
				   //String Data="";
				 
				   List<String> data= jdbcTemplate.queryForList("SELECT COUNT(*) COUN FROM test.tbl_emp_saturday_policy where employee_seq='"+object.getString("userid")+"'",String.class);
				 
				 
				 response.put("saturdaypolicycheck", data); 
				
				 return response;
			}
			
			
			
			
			
			//SELECT COUNT(*) COUN FROM test.tbl_emp_saturday_policy where employee_SEQ='10515'
			
			
			
			
			
			
			@PostMapping("saturdayAssign")
		   	public LinkedHashMap<String, Object> Assign_Sat(@RequestBody String login) throws Exception {
				LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
				org.json.JSONObject object = new org.json.JSONObject(login);
				 
				  System.out.println(object);
				
				  int count = 0;
				 
				  count= reposity.Assign_Sat_Policy(object.getString("buttontype"),object.getString("empID"),object.getString("userid"));
				  response.put("count",count);
				  return response;
			}

		 	@PostMapping("payslips")
			public LinkedHashMap<String, Object> Payslips(@RequestBody String login) throws JSONException {
				LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
				org.json.JSONObject object = new org.json.JSONObject(login);
				System.out.println(object);
				JSONArray Values = new JSONArray();
				Values=reposity.getpayslips((object.getString("empID")));
				response.put("payslips", Values);
				return response;
			}
		 	
		 	@PostMapping("ITS")
			public LinkedHashMap<String, Object> ITS(@RequestBody String login) throws JSONException {
				LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
				org.json.JSONObject object = new org.json.JSONObject(login);
				 
				System.out.println(object);
				
				JSONArray Values = new JSONArray();
				Values=reposity.getITS((object.getString("empID")));
				response.put("ITS", Values);
				return response;
			}
		 	
		 	@RequestMapping(value = "/get/pdf/{emp}/{pwd}/{payperiod}/{bu}", method = RequestMethod.GET)
		     public Object getPdf(@PathVariable("emp") String emp,@PathVariable("pwd") String pwd,@PathVariable("payperiod") String payperiod,@PathVariable("bu") String bu) {
		           //logger.info("Service called: getPdf");
		 		
		 		  File file = null;
		           
		 		  boolean flag=false; 
		 		  
		 		  String Parentpath="null";
		 		
		 		try {
					String EMPINFO="SELECT COUNT(*) COUNT FROM  hclhrm_prod.tbl_employee_login where EMPLOYEECODE='"+emp+"' and CONCAT(MD5('"+emp+"'),PASSWORD)='"+pwd+"';" ;
					
					System.out.println(EMPINFO);
					List<Map<String, Object>>  rows = jdbcTemplate.queryForList(EMPINFO.toString());
					// STATUS, DISPLAYNAME, HRMSEMPLOYEEID, EmpCode, Fullname, DIVISION, DEPT, DESIGNATION, STATUS, EmploymentType
					  for (@SuppressWarnings("rawtypes") Map row : rows) {
						  
						  if(row.get("COUNT").toString().equalsIgnoreCase("1"))
						  {
							  flag=true;
						  }
			        }
					}catch(Exception err){
						System.out.println("Exception at reverse" +err);
					}
		           try {
		        	   
		        	   if(flag)
		        	   {
		        		  
		        	   file = new File("C:\\Program Files\\Apache Software Foundation\\Tomcat 9.0\\webapps\\"+Parentpath+"\\BKP\\"+payperiod+"\\"+bu+"\\"+emp+".pdf");
						  if (file.exists())
					        {
							  file = new File("C:\\Program Files\\Apache Software Foundation\\Tomcat 9.0\\webapps\\"+Parentpath+"\\BKP\\"+payperiod+"\\"+bu+"\\"+emp+".pdf");
								
					        }
					         else
					           {
					        	 file = new File("C:\\Program Files\\Apache Software Foundation\\Tomcat 9.0\\webapps\\"+Parentpath+"\\FileNotFound.pdf");
					           }
		        	     }
		        	   else {
		        		   
		        		   file = new File("C:\\Program Files\\Apache Software Foundation\\Tomcat 9.0\\webapps\\"+Parentpath+"\\FileNotFound.pdf");
		        	   }
		        	   
		        	   
		        	     FileInputStream fileInputStream = new FileInputStream(file);
	                     return IOUtils.toByteArray(fileInputStream);
		        	   
		           } catch (IOException e) {
		                e.printStackTrace();
		           }
		           //logger.info("Service completed: getPdf");
		           return null;
		     }
		 	
		 	
		 	
 	 	@RequestMapping(value = "/get/ITpdf/{emp}/{pwd}/{type}", method = RequestMethod.GET)
 	     public Object getITPdf(@PathVariable("emp") String emp,@PathVariable("pwd") String pwd,@PathVariable("type") String type) {
 		           //logger.info("Service called: getPdf");
 		 		
	 		  File file = null;
	           
		 		  boolean flag=false;  
		 		  
		 		  //String Parentpath="BSTPAYSLIP";
		 		
		 		try {
		 			String EMPINFO="SELECT COUNT(*) COUNT FROM  hclhrm_prod.tbl_employee_login where EMPLOYEECODE='"+emp+"' and  EMPLOYEECODE!=30546 and CONCAT(MD5('"+emp+"'),PASSWORD)='"+pwd+"';" ;
					 List<Map<String, Object>>  rows = jdbcTemplate.queryForList(EMPINFO.toString());
					// STATUS, DISPLAYNAME, HRMSEMPLOYEEID, EmpCode, Fullname, DIVISION, DEPT, DESIGNATION, STATUS, EmploymentType
					  for (@SuppressWarnings("rawtypes") Map row : rows) {
					  
						  if(row.get("COUNT").toString().equalsIgnoreCase("1"))
						  {
							  flag=true;
						  }
			        }
					}catch(Exception err){
						System.out.println("Exception at reverse" +err);
					}
		           try {
		        	   
		        	   if(flag)
		        	   {
						
							  if(type.equalsIgnoreCase("form16a"))
							  {
								  
								  String PAN=mstUserRepository.PanNumber(emp);
								  
								  System.out.println(PAN);
								  
								  if(!PAN.equalsIgnoreCase("0"))
								  {
									 // flag=true;
									  file = new File("C:\\Program Files\\Apache Software Foundation\\Tomcat 9.0\\webapps\\ForCastbkp\\16AANDB2025\\"+PAN+".pdf");
								  }
								  if (file.exists()&&!PAN.equalsIgnoreCase("0"))
							        {
									  file = new File("C:\\Program Files\\Apache Software Foundation\\Tomcat 9.0\\webapps\\ForCastbkp\\16AANDB2025\\"+PAN+".pdf");
							        }
							        else
							         {
							        	file = new File("C:\\Program Files\\Apache Software Foundation\\Tomcat 9.0\\webapps\\ForCastbkp\\FileNotFound.pdf");
							         } 
							  }
							  
							  else if(type.equalsIgnoreCase("forecast"))
							  {
								  file=new File("C:\\Program Files\\Apache Software Foundation\\Tomcat 9.0\\webapps\\ForCastbkp\\"+emp+".pdf");
								  if (file.exists())
							        {
									  file = new File("C:\\Program Files\\Apache Software Foundation\\Tomcat 9.0\\webapps\\ForCastbkp\\"+emp+".pdf");
							        }
								   else
								   {
									   
									   file = new File("C:\\Program Files\\Apache Software Foundation\\Tomcat 9.0\\webapps\\ForCastbkp\\FileNotFound.pdf");

									     /// ADD.put("FY","https://sso.heterohcl.com/null/FileNotFound.pdf");
								   }
								  
							  }
							  
	        	     }
	        	   else {
	        		   
	        		   file = new File("C:\\Program Files\\Apache Software Foundation\\Tomcat 9.0\\webapps\\ForCast\\FileNotFound.pdf");
     	        	   }
	        	   
		        	   
		        	  FileInputStream fileInputStream = new FileInputStream(file);
                     return IOUtils.toByteArray(fileInputStream);
	        	   
	           } catch (IOException e) {
	                e.printStackTrace();
	           }
//		           //logger.info("Service completed: getPdf");
	           return null;
	     }
 	 	
 	// Endpoint to fetch letter PDF based on employee code
 	    @RequestMapping(value = "/get/letter/{emp}", method = RequestMethod.GET)
 	    public Object getletterPdf(@PathVariable("emp") String emp) {
 	        try {
 	            String query = "SELECT COUNT(*) AS COUNT, A.FILEPATH " +
 	                           "FROM hclhrm_prod.tbl_employee_login L " +
 	                           "LEFT JOIN test.appointmentletter_temp A ON L.EMPLOYEECODE = A.employeeid " +
 	                           "WHERE L.EMPLOYEECODE = " + emp;

 	            List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);

 	            if (rows != null && !rows.isEmpty()) {
 	                Map<String, Object> row = rows.get(0);
 	                String count = row.get("COUNT").toString();
 	                String filePath = (String) row.get("FILEPATH");

 	                if ("1".equals(count) && filePath != null) {
 	                    File file = new File(filePath);
 	                    if (file.exists()) {
 	                        FileInputStream fileInputStream = new FileInputStream(file);
 	                        return IOUtils.toByteArray(fileInputStream);
 	                    }
 	                }
 	            }
 	        } catch (IOException e) {
 	            e.printStackTrace();
 	        }
 	        return null;
 	    }
    
 	   @RequestMapping(value = "/get/incrementletter/{emp}", method = RequestMethod.GET)
	    public Object gethikeletterPdf(@PathVariable("emp") String emp) {
	        try {
	            String query = "SELECT COUNT(*) AS COUNT,FILE_PATH FROM test.tbl_increment_letter where employeeid="+emp;

	            List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);

	            if (rows != null && !rows.isEmpty()) {
	                Map<String, Object> row = rows.get(0);
	                String count = row.get("COUNT").toString();
	                String filePath = (String) row.get("FILE_PATH");

	                if ("1".equals(count) && filePath != null) {
	                    File file = new File(filePath);
	                    if (file.exists()) {
	                        FileInputStream fileInputStream = new FileInputStream(file);
	                        return IOUtils.toByteArray(fileInputStream);
	                    }
	                }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }  
 	   
 	   
 	  @RequestMapping(value = "/get/promotionletter/{emp}", method = RequestMethod.GET)
	    public Object getpromotionletterPdf(@PathVariable("emp") String emp) {
	        try {
	            String query = "SELECT COUNT(*) AS COUNT,FILEPATH FROM test.tbl_promotion_letter where employeeid="+emp;

	            List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);

	            if (rows != null && !rows.isEmpty()) {
	                Map<String, Object> row = rows.get(0);
	                String count = row.get("COUNT").toString();
	                String filePath = (String) row.get("FILEPATH");

	                if ("1".equals(count) && filePath != null) {
	                    File file = new File(filePath);
	                    if (file.exists()) {
	                        FileInputStream fileInputStream = new FileInputStream(file);
	                        return IOUtils.toByteArray(fileInputStream);
	                    }
	                }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return null;
	    } 
 	  
 	  
 	  
 	 @RequestMapping(value = "/get/confirmationletter/{emp}", method = RequestMethod.GET)
	    public Object getconfirmationletterPdf(@PathVariable("emp") String emp) {
 		 System.out.println("HI");
	        try {
	            //String query = "SELECT COUNT(*) AS COUNT,FILEPATH FROM test.tbl_promotion_letter where employeeid="+emp;
	        	//String emp = "yourEmployeeSequenceNumber"; // Replace with actual value or variable
	        	String query = "SELECT COUNT(*) AS COUNT, C.FILE_PATH " +
	        	               "FROM TEST.TBL_EMPLOYEE_CONFIRMATION_DETAILS C " +
	        	               "LEFT JOIN hclhrm_prod.tbl_employee_primary P ON P.EMPLOYEEID = C.EMPLOYEEID " +
	        	               "LEFT JOIN hcladm_prod.tbl_businessunit bu ON bu.businessunitid = p.companyid " +
	        	               "WHERE p.EMPLOYEESEQUENCENO ="+emp+"";

	        	  
	            List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);

	            if (rows != null && !rows.isEmpty()) {
	                Map<String, Object> row = rows.get(0);
	                String count = row.get("COUNT").toString();
	                String filePath = (String) row.get("FILE_PATH");

	                if ("1".equals(count) && filePath != null) {
	                    File file = new File(filePath);
	                    if (file.exists()) {
	                        FileInputStream fileInputStream = new FileInputStream(file);
	                        return IOUtils.toByteArray(fileInputStream);
	                    }
	                }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return null;
	    } 
 	 
 	 
// 	 @PostMapping("/Form16list/{userid}")
//     public LinkedHashMap<String, Object> form16(@PathVariable String userid) {
//         LinkedHashMap<String, Object> response = new LinkedHashMap<>();
//         List<Form16Info> values = reposity.form16(userid);
//         response.put("Form16list", values);
//         return response;
//     }

 	@GetMapping("/Form16list/{userid}")
 	public LinkedHashMap<String, Object> form16(@PathVariable String userid) {
 	    LinkedHashMap<String, Object> response = new LinkedHashMap<>();
 	    List<Form16Info> values = reposity.form16(userid);
 	    response.put("Form16list", values);
 	    return response;
 	}
 	
 	
 	
 	@RequestMapping(value = "/FY/form16/{year}/{PAN}", method = RequestMethod.GET)
 	public Object getForm16(@PathVariable("year") String year,
 	                       @PathVariable("PAN") String pan) {
 	    try {
 	        File file = new File("C:/Program Files/Apache Software Foundation/Tomcat 9.0/webapps/ForCast/FileNotFound.pdf");

 	        // Check for valid PAN
 	        if (pan != null && !pan.equalsIgnoreCase("0") && !pan.equalsIgnoreCase("NA")) {
 	            File actualFile = new File("C:/Program Files/Apache Software Foundation/Tomcat 9.0/webapps/ForCastbkp/16AANDB" + year + "/" + pan + ".pdf");

 	            if (actualFile.exists()) {
 	                file = actualFile;
 	            }
 	        }

 	        try (FileInputStream fis = new FileInputStream(file)) {
 	            return IOUtils.toByteArray(fis);  // Still a valid Object (byte[])
 	        }

 	    } catch (IOException e) {
 	        e.printStackTrace();
 	        return "Error retrieving PDF file.";  // Optional: meaningful message instead of null
 	    }
 	}


			 
	 
}

