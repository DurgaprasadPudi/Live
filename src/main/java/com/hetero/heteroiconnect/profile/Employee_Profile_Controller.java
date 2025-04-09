package com.hetero.heteroiconnect.profile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hetero.heteroiconnect.applicationproperty.Propertyconfig;
import com.hetero.heteroiconnect.profileRepository.Employee_Profile_Repository;
import com.hetero.heteroiconnect.user.LoginUser;
import com.hetero.heteroiconnect.user.LoginUserRepository;

import net.sf.json.JSONArray;



@RestController
@RequestMapping("/profile")
public class Employee_Profile_Controller {
	
	     @Autowired
	    Employee_Profile_Repository empreposity;
	     
	     @Autowired
	 	LoginUserRepository mstUserRepository;
	     
		 @Autowired
		 private Propertyconfig Propertyconfig;
		 private String BANK_FOLDER = Propertyconfig.getBankDetailspath();
		 private String PAN_FOLDER = Propertyconfig.getPANDetailspath();
		 private String ADHAR_FOLDER = Propertyconfig.getAadharDetailspath();
		// private String PAN_FOLDER = Propertyconfig.getAadharDetailspath();
	    
	   @SuppressWarnings("unchecked")
	   @PostMapping("employee_request")
	  /*   @PostMapping(value = "/employee_request",consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
		 	        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})*/
		public LinkedHashMap<String, Object> Empinfo(@RequestBody String login) throws JSONException, JsonParseException, JsonMappingException, IOException, SQLException {
			LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
			//System.out.println(login);
			ObjectMapper objectMapper = new ObjectMapper();
			// Deserialization into a Map
			 // convert JSON string to Map
			 LinkedHashMap <String, String> map =objectMapper.readValue(login, LinkedHashMap.class);
		     //JSONArray Values = new JSONArray();
			 //Values=reposity.EmpInfo(object.getString("userid"));
			 
			int  a= empreposity.EmployeeRequest(map);
			response.put("employee_request", a);
			return response;
		}
	   
	        @PostMapping("Employee_Requests_Approve")
			public LinkedHashMap<String, Object> Employee_Request_Approve(@RequestBody String login) throws JSONException, JsonParseException, JsonMappingException, IOException, SQLException {
				LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
				//System.out.println(login);
				ObjectMapper objectMapper = new ObjectMapper();
				// Deserialization into a Map
				 // convert JSON string to Map
				 LinkedHashMap <String, String> map =objectMapper.readValue(login, LinkedHashMap.class);
			     //JSONArray Values = new JSONArray();
				 //Values=reposity.EmpInfo(object.getString("userid"));
				 
				int  a= empreposity.Employee_Request_Approve(map);
				response.put("employee_request", a);
				return response;
			}
	       
	        @PostMapping("Request_Status")
			public LinkedHashMap<String, Object> empshowleavetypes(@RequestBody String login) throws JSONException {
				LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
				org.json.JSONObject object = new org.json.JSONObject(login);
                 JSONArray Values = new JSONArray();
				 Values=empreposity.Request_Status(object.getString("empID"));
				 response.put("Request_Status", Values);
				return response;
			}
	        
	        
	        
	        @PostMapping("employee_view")
	    	public LinkedHashMap<String, Object> user(@RequestBody String login) throws JSONException {
	    		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
	    		JSONObject object = new JSONObject(login);
	    	 
	    		LoginUser user = null;
	    		    
	    		 user = mstUserRepository.findByUsername(
	    					object.getString("userName"));
	    		 JSONArray Values = new JSONArray();
	    		  
	    		 Values=empreposity.Request_Data(object.getString("userName"),object.getString("REQUESTTYPE"));
	    	 
	    		 
	    			  response.put("Previous_Fields",user);
	    			  response.put("Request_Fields",Values);
	    		 
	    		return response;
	    	}
	        
	        @PostMapping("hr_view")
	    	public LinkedHashMap<String, Object> hr_view(@RequestBody String login) throws JSONException {
	    		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
	    		JSONObject object = new JSONObject(login);
	    		JSONArray Values = new JSONArray();
	    		 Values=empreposity.HR_View(object.getString("REQUESTTYPE"),object.getString("empID"));
	    		 response.put("hr_view",Values);
	    		return response;
	    	}
	        
	        @PostMapping("dashboard")
	    	public LinkedHashMap<String, Object> Dashboard(@RequestBody String login) throws JSONException {
	    		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
	    		JSONObject object = new JSONObject(login);
	    		JSONArray Values = new JSONArray();
	    		 Values=empreposity.Dashboard(object.getString("REQUESTTYPE"),object.getString("empID"));
	    		 response.put("dashboard",Values);
	    		return response;
	    	}
	        
	        @PostMapping("panverify")
	    	public LinkedHashMap<String, Object> Panverify(@RequestBody String login) throws JSONException {
	    		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
	    		JSONObject object = new JSONObject(login);
	    		JSONArray Values = new JSONArray();
	    		 Values=empreposity.Panverify(object.getString("PAN"));
	    		 response.put("count",Values);
	    		  
	    		return response;
	    	}
	        
	        @PostMapping("pan_first_verify")
	    	public LinkedHashMap<String, Object> pan_first_verify(@RequestBody String login) throws JSONException {
	    		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
	    		JSONObject object = new JSONObject(login);
	    		JSONArray Values = new JSONArray();
	    		JSONArray existpan = new JSONArray();
	    		Values=empreposity.pan_first_verify(object.getString("empID"));
	    		existpan=empreposity.EXIST_PAN(object.getString("empID"));
	    		response.put("exist_pan",existpan);
	    		response.put("pan_first_verify",Values);
	    		return response;
	    	}
	          
	        @PostMapping("login_pan_employee_verification")
			public LinkedHashMap<String, Object> login_pan_employee_verification(@RequestBody String login) throws JSONException, JsonParseException, JsonMappingException, IOException, SQLException {
				LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
				LinkedHashMap<String,Object> result =new ObjectMapper().readValue(login, LinkedHashMap.class);
				 int  a=0;
				a= empreposity.first_PAN_Login_EmployeeRequest(result);
				response.put("login_pan_employee_verification", a);
				return response;
			}
	       
	        @PostMapping("remove_profilepic")
	    	public LinkedHashMap<String, Object> Profilepic_Remove(@RequestBody String login) throws JSONException {
	    		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
	    		JSONObject object = new JSONObject(login);
	    		 int i=empreposity.Profilepic_Remove(object.getString("empID"));
	    		 response.put("count",i);
	    		return response;
	    	}
	        
	        @PostMapping("login_pan_employee_upload")
		    public LinkedHashMap<String, Object>  LOGIN_PAN_uploadFileMulti(
		    		@RequestParam("FILE") MultipartFile uploadfiles,
		    		@RequestParam("EMPLOYEEID") String login,
		    		@RequestParam("REQUESTTYPE") String REQUESTTYPE,
		    		@RequestParam("PANTYPE") String PANTYPE,
		    		@RequestParam("EXPECTEDDATE") String EXPECTEDDATE,
		    		@RequestParam("PAN") String PAN) throws IOException, SQLException {
		         
	        	LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
	        	Random rand = new Random();
				int nRand = rand.nextInt(200000) + 12000;
	       
		    	 boolean flag=uploadFile(uploadfiles,login,nRand,REQUESTTYPE);
		    	 int user = 0;
		    	 if(flag)
		    	 {
		 			LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		 			map.put("EMPLOYEEID", login);
		 			map.put("REQUESTTYPE", REQUESTTYPE);
		 			map.put("PANTYPE", PANTYPE);
		 			map.put("EXPECTEDDATE", EXPECTEDDATE); 
		 			map.put("PAN", PAN);
		 			map.put("FILENAME", uploadfiles.getOriginalFilename());
		 			
		 			if(REQUESTTYPE.equalsIgnoreCase("PANADD"))
		 			{
		 			 	map.put("FILEPATH",PAN_FOLDER.split("//")[5]+"//"+login+"//"+nRand+"//"+uploadfiles.getOriginalFilename());
		 			}
		 			user= empreposity.first_PAN_Login_EmployeeRequest(map);
		 			response.put("employee_request", user);
		    }
		    	 
		    	 return response;
		    } 
	        
	        public boolean uploadFile(MultipartFile file,String empID,int nRand,String REQUESTTYPE) throws FileSystemException {
		    	 boolean flag=false;
		        try {
		        	
		         //Path path = Paths.get(new File(UPLOADED_FOLDER+"/"+empID).mkdir() + file.getOriginalFilename());
		         
		        // System.out.println(File.separator);
		        	String COMMONPATH="";
		        	
		        	if(REQUESTTYPE.equalsIgnoreCase("BANKADD"))
		 			{
		        		COMMONPATH=BANK_FOLDER;
		 			}
		 			if(REQUESTTYPE.equalsIgnoreCase("PANADD"))
		 			{
		 				COMMONPATH=PAN_FOLDER;
		 			}
		 			if(REQUESTTYPE.equalsIgnoreCase("AADHARADD"))
		 			{
		 				COMMONPATH=ADHAR_FOLDER;
		 			}
		 			
		 			if(REQUESTTYPE.equalsIgnoreCase("PANADD"))
		 			{
		 				COMMONPATH=PAN_FOLDER;
		 			}
		        	
		 			//System.out.println(COMMONPATH+"//"+empID+"//"+nRand);
		 			 
		 			File fileStructure = new File(COMMONPATH+"//"+empID+"//"+nRand);

		 			  // File object has a method called as exists() which
		 			  // Tests whether the file or directory denoted by 
		 			  // this abstract pathname exists. 
		 			  if(! fileStructure.exists()) {

		 			   // File object has a method called as mkdir() which
		 			   // Creates the directory named by this abstract pathname. 
		 			   if (fileStructure.mkdirs()) {

		 			    System.out.println("New Folder/Directory created .... ");
		 			   }
		 			   else {
		 			    System.out.println("Oops!!! Something blown up file creation...");
		 			   }
		 			  }
		 			
		        	//new File(COMMONPATH+"/"+nRand).mkdir();
		        	 
		           Path copyLocation = Paths
		                .get(COMMONPATH+"/"+empID+"/"+nRand+File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
		           
		           System.out.println(copyLocation);
		            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING); 
		            
		            flag=true;
		            
		        } catch (Exception e) {
		        	//flag=false;
		            e.printStackTrace();
		            throw new FileSystemException("Could not store file " + file.getOriginalFilename()
		                + ". Please try again!");
		        }
				return flag;
	    	
}
	        
	        
	        @PostMapping("fileupload")
		    public LinkedHashMap<String, Object>  uploadFileMulti(@RequestParam("FILE") MultipartFile uploadfiles,
		    		@RequestParam("EMPLOYEEID") String login,@RequestParam("REQUESTTYPE") String REQUESTTYPE,
		    		@RequestParam("BANKID") String BANKID,@RequestParam("BANKIFC") String BANKIFC,
		    		@RequestParam("BANKACC") String BANKACC,@RequestParam("BANKNAME") String BANKNAME,@RequestParam("PAN") String PAN) throws IOException, SQLException {
		        //  logger.debug("Multiple file upload!");
		        // Get file name
	        	LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
	        	Random rand = new Random();
				int nRand = rand.nextInt(200000) + 12000;
	        //	JSONObject object = new JSONObject(login);
		    	// System.out.println("i am in " +  uploadfiles.getOriginalFilename());
		    	 boolean flag=uploadFile(uploadfiles,login,nRand,REQUESTTYPE);
		    	 int user = 0;
		    	 if(flag)
		    	 {
		    		// System.out.println(uploadfiles.getOriginalFilename()+"GETNAME");
		    		// LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
		 			//System.out.println(login);
		 		//	ObjectMapper objectMapper = new ObjectMapper();
		 			// Deserialization into a Map
		 			 // convert JSON string to Map
		 			LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		 			
		 			map.put("EMPLOYEEID", login);
		 			map.put("REQUESTTYPE", REQUESTTYPE);
		 			
		 		//	if(BANKIFC)
		 			
		 			
		 			
		 			
		 			if(REQUESTTYPE.equalsIgnoreCase("PANADD"))
		 			{
		 				map.put("PAN", PAN);
		 			}
		 			else if(REQUESTTYPE.equalsIgnoreCase("BANKADD"))
		 			{
		 				if(!BANKIFC.equalsIgnoreCase("0"))
			 			{
			 				map.put("BANKIFC", BANKIFC);
			 			}
			 			
			 			if(!BANKID.equalsIgnoreCase("0"))
			 			{
			 				map.put("BANKID", BANKID);
			 				map.put("BANKNAME", BANKNAME);
			 			}
			 			
			 			if(!BANKACC.equalsIgnoreCase("0"))
			 			{
			 				map.put("BANKACC", BANKACC);
			 			}
		 			}
		 			
		 			else if(REQUESTTYPE.equalsIgnoreCase("ADHARADD"))
		 			{
		 				
		 			}
		 			
		 			 
		 			map.put("FILENAME", uploadfiles.getOriginalFilename());
		 	  
		 			if(REQUESTTYPE.equalsIgnoreCase("BANKADD"))
		 			{
		 				map.put("FILEPATH", BANK_FOLDER.split("//")[5]+"//"+login+"//"+nRand+"//"+uploadfiles.getOriginalFilename());
		 			}
		 			if(REQUESTTYPE.equalsIgnoreCase("PANADD"))
		 			{
		 			 	map.put("FILEPATH",PAN_FOLDER.split("//")[5]+"//"+login+"//"+nRand+"//"+uploadfiles.getOriginalFilename());
		 			}
		 			if(REQUESTTYPE.equalsIgnoreCase("AADHARADD"))
		 			{
		 			 	map.put("FILEPATH",ADHAR_FOLDER.split("//")[5]+"//"+login+"//"+nRand+"//"+uploadfiles.getOriginalFilename());
		 			}
		 			
//		 			if(REQUESTTYPE.equalsIgnoreCase("PANADD"))
//		 			{
//		 			 	map.put("FILEPATH",PAN_FOLDER.split("//")[3]+"//"+login+"//"+nRand+"//"+uploadfiles.getOriginalFilename());
//		 			}
		    		 
		 			user= empreposity.EmployeeRequest(map);
		 			response.put("employee_request", user);
		    }
		    	 
		    	 return response;
		    } 
	        
	        
}
