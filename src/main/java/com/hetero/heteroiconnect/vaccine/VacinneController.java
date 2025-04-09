package com.hetero.heteroiconnect.vaccine;

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
@RequestMapping("/vaccine")
public class VacinneController {
 
	    @Autowired
	    VaccineRepositry reposity;
			@PostMapping("empvaccinedetails")
			public LinkedHashMap<String, Object> Empinfo(@RequestBody String login) throws JSONException {
				LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
				org.json.JSONObject object = new org.json.JSONObject(login);
				 JSONArray Values = new JSONArray();
				 Values=reposity.EmpInfo(object.getString("empID"));
				 response.put("empvaccinedetails", Values);
				return response;
			}
			
			 
					@PostMapping("vaccinetypes")
					public LinkedHashMap<String, Object> vaccinetypes(@RequestBody String login) throws JSONException {
						LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
						org.json.JSONObject object = new org.json.JSONObject(login);
						 JSONArray Values = new JSONArray();
						 Values=reposity.vaccinetypes(object.getString("empID"));
						 response.put("vaccinetypes", Values);
						return response;
					}
			
		  
			 @PostMapping(value = "/vaccine_entry",consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
	 	        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
		   	public LinkedHashMap<String, Object> vaccinerequest(@RequestBody Vaccine vc) throws Exception {
				LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
				int count = 0;
				count= reposity.vaccinerequest(vc);
				  response.put("count",count);
					return response;
			}
			
			@PostMapping(value = "/familyrequest",consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
		 	        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
			public LinkedHashMap<String, Object> familyrequest(@RequestBody Family fm) throws Exception {
				LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
				int count = 0;
				count= reposity.familyrequest(fm);
				  response.put("count",count);
					return response;
				 
				}
			
			 
			@PostMapping("Family")
			public LinkedHashMap<String, Object> Family(@RequestBody String login) throws JSONException {
				LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
				org.json.JSONObject object = new org.json.JSONObject(login);
				 
				 JSONArray Values = new JSONArray();
				   
				 Values=reposity.Family(object.getString("empID"));
				 
				 response.put("Familydetails", Values);
				return response;
			}
			
		 
}
