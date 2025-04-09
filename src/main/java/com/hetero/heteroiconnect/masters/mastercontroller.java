package com.hetero.heteroiconnect.masters;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hetero.heteroiconnect.masters.entity.Department;
import com.hetero.heteroiconnect.masters.entity.Designation;

 

@RestController
@RequestMapping("/master")
public class mastercontroller {
	@Autowired
	private masterservice deptAndDesigAndUniversityService;

//fetching
	
	@PostMapping(value = "businessunit", produces = "application/json")
	public List<Map<String, Object>> getDistinctBusinessUnits(@RequestParam("empCode") int empCode) {
		return deptAndDesigAndUniversityService.getDistinctBusinessUnits(empCode);
	}
	@PostMapping(value = "departments", produces = "application/json")
	public ResponseEntity<Object> fetchdepts() {
		return ResponseEntity.ok(deptAndDesigAndUniversityService.fetchDepartments());
	}
	
	@PostMapping(value = "designations", produces = "application/json")
	public ResponseEntity<Object> fetchdesigns() {
		return ResponseEntity.ok(deptAndDesigAndUniversityService.fetchDesignations());
	}

	@PostMapping(value = "universitys", produces = "application/json")
	public ResponseEntity<Object> fetchuniversitys() {
		return ResponseEntity.ok(deptAndDesigAndUniversityService.fetchuniversitys());
	}

	// insert
	@PostMapping(value = "insertUniversity", consumes = "multipart/form-data", produces = "application/json")
	public ResponseEntity<Object> insertUniversity(@RequestParam("name") String name,@RequestParam("createdBy") int createdBy,@RequestParam("type") String type) {
		return ResponseEntity.ok(deptAndDesigAndUniversityService.insertUniversity(name,createdBy,type));
	}

	@PostMapping(value = "insertdepartment", consumes = "multipart/form-data", produces = "application/json")
	public ResponseEntity<Object> insertDepartment(@RequestParam("name") String name, @RequestParam("code") String code,
			@RequestParam("status") int status,@RequestParam("createdby") int createdby,@RequestParam("type") String type) {
		return ResponseEntity.ok(deptAndDesigAndUniversityService.insertDepartment(name, code, status,createdby,type));
	}
	@PostMapping(value = "insertassigndepartment", consumes = "application/json", produces = "application/json")
	    public ResponseEntity<Object> assignDepartmentinsert(@RequestBody List<Department> departments) {
	        return ResponseEntity.ok(deptAndDesigAndUniversityService.assignDepartmentinsert(departments));
	}
	@PostMapping(value = "insertassigndesignation", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> assignDesignationinsert(@RequestBody List<Designation> designations) {
        return ResponseEntity.ok(deptAndDesigAndUniversityService.assignDesignationinsert(designations));
}

	@PostMapping(value = "insertdesignation", consumes = "multipart/form-data", produces = "application/json")
	public ResponseEntity<Object> insertDesignation(@RequestParam("name") String name,
			@RequestParam("code") String code, @RequestParam("status") int status,@RequestParam("createdby") int createdby,@RequestParam("type") String type) {
		return ResponseEntity.ok(deptAndDesigAndUniversityService.insertDesignation(name, code, status,createdby,type));
	}

	@PostMapping(value = "updateUniversity/{universityid}/{name}/{createdby}/{type}", produces = "application/json")
	public ResponseEntity<Object> updateUniversity(
	        @PathVariable("universityid") int universityid,
	        @PathVariable("createdby") int createdby, 
	        @PathVariable("type") String type, 
	        @PathVariable("name") String name
	) {
	    return ResponseEntity.ok(deptAndDesigAndUniversityService.updateUniversity(universityid, name, createdby, type));
	}

	@PostMapping(value = "updateDesignation/{designationid}/{name}/{code}/{status}/{modifiedby}/{type}", produces = "application/json")
	public ResponseEntity<Object> updateDesignation(@PathVariable("designationid") int designationid,
			@PathVariable("name") String name, @PathVariable("code") String code, @PathVariable("status") int status,@PathVariable("modifiedby") int modifiedby,@PathVariable("type") String type) {
		return ResponseEntity.ok(deptAndDesigAndUniversityService.updateDesignation(designationid, name, code, status,modifiedby,type));
	}
	@PostMapping(value = "updateDepartment/{departmentid}/{name}/{code}/{status}/{modifiedby}/{type}", produces = "application/json")
	public ResponseEntity<Object> updateDepartment(@PathVariable("departmentid") String departmentid,
	                                               @PathVariable("name") String name,
	                                               @PathVariable("code") String code,
	                                               @PathVariable("status") int status,
	                                               @PathVariable("modifiedby") int modifiedby,@PathVariable("type") String type) {
	    return ResponseEntity.ok(deptAndDesigAndUniversityService.updateDepartment(departmentid, name, code, status, modifiedby,type));
	}

	@PostMapping(value="assignDepartment",consumes = "multipart/form-data", produces = "application/json")
	public ResponseEntity<Object> assignDepartment(@RequestParam("Businessunitid") int businessunitid) {
	    return ResponseEntity.ok(deptAndDesigAndUniversityService.assignDepartment(businessunitid));
	}
	@PostMapping(value="assignDesignation",consumes = "multipart/form-data", produces = "application/json")
	public ResponseEntity<Object> assignDesignation(@RequestParam("Businessunitid") int businessunitid) {
	    return ResponseEntity.ok(deptAndDesigAndUniversityService.assignDesignation(businessunitid));
	}

	//qualification
		@PostMapping(value="/educationlevel", produces = "application/json")
		public ResponseEntity<Object> educationLevel() {
		    return ResponseEntity.ok(deptAndDesigAndUniversityService.educationLevel());
		}
		@PostMapping(value="/educationqualifiaction",consumes = "multipart/form-data",produces = "application/json")
		public ResponseEntity<Object> educationqulification(@RequestParam("educationlevelid") int educationlevelid) {
		    return ResponseEntity.ok(deptAndDesigAndUniversityService.educationQualification(educationlevelid));
		}

		@PostMapping(value="/branch",consumes = "multipart/form-data",produces = "application/json")
		public ResponseEntity<Object> qualificationBranch(@RequestParam("qualificationid") String qualificationid) {
		    return ResponseEntity.ok(deptAndDesigAndUniversityService.qualificationBranch(qualificationid));
		}
		@PostMapping(value="/insertqualificationlevel",consumes = "multipart/form-data", produces = "application/json")
		public ResponseEntity<Object> insertQualificationLevel(@RequestParam("qualificationname") String qualificationname,
				@RequestParam("educationlevelid") int educationlevelid,@RequestParam("code") String code,@RequestParam("createdby") int createdby,@RequestParam("type") String type) {
		    return ResponseEntity.ok(deptAndDesigAndUniversityService.insertQualificationLevel(qualificationname,educationlevelid,code,createdby,type));
		}
		
		@PostMapping(value="/insertbranch",consumes = "multipart/form-data", produces = "application/json")
		public ResponseEntity<Object> insertBranch(@RequestParam("qualificationid") String qualificationid ,
				@RequestParam("branchname") String branchname,@RequestParam("createdby") int createdby,@RequestParam("type") String type) {
		    return ResponseEntity.ok(deptAndDesigAndUniversityService.insertBranch(qualificationid,branchname,createdby,type));
		}
		@PostMapping(value="/getqualification",produces = "application/json")
		public ResponseEntity<Object> getQualification() {
		    return ResponseEntity.ok(deptAndDesigAndUniversityService.getQualification());
		}
		@PostMapping(value="/getbranch",produces = "application/json")
		public ResponseEntity<Object> getBranch() {
		    return ResponseEntity.ok(deptAndDesigAndUniversityService.getBranch());
		}
		@PostMapping(value="/editqualificationlevel",consumes = "multipart/form-data", produces = "application/json")
		public ResponseEntity<Object> editqualificationlevel(@RequestParam("qualificationid") String qualificationid ,@RequestParam("qualificationname") String qualificationname,
				@RequestParam("educationlevelid") int educationlevelid,@RequestParam("code") String code,@RequestParam("createdby") int createdby,@RequestParam("type") String type) {
		    return ResponseEntity.ok(deptAndDesigAndUniversityService.editqualificationlevel(qualificationid,qualificationname,educationlevelid,code,createdby,type));
		}
		@PostMapping(value="/editbranch",consumes = "multipart/form-data", produces = "application/json")
		public ResponseEntity<Object> editbranch(@RequestParam("qualificationid") String qualificationid ,@RequestParam("branchid") String branchid,
				@RequestParam("branchname") String branchname,@RequestParam("createdby") int createdby,@RequestParam("type") String type) {
		    return ResponseEntity.ok(deptAndDesigAndUniversityService.editbranch(qualificationid,branchid,branchname,createdby,type));
		}
		@PostMapping(value="/employeedata",consumes = "multipart/form-data", produces = "application/json")
		public ResponseEntity<Object> leavebalanceEligibleEmp(@RequestParam("employeeSequenceNo") String employeeSequenceNo) {
		    return ResponseEntity.ok(deptAndDesigAndUniversityService.leavebalanceEligibleEmp(employeeSequenceNo));
		}
		  
		//leave quota
		@PostMapping(value="/assignedleaves",consumes = "multipart/form-data", produces = "application/json")
		public ResponseEntity<Object> leavebalance(@RequestParam("employeeSequenceNo") String employeeSequenceNo,@RequestParam("location") String location) {
		    return ResponseEntity.ok(deptAndDesigAndUniversityService.leavebalance(employeeSequenceNo,location));
		}
		//unassigned
		@PostMapping(value="/unassignedleaves",consumes = "multipart/form-data", produces = "application/json")
		public ResponseEntity<Object> leaveunassignedleavebalance(@RequestParam("employeeSequenceNo") String employeeSequenceNo,@RequestParam("location") String location) {
		    return ResponseEntity.ok(deptAndDesigAndUniversityService.leaveunassignedleavebalance(employeeSequenceNo,location));
		}
		@PostMapping(value="/action",consumes = "multipart/form-data", produces = "application/json")
		public ResponseEntity<Object> leaveAction(@RequestParam("Leavetypeid") String Leavetypeid,@RequestParam("employeeid") String employeeid,@RequestParam("createdby") String createdby,
				@RequestParam("actiontype") String actiontype,@RequestParam("Total") String Total,@RequestParam("employeesequenceno") String employeesequenceno) {
		    return ResponseEntity.ok(deptAndDesigAndUniversityService.leaveaction(Leavetypeid,employeeid,createdby,actiontype,Total,employeesequenceno));
		}
		
		//reports
		@GetMapping(value = "/payperiodyear", produces = "application/json")
		public ResponseEntity<List<Map<String, Object>>> getDistinctPayperiod() {
			return  ResponseEntity.ok(deptAndDesigAndUniversityService.getDistinctPayPeriod());
		}
	   //leavedata report
		@PostMapping(value="/leavereport",consumes = "multipart/form-data", produces = "application/json")
		public ResponseEntity<Object> leaveReport(@RequestParam("payperioddate") String payperioddate) {
		    return ResponseEntity.ok(deptAndDesigAndUniversityService.leaveReport(payperioddate));
		}
	

}
