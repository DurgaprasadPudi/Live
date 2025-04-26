package com.hetero.heteroiconnect.worksheet.report;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.hetero.heteroiconnect.worksheet.report.entity.TeamDTO;

 

public interface WorksheetDownloadService {



	public byte[] Download(int year, int month, String employeeid, String formDate, String toDate,int teamS) throws IOException, ExecutionException, InterruptedException;

	/*
	 * byte[] selfEmployeeDownload(int year, int month, String employeeid, String
	 * fromDate, String toDate);
	 */

	public Object getEmployees(Integer year, Integer month, String employeeid, String fromDate, String toDate);

	public Object getEmployeesByEmployeeid(Integer year, Integer month, String employeeid, String fromDate,
			String toDate);

	public List<TeamDTO> getTeams(String employeeid);

	public byte[] selfEmployeeDownload(Integer year, Integer month, String employeeid, String fromDate, String toDate);





	
}
