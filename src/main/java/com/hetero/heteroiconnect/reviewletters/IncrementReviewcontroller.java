package com.hetero.heteroiconnect.reviewletters;

import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.sf.json.JSONArray;

 

@RestController
@RequestMapping("/increment")
public class IncrementReviewcontroller {

	
	@Autowired  
	 IncrementReviewrespository review;
   
//	@PostMapping("incrementlink")
//   	public LinkedHashMap<String, Object> Appointmentlink(@RequestBody String login) throws Exception {
//
//		LinkedHashMap<String, Object> response = new LinkedHashMap<>();
//		 try {
//	            org.json.JSONObject object = new org.json.JSONObject(login);
//	            String empID = object.getString("empID");
//
//	            // Call service method to check FILEPATH condition
//	            String View = review.checkletter(empID);
//
//	            response.put("VIEW", View);
//	        } catch (Exception e) {
//	            response.put("error", "Error processing request");
//	            e.printStackTrace(); // Log exception for debugging
//	        }
//
//	        return response;
//	    
//}    
	
	@PostMapping("letter")
   	public LinkedHashMap<String, Object> initiate(@RequestBody String login) throws Exception {
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
		org.json.JSONObject object = new org.json.JSONObject(login);
		
		JSONArray main = new JSONArray();
		main=review.fetchIncrementLetterData();
		  response.put("letters",main);

			return response;
	}
	
	
	@PostMapping("incrementlink")
   	public LinkedHashMap<String, Object> incrementlink(@RequestBody String login) throws Exception {

		LinkedHashMap<String, Object> response = new LinkedHashMap<>();
		 try {
	            org.json.JSONObject object = new org.json.JSONObject(login);
	            String empID = object.getString("empID");

	            // Call service method to check FILEPATH condition
	            String View = review.checkincrementrespository(empID);

	            response.put("VIEW", View);
	        } catch (Exception e) {
	            response.put("error", "Error processing request");
	            e.printStackTrace(); // Log exception for debugging
	        }

	        return response;
	    
}

	
}