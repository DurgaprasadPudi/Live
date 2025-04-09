package com.hetero.heteroiconnect.location;


import java.util.LinkedHashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.sf.json.JSONArray;
  
@RestController
@RequestMapping("/location")
public class Locationcontroller {
	      
	@PersistenceContext
	EntityManager entityManager;
	 
	// Employee Leave Summary 
	@PostMapping("states")
	public LinkedHashMap<String, Object> states(@RequestBody String login) throws JSONException {
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
		JSONObject object = new JSONObject(login);
		    
	           String Sql="SELECT LOCATIONID,NAME FROM hcllcm_prod.tbl_location where PARENT=1 and status=1001 ORDER BY NAME ASC";
				
				JSONArray states = new JSONArray();
				 
		          List<Object[]> Experience_Obj = entityManager.createNativeQuery(Sql).getResultList();
		         for (Object temp[] : Experience_Obj) {
		        	
		        	  net.sf.json.JSONObject addobj=new net.sf.json.JSONObject();
		        	 
		        	 addobj.put("LOCATIONID", temp[0].toString());
		        	 addobj.put("NAME", temp[1].toString());
		        	  
		        	 states.add(addobj);
		         }   
		        
			         response.put("states",states); 
		      
		return response;
	}
	
	
	@PostMapping("cities")
	public LinkedHashMap<String, Object> cities(@RequestBody String login) throws JSONException {
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
		JSONObject object = new JSONObject(login);
		    
	           String Sql="SELECT LOCATIONID,NAME FROM hcllcm_prod.tbl_location where PARENT='"+object.getString("location")+"' and status=1001";
				
				JSONArray cities = new JSONArray();
				 
		          List<Object[]> Experience_Obj = entityManager.createNativeQuery(Sql).getResultList();
		         for (Object temp[] : Experience_Obj) {
		        	
		        	  net.sf.json.JSONObject addobj=new net.sf.json.JSONObject();
		        	 
		        	 addobj.put("LOCATIONID", temp[0].toString());
		        	 addobj.put("NAME", temp[1].toString());
		        	  
		        	 cities.add(addobj);
		         } 
		        
			         response.put("cities",cities); 
		      
		return response;
	}
	
	
	@PostMapping("BankDetails")
	public LinkedHashMap<String, Object> BankDetails(@RequestBody String login) throws JSONException {
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
		JSONObject object = new JSONObject(login);
		    
	           String Sql="SELECT BANKID,BANKNAME FROM hcladm_prod.tbl_bank_details where status=1001";
				JSONArray cities = new JSONArray();
		          List<Object[]> Experience_Obj = entityManager.createNativeQuery(Sql).getResultList();
		         for (Object temp[] : Experience_Obj) {
		        	
		        	  net.sf.json.JSONObject addobj=new net.sf.json.JSONObject();
		        	 
		        	 addobj.put("BANKID", temp[0].toString());
		        	 addobj.put("BANKNAME", temp[1].toString());
		        	  
		        	 cities.add(addobj);
		         } 
			         response.put("BankDetails",cities); 
		      
		return response;
	}

	
	@PostMapping("Relations")
	public LinkedHashMap<String, Object> Relations(@RequestBody String login) throws JSONException {
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
		JSONObject object = new JSONObject(login);
	           String Sql="SELECT RELATIONID, RELATIONNAME FROM hcladm_prod.tbl_relations";
				JSONArray cities = new JSONArray();
		          List<Object[]> Experience_Obj = entityManager.createNativeQuery(Sql).getResultList();
		         for (Object temp[] : Experience_Obj) {
		        	  net.sf.json.JSONObject addobj=new net.sf.json.JSONObject();
		        	 addobj.put("RELATIONID", temp[0].toString());
		        	 addobj.put("RELATIONNAME", temp[1].toString());
		        	  
		        	 cities.add(addobj);
		         } 
		        
			         response.put("Relations",cities); 
		      
		return response;
	}

	
	
	
	//SELECT LOCATIONID,NAME FROM hcllcm_prod.tbl_location where PARENT='"+Param+"' and status=1001
	 
	 
}
