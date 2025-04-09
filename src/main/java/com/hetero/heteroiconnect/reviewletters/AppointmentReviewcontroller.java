package com.hetero.heteroiconnect.reviewletters;

import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.sf.json.JSONArray;

@RestController
@RequestMapping("/reviewletter")
public class AppointmentReviewcontroller {

	
	@Autowired
	AppointmentReviewrepository review;
  
   	@PostMapping("letter")
   	public LinkedHashMap<String, Object> initiate(@RequestBody String login) throws Exception {
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
		org.json.JSONObject object = new org.json.JSONObject(login);
		
		JSONArray main = new JSONArray();
		main=review.Reviewletter();
		  response.put("letters",main);

			return response;
	}
   	@PostMapping("Genrateletter")
   	public LinkedHashMap<String, Object> AssignRemove(@RequestBody String login) throws Exception {
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
		org.json.JSONObject object = new org.json.JSONObject(login);
		 
		  System.out.println(object.getString("empID")+"<---->"+object.getString("userid"));
		
		  int count = 0;
		 
		  count= review.GenRate(object.getString("empID"),object.getString("userid"),object.getString("Type"));
		  response.put("count",count);
		  return response;
	}
	@PostMapping("Appointmentlink")
   	public LinkedHashMap<String, Object> Appointmentlink(@RequestBody String login) throws Exception {

		LinkedHashMap<String, Object> response = new LinkedHashMap<>();
		 try {
	            org.json.JSONObject object = new org.json.JSONObject(login);
	            String empID = object.getString("empID");

	            // Call service method to check FILEPATH condition
	            String View = review.checkFilePath(empID);

	            response.put("VIEW", View);
	        } catch (Exception e) {
	            response.put("error", "Error processing request");
	            e.printStackTrace(); // Log exception for debugging
	        }

	        return response;
	    
}
  
	
}