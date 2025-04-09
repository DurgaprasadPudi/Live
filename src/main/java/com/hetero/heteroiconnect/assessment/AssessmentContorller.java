package com.hetero.heteroiconnect.assessment;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.sf.json.JSONArray;

@RestController
@RequestMapping("/assesment")
public class AssessmentContorller {

	
	@Autowired
	AssessmentRepository assement;
  
   	@PostMapping("initiate")
   	public LinkedHashMap<String, Object> initiate(@RequestBody String login) throws Exception {
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
		org.json.JSONObject object = new org.json.JSONObject(login);
		
		JSONArray main = new JSONArray();
		main=assement.Initateassement();
		  response.put("intiate",main);

			return response;
	}
	
   	@PostMapping("assesementapprovalreport")
   	public LinkedHashMap<String, Object> AssessmentAprovalReport(@RequestBody String login) throws Exception {
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
		org.json.JSONObject object = new org.json.JSONObject(login);
		
		JSONArray main = new JSONArray();
		
		main=assement.AssessmentAprovalReport();
		
		  response.put("assesementapprovalreport",main);
			 
			return response;
	}
	
      
    
   	@PostMapping("permanentreport")
   	public LinkedHashMap<String, Object> permanentreport(@RequestBody String login) throws Exception {
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
		org.json.JSONObject object = new org.json.JSONObject(login);
		
		JSONArray main = new JSONArray();
		
		main=assement.PermanentReport();
		
		  response.put("permanentreport",main);
			 
			return response;
	}
   	
   	
   	@PostMapping("assesementextendreport")
   	public LinkedHashMap<String, Object> assesementextendreport(@RequestBody String login) throws Exception {
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
		org.json.JSONObject object = new org.json.JSONObject(login);
		
		JSONArray main = new JSONArray();
		
		main=assement.extendedReport();
		
		  response.put("assesementextendreport",main);
			 
			return response;
	}
   	
  	@PostMapping("hrprocess")
   	public LinkedHashMap<String, Object> hrprocess(@RequestBody String login) throws Exception {
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
		org.json.JSONObject object = new org.json.JSONObject(login);
		
		JSONArray main = new JSONArray();
		
		String userlist=object.getString("userid").replace("[", "").replace("]","");
		int j=0;
		
		 for(int i=0;i<userlist.split(",").length;i++){
   
			 String userid=userlist.split(",")[i];
			 j= assement.hrProcessingEmp(userid,object.getString("empID"));
				
			}
		 
		  response.put("hrprocess",j);
			 
			return response;
	}
  	
  	
  	@PostMapping("assessmentaccesslink")
   	public LinkedHashMap<String, Object> assessmentaccesslink(@RequestBody String login) throws Exception {
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
		org.json.JSONObject object = new org.json.JSONObject(login);
		
		//JSONArray main = new JSONArray();
		
		boolean flag=false;
		
		 String access_str="";
		 access_str= assement.checking_access(object.getString("empID"));
		 
			 
			 if(access_str.equalsIgnoreCase("0")){
				 
				 flag=false;
     			
     		 }
     		 else if(!access_str.equalsIgnoreCase("0")){
     			 
     			flag=true;
     		 }
		  response.put("access",flag);
			 
			return response;
	}
  	
  	
  	@PostMapping("assessmentformlist")
   	public LinkedHashMap<String, Object> assessmentform(@RequestBody String login) throws Exception {
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
		org.json.JSONObject object = new org.json.JSONObject(login);
		
		JSONArray main = new JSONArray();
		
		 main= assement.probationaryListToMngr(object.getString("empID"));
		  response.put("assessmentform",main);
			return response;
	}
  	
  	@PostMapping("assesmentfromview")
   	public LinkedHashMap<String, Object> assesmentfromview(@RequestBody String login) throws Exception {
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
		org.json.JSONObject object = new org.json.JSONObject(login);
		
		net.sf.json.JSONObject main = new net.sf.json.JSONObject();
		
		 main= assement.assesmentfromview(object.getString("userid"),object.getString("empID"),object.getString("mode"));
		  response.put("assesmentfromview",main);
			return response;
	}
  	
  	
 	@PostMapping("assementemployeeprofiledata")
   	public LinkedHashMap<String, Object> Assementemployeeprofiledata(@RequestBody String login) throws Exception {
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
		org.json.JSONObject object = new org.json.JSONObject(login);
		
		net.sf.json.JSONObject main = new net.sf.json.JSONObject();
		
		 main= assement.Assementemployeeprofiledata(object.getString("userid"));
		  response.put("assementemployeeprofiledata",main);
			return response;
	}
 	
 	@PostMapping("assessmentfeedback")
   	public LinkedHashMap<String, Object> assessmentfeedback(@RequestBody String login) throws Exception {
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
		org.json.JSONObject object = new org.json.JSONObject(login);
		
		//net.sf.json.JSONObject main = new net.sf.json.JSONObject();
		
		JSONArray main = new JSONArray();
		
		 main= assement.assessmentfeedback(object.getString("userid"),object.getString("empID"));
		  response.put("assessmentfeedback",main);
			return response;
	}
 	
 	
 	@PostMapping("nextApproval")
   	public LinkedHashMap<String, Object> nextApproval(@RequestBody String login) throws Exception {
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
		org.json.JSONObject object = new org.json.JSONObject(login);
		
		//net.sf.json.JSONObject main = new net.sf.json.JSONObject();
		
		JSONArray main = new JSONArray();
		 main= assement.nextApprovals(object.getString("empID"));
		  response.put("nextApproval",main);
			return response;
	}
	 
	
	 @PostMapping(value = "/assessmentsubmit",consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
	 	        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
		public LinkedHashMap<String, Object> Applyleave(@RequestBody Assesment asse) throws JSONException, IOException, SQLException {
			LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
			//Map hm=new HashMap();
			
			String messege=assement.assessmentsubmit(asse);
			  response.put("status",asse.toString()); 
				return response;
			}
  	
   	
}
