package com.hetero.heteroiconnect.jobcode;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface JobcodeDao {
	public int uploadFile(String jcode, String eid, MultipartFile file);

	public Map<String,Object> checkJobcode(String jcode);

	public Object checkJobcodeGetData(String eid);
	
	public Map<String,Object> checkJobcodeEmp(String eid);
	
	public EmployeeJobcode GetEmpData(String eid);
	
	public  Map<String, Object>  checkFile(String file);
}
