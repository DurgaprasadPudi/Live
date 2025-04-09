package com.hetero.heteroiconnect.familydetailsreports;
import java.util.LinkedHashMap;

import javax.persistence.EntityManager;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.sf.json.JSONArray;

@RestController
@RequestMapping("/sindhuhospital")
public class familydetailsreportcontroller {
  
	    @Autowired
	    familydetailsreportRepository reposity;
	    
	    @Autowired 
	    private EntityManager entityManager;
	    
		@Autowired
		JdbcTemplate jdbcTemplate;
		 
		
		  
			@PostMapping("GetFamilyDetails")
			public LinkedHashMap<String, Object> GetFamilyDetails(@RequestBody String login) throws JSONException {
				LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
				org.json.JSONObject object = new org.json.JSONObject(login);
				 
                 JSONArray Values = new JSONArray();
				  
				 Values=reposity.GetFamilyDetails();
				 
				 response.put("GetFamilyDetails", Values);
				return response;
			}
			
			@PostMapping("GetEmployeeDetails")
			public LinkedHashMap<String, Object> GetEmployeeDetails(@RequestBody String login) throws JSONException {
				LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
				org.json.JSONObject object = new org.json.JSONObject(login);
				 
                 JSONArray Values = new JSONArray();
				  
				 Values=reposity.GetEmployeeDetails();
				 
				 response.put("GetEmployeeDetails", Values);
				return response;
			}
			
			
}
