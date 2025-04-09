package com.hetero.heteroiconnect.otherservice;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import net.sf.json.JSONArray;

@RestController
@RequestMapping("/iconnect")
public class EmplistController {
	
	 @Autowired
	 OtherDao OtherDao;
	 
    @PostMapping("BU_emplist")
	public LinkedHashMap<String, Object> Employee_Request_Approve(@RequestBody String login) throws JSONException, JsonParseException, JsonMappingException, IOException, SQLException {
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
		org.json.JSONObject object = new org.json.JSONObject(login);
        JSONArray Values = new JSONArray();
		 Values= OtherDao.getlist(object.getString("BUID"));
		response.put("BuLIST", Values);
		return response;
	}

}
