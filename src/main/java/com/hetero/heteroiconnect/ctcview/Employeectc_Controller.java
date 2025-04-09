package com.hetero.heteroiconnect.ctcview;

import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.sf.json.JSONArray;


@RestController
@RequestMapping("/ctcinfo")
public class Employeectc_Controller {
	
	
	@Autowired
	Employeectc_Repository emp;
  
	//@PostMapping("view")
   	@PostMapping("ctcview")
   	public LinkedHashMap<String, Object> initiate(@RequestBody String login) throws Exception {
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
		org.json.JSONObject object = new org.json.JSONObject(login);
		
		JSONArray main = new JSONArray();
		
		  main=emp.ctcview(object.getString("empID"),object.getString("Password"),object.getString("HRMSEMPLOYEEID"));
		  response.put("ctcview",main);
			return response;
	}

}
