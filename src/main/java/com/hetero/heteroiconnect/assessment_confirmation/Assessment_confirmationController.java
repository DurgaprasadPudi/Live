package com.hetero.heteroiconnect.assessment_confirmation;

import java.sql.SQLException;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.sf.json.JSONArray;

@RestController()
@RequestMapping("/Assess_confirmation")
public class Assessment_confirmationController {
	@Autowired
	Assessment_confirmationRepository assess_confirm;
 
	@PostMapping("assess_confirmation_dropdownlist")
	public LinkedHashMap<String, Object> alldropdownlist(@RequestBody String empid) throws SQLException{
		System.out.println(" ENtered in");
		LinkedHashMap<String, Object> lhm=new LinkedHashMap<String, Object>();
		JSONArray design_arr = new JSONArray();
		JSONArray depart_arr = new JSONArray();
		JSONArray EmployeementTypes_arr = new JSONArray();

		design_arr= assess_confirm.designations();
		depart_arr=assess_confirm.departments();
		EmployeementTypes_arr=assess_confirm.employementtypes();

		System.out.println("design_arr :: "+design_arr);
		lhm.put("Designations", design_arr);
		lhm.put("Departmants", depart_arr);
		lhm.put("EmployeementTypes", EmployeementTypes_arr);

		return lhm;

	}


	@PostMapping("confirmation")
	public ResponseEntity<Integer>  confirmation(@RequestBody Assessment_confirmationPojo empid) throws Exception{
		System.out.println(" ENtered in confirmationinsert" +empid);
		
	int str=	assess_confirm.confirmationinserting(empid);
		 return new ResponseEntity<>(str, HttpStatus.OK);
		  

	}




}
