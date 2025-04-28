package com.hetero.heteroiconnect.worksheet.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hetero.heteroiconnect.worksheet.report.Exception.NoDataFoundException;
import com.hetero.heteroiconnect.worksheet.report.Exception.NotFiledDataException;
import com.hetero.heteroiconnect.worksheet.report.entity.EmployeeWorksheetDTO;
import com.hetero.heteroiconnect.worksheet.report.entity.TeamDTO;

 

@Service
public class WorksheetDownloadImpl implements WorksheetDownloadService {

	@Autowired
	private WorksheetDownloadRepository worksheetRepository;

	public byte[] Download(int year, int month, String employeeid, String formDate, String toDate, int teamS)
			throws IOException, ExecutionException, InterruptedException {
		Workbook workbook = new XSSFWorkbook();
		List<Object[]> teams = worksheetRepository.tabs(employeeid);

		if (teamS == 0 || teams.isEmpty()) {
			List<EmployeeWorksheetDTO> data;
			if (year > 0 && month > 0) {
				data = worksheetRepository.EmployeeWorksheets(employeeid, year, month).stream().map(this::mapToDTO)
						.collect(Collectors.toList());
			} else {
				data = worksheetRepository.dateEmployeeWorksheets(employeeid, formDate, toDate).stream()
						.map(this::mapToDTO).collect(Collectors.toList());
			}

			if (data.isEmpty()) {
				throw new NoDataFoundException("No data found for the given parameters.");
			}

			Sheet sheet = workbook.createSheet("Worksheet");
			CellStyle headerCellStyle = createHeaderCellStyle(workbook);
			CellStyle timeCellStyle = createTimeCellStyle(workbook);

			Row headerRow = sheet.createRow(0);
			String[] headers = { "Employee ID", "Task Date", "Team Name", "Employee Name", "Time Block",
					"Task Description", "Project Name", "Module", "Dependent Person", "Category Name", "Activity Name",
					"Priority Name", "Outcome Name", "Task Type Name", "Planned Adhoc Name", "Task Alignment Name",
					"Start Time", "End Time", "Duration", "Remarks", "Workplace Name", "Status", "iconnect_in",
					"iconnect_out", "iconnect_duration" };
			populateHeaderRow(headerRow, headers, headerCellStyle);

			AtomicInteger rowNum = new AtomicInteger(1);
			data.forEach(d -> populateRow(sheet, rowNum, d, timeCellStyle));
			IntStream.range(0, headers.length).forEach(sheet::autoSizeColumn);
		} else if (teamS == 999 && !teams.isEmpty()) {
			AtomicBoolean dataFound = new AtomicBoolean(false);
			List<CompletableFuture<Void>> futures = teams.stream().map(team -> {
				String tabName = (String) team[1];
				int tabId = (int) team[0];

				return CompletableFuture.runAsync(() -> {
					try {
						List<EmployeeWorksheetDTO> teamData;
						if (year > 0 && month > 0) {
							teamData = worksheetRepository.fetchEmployeeWorksheets(tabId, year, month).stream()
									.map(this::mapToDTO).collect(Collectors.toList());
						} else {
							teamData = worksheetRepository.betweenDateEmployeeWorksheets(tabId, formDate, toDate)
									.stream().map(this::mapToDTO).collect(Collectors.toList());
						}

						if (!teamData.isEmpty()) {
							dataFound.set(true);
							synchronized (workbook) {
								Sheet sheet = workbook.createSheet(tabName);
								CellStyle headerCellStyle = createHeaderCellStyle(workbook);
								CellStyle timeCellStyle = createTimeCellStyle(workbook);

								Row headerRow = sheet.createRow(0);
								String[] headers = { "Employee ID", "Task Date", "Team Name", "Employee Name",
										"Time Block", "Task Description", "Project Name", "Module", "Dependent Person",
										"Category Name", "Activity Name", "Priority Name", "Outcome Name",
										"Task Type Name", "Planned Adhoc Name", "Task Alignment Name", "Start Time",
										"End Time", "Duration", "Remarks", "Workplace Name", "Status", "iconnect_in",
										"iconnect_out", "iconnect_duration" };
								populateHeaderRow(headerRow, headers, headerCellStyle);

								AtomicInteger rowNum = new AtomicInteger(1);
								teamData.forEach(dto -> populateRow(sheet, rowNum, dto, timeCellStyle));
								IntStream.range(0, headers.length).forEach(sheet::autoSizeColumn);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			}).collect(Collectors.toList());

			CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

			if (!dataFound.get()) {
				throw new NoDataFoundException("No data found for the given parameters.");
			}
		} else {
			List<EmployeeWorksheetDTO> teamData;
			if (year > 0 && month > 0) {
				teamData = worksheetRepository.fetchEmployeeWorksheets(teamS, year, month).stream().map(this::mapToDTO)
						.collect(Collectors.toList());
			} else {
				teamData = worksheetRepository.betweenDateEmployeeWorksheets(teamS, formDate, toDate).stream()
						.map(this::mapToDTO).collect(Collectors.toList());
			}

			synchronized (workbook) {
				try {
					Sheet sheet = workbook.createSheet("worksheet");
					CellStyle headerCellStyle = createHeaderCellStyle(workbook);
					CellStyle timeCellStyle = createTimeCellStyle(workbook);

					Row headerRow = sheet.createRow(0);
					String[] headers = { "Employee ID", "Task Date", "Team Name", "Employee Name", "Time Block",
							"Task Description", "Project Name", "Module", "Dependent Person", "Category Name",
							"Activity Name", "Priority Name", "Outcome Name", "Task Type Name", "Planned Adhoc Name",
							"Task Alignment Name", "Start Time", "End Time", "Duration", "Remarks", "Workplace Name",
							"Status", "iconnect_in", "iconnect_out", "iconnect_duration" };
					populateHeaderRow(headerRow, headers, headerCellStyle);

					AtomicInteger rowNum = new AtomicInteger(1);
					teamData.forEach(dto -> populateRow(sheet, rowNum, dto, timeCellStyle));
					IntStream.range(0, headers.length).forEach(sheet::autoSizeColumn);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			workbook.write(baos);
			return baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			workbook.close();
		}
	}

	private EmployeeWorksheetDTO mapToDTO(Object[] record) {
		int employeeId = (int) record[0];
		String taskDate = (record[1] != null) ? new SimpleDateFormat("yyyy-MM-dd").format(record[1]) : "";
		String teamName = (String) record[2];
		String employeeName = (String) record[3];
		String timeBlock = (String) record[4];
		String taskDescription = (String) record[5];
		String projectName = (String) record[6];
		String module = (String) record[7];
		String dependentPerson = (String) record[8];
		String categoryName = (String) record[9];
		String activityName = (String) record[10];
		String priorityName = (String) record[11];
		String outcomeName = (String) record[12];
		String taskTypeName = (String) record[13];
		String plannedAdhocName = (String) record[14];
		String taskAlignmentName = (String) record[15];
		String startTime = (record[16] != null) ? record[16].toString() : "";
		String endTime = (record[17] != null) ? record[17].toString() : "";
		String duration = (record[18] != null) ? record[18].toString() : "";
		String remarks = (String) record[19];
		String workplaceName = (String) record[20];
		String status = (String) record[21];
		String Att_in = (String) record[22];
		String Att_out = (String) record[23];
		String Net_hours = (String) record[24];
		return new EmployeeWorksheetDTO(employeeId, taskDate, teamName, employeeName, timeBlock, taskDescription,
				projectName, module, dependentPerson, categoryName, activityName, priorityName, outcomeName,
				taskTypeName, plannedAdhocName, taskAlignmentName, startTime, endTime, duration, remarks, workplaceName,
				status, Att_in, Att_out, Net_hours);
	}

//	private void populateHeaderRow(Row headerRow, String[] headers, CellStyle headerCellStyle) {
//		IntStream.range(0, headers.length).forEach(i -> {
//			var cell = headerRow.createCell(i);
//			cell.setCellValue(headers[i]);
//			cell.setCellStyle(headerCellStyle);
//		});
//	}
	
	private void populateHeaderRow(Row headerRow, String[] headers, CellStyle headerCellStyle) {
	    IntStream.range(0, headers.length).forEach(i -> {
	        Cell cell = headerRow.createCell(i);
	        cell.setCellValue(headers[i]);
	        cell.setCellStyle(headerCellStyle);
	    });
	}

	private void populateRow(Sheet sheet, AtomicInteger rowNum, EmployeeWorksheetDTO employeeWorksheet,
			CellStyle timeCellStyle) {
		Row row = sheet.createRow(rowNum.getAndIncrement());
		row.createCell(0).setCellValue(employeeWorksheet.getEmployeeId());
		row.createCell(1).setCellValue(employeeWorksheet.getTaskDate());
		row.createCell(2).setCellValue(employeeWorksheet.getTeamName());
		row.createCell(3).setCellValue(employeeWorksheet.getEmployeeName());
		row.createCell(4).setCellValue(employeeWorksheet.getTimeBlock());
		row.createCell(5).setCellValue(employeeWorksheet.getTaskDescription());
		row.createCell(6).setCellValue(employeeWorksheet.getProjectName());
		row.createCell(7).setCellValue(employeeWorksheet.getModule());
		row.createCell(8).setCellValue(employeeWorksheet.getDependentPerson());
		row.createCell(9).setCellValue(employeeWorksheet.getCategoryName());
		row.createCell(10).setCellValue(employeeWorksheet.getActivityName());
		row.createCell(11).setCellValue(employeeWorksheet.getPriorityName());
		row.createCell(12).setCellValue(employeeWorksheet.getOutcomeName());
		row.createCell(13).setCellValue(employeeWorksheet.getTaskTypeName());
		row.createCell(14).setCellValue(employeeWorksheet.getPlannedAdhocName());
		row.createCell(15).setCellValue(employeeWorksheet.getTaskAlignmentName());
		if (employeeWorksheet.getStartTime() != null) {
			row.createCell(16).setCellValue(employeeWorksheet.getStartTime());
			row.getCell(16).setCellStyle(timeCellStyle);
		}
		if (employeeWorksheet.getEndTime() != null) {
			row.createCell(17).setCellValue(employeeWorksheet.getEndTime());
			row.getCell(17).setCellStyle(timeCellStyle);
		}
		if (employeeWorksheet.getDuration() != null) {
			row.createCell(18).setCellValue(employeeWorksheet.getDuration());
			row.getCell(18).setCellStyle(timeCellStyle);
		}
		row.createCell(19).setCellValue(employeeWorksheet.getRemarks());
		row.createCell(20).setCellValue(employeeWorksheet.getWorkplaceName());
		row.createCell(21).setCellValue(employeeWorksheet.getStatus());
		row.createCell(22).setCellValue(employeeWorksheet.getAtt_in());
		row.createCell(23).setCellValue(employeeWorksheet.getAtt_out());
		row.createCell(24).setCellValue(employeeWorksheet.getNet_hours());
	}

	private CellStyle createHeaderCellStyle(Workbook workbook) {
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		return headerCellStyle;
	}

	private CellStyle createTimeCellStyle(Workbook workbook) {
		CellStyle timeCellStyle = workbook.createCellStyle();
		DataFormat dataFormat = workbook.createDataFormat();
		timeCellStyle.setDataFormat(dataFormat.getFormat("HH:mm:ss"));
		return timeCellStyle;
	}

	@Override
	public List<WorksheetEmployees> getEmployees(Integer year, Integer month, String employeeid, String fromDate,
			String toDate) {
		List<Object[]> teams = worksheetRepository.tabs(employeeid);
		List<Object[]> data = new ArrayList<>();
		List<WorksheetEmployees> allData = new ArrayList<>();
		if (teams == null || teams.isEmpty()) {
			if (year > 0 && month > 0) {
				data = worksheetRepository.fetchEmployeeWorkDataByEmployeeeid(employeeid, year, month);

			} else {
				data = worksheetRepository.fetchEmployeeWorkDataByEmployeeeid(employeeid, fromDate, toDate);
			}
			if (data.isEmpty()) {
				throw new NotFiledDataException("No Data Found.");
			}
			if (data != null) {
				for (Object[] row : data) {
					WorksheetEmployees employee = new WorksheetEmployees();
					employee.setEmployeeId(row[0] != null ? ((Number) row[0]).longValue() : null);
					employee.setCallName(row[1] != null ? row[1].toString() : null);
					employee.setTeamName(row[2] != null ? row[2].toString() : null);
					employee.setEmploymenttype(row[3] != null ? row[3].toString() : null);
					allData.add(employee);
				}
			}
		}
		for (Object[] team : teams) {
			Integer tabId = (Integer) team[0];
			if (tabId == null || tabId == 0)
				continue;

			if (year > 0 && month > 0) {
				data = worksheetRepository.fetchEmployeeWorkData(tabId, year, month);
			} else {
				data = worksheetRepository.fetchEmployeeWorkDataByDate(tabId, fromDate, toDate);
			}
			if (data.isEmpty()) {
				throw new NotFiledDataException("No Data Found...");
			}
			if (data != null) {
				for (Object[] row : data) {
					WorksheetEmployees employee = new WorksheetEmployees();
					employee.setEmployeeId(row[0] != null ? ((Number) row[0]).longValue() : null);
					employee.setCallName(row[1] != null ? row[1].toString() : null);
					employee.setTeamName(row[2] != null ? row[2].toString() : null);
					employee.setEmploymenttype(row[3] != null ? row[3].toString() : null);
					allData.add(employee);
				}
			}
		}

		return allData;
	}

	public List<EmployeeWorksheetDTO> getEmployeesByEmployeeid(Integer year, Integer month, String employeeid,
			String fromDate, String toDate) {
		List<Object[]> rawData;
		if (year != null && month != null && year > 0 && month > 0) {
			rawData = worksheetRepository.EmployeeWorksheets(employeeid, year, month);
		} else {
			rawData = worksheetRepository.dateEmployeeWorksheets(employeeid, fromDate, toDate);
		}
		return rawData.stream().map(record -> {
			try {
				return mapToDTOs(record);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}).filter(Objects::nonNull).collect(Collectors.toList());
	}

	private EmployeeWorksheetDTO mapToDTOs(Object[] record) {
		EmployeeWorksheetDTO dto = new EmployeeWorksheetDTO();
		dto.setEmployeeId(((Number) record[0]).intValue());
		dto.setTaskDate(String.valueOf(record[1]));
		dto.setTeamName(String.valueOf(record[2]));
		dto.setEmployeeName(String.valueOf(record[3]));
		dto.setTimeBlock(String.valueOf(record[4]));
		dto.setTaskDescription(String.valueOf(record[5]));
		dto.setProjectName(String.valueOf(record[6]));
		dto.setModule(String.valueOf(record[7]));
		dto.setDependentPerson(String.valueOf(record[8]));
		dto.setCategoryName(String.valueOf(record[9]));
		dto.setActivityName(String.valueOf(record[10]));
		dto.setPriorityName(String.valueOf(record[11]));
		dto.setOutcomeName(String.valueOf(record[12]));
		dto.setTaskTypeName(String.valueOf(record[13]));
		dto.setPlannedAdhocName(String.valueOf(record[14]));
		dto.setTaskAlignmentName(String.valueOf(record[15]));
		dto.setStartTime(String.valueOf(record[16]));
		dto.setEndTime(String.valueOf(record[17]));
		dto.setDuration(String.valueOf(record[18]));
		dto.setRemarks(String.valueOf(record[19]));
		dto.setWorkplaceName(String.valueOf(record[20]));
		dto.setStatus(String.valueOf(record[21]));
		dto.setAtt_in(String.valueOf(record[22]));
		dto.setAtt_out(String.valueOf(record[23]));
		dto.setNet_hours(String.valueOf(record[24]));
		return dto;
	}

//	@Override
//	public List<TeamDTO> getTeams(String employeeid) {
//	    return worksheetRepository.getTeams(employeeid).stream()
//	        .map(row -> new TeamDTO(
//	            row[0] != null ? String.valueOf(row[0]) : null,  // Convert Integer to String safely
//	            row[1] != null ? row[1].toString() : null         // Already a String, but safe check
//	        ))
//	        .collect(Collectors.toList());
//	}

	public List<TeamDTO> getTeams(String employeeid) {
		List<Object[]> teamsFromDb = worksheetRepository.getTeams(employeeid);
		System.err.println("Teams from DB size: " + teamsFromDb.size());
		List<TeamDTO> teamDTOList = teamsFromDb.stream().map(
				row -> new TeamDTO(row[0] != null ? (Integer) row[0] : 0, row[1] != null ? row[1].toString() : null))
				.collect(Collectors.toList());
		teamDTOList.stream().anyMatch(team -> employeeid.equals(String.valueOf(team.getTeamId())));
		teamDTOList.add(new TeamDTO(0, "Self"));

		if (teamsFromDb.size() > 3) {
			teamDTOList.add(new TeamDTO(999, "AllTeams"));

		}
		return teamDTOList;
	}

	@Override
	public byte[] selfEmployeeDownload(Integer year, Integer month, String employeeid, String fromDate, String toDate) {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet(employeeid);

		try {
			List<EmployeeWorksheetDTO> data;

			if (year > 0 && month > 0) {
				data = worksheetRepository.EmployeeWorksheets(employeeid, year, month).stream()
						.map(record -> mapToDTO(record)).collect(Collectors.toList());
			} else {
				data = worksheetRepository.dateEmployeeWorksheets(employeeid, fromDate, toDate).stream()
						.map(record -> mapToDTO(record)).collect(Collectors.toList());
			}

			if (data.isEmpty()) {
				throw new NoDataFoundException("No data found for the given parameters.");
			}

			// Create Excel sheet content if data exists
			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
			headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			// Time cell style
			CellStyle timeCellStyle = workbook.createCellStyle();
			DataFormat dataFormat = workbook.createDataFormat();
			timeCellStyle.setDataFormat(dataFormat.getFormat("HH:mm:ss"));

			Row headerRow = sheet.createRow(0);
			String[] headers = { "Employee ID", "Task Date", "Team Name", "Employee Name", "Time Block",
					"Task Description", "Project Name", "Module", "Dependent Person", "Category Name", "Activity Name",
					"Priority Name", "Outcome Name", "Task Type Name", "Planned Adhoc Name", "Task Alignment Name",
					"Start Time", "End Time", "Duration", "Remarks", "Workplace Name", "Status", "iconnect_in",
					"iconnect_out", "iconnect_duration" };

			IntStream.range(0, headers.length).forEach(i -> {
			    Cell cell = headerRow.createCell(i); // Replace 'var' with 'Cell'
			    cell.setCellValue(headers[i]);
			    cell.setCellStyle(headerCellStyle);
			});

			// Populate data rows
			AtomicInteger rowNum = new AtomicInteger(1);
			data.forEach(employeeWorksheet -> {
				Row row = sheet.createRow(rowNum.getAndIncrement());
				row.createCell(0).setCellValue(employeeWorksheet.getEmployeeId());
				row.createCell(1)
						.setCellValue(employeeWorksheet.getTaskDate() != null ? employeeWorksheet.getTaskDate() : "");
				row.createCell(2)
						.setCellValue(employeeWorksheet.getTeamName() != null ? employeeWorksheet.getTeamName() : "");
				row.createCell(3).setCellValue(
						employeeWorksheet.getEmployeeName() != null ? employeeWorksheet.getEmployeeName() : "");
				row.createCell(4)
						.setCellValue(employeeWorksheet.getTimeBlock() != null ? employeeWorksheet.getTimeBlock() : "");
				row.createCell(5).setCellValue(
						employeeWorksheet.getTaskDescription() != null ? employeeWorksheet.getTaskDescription() : "");
				row.createCell(6).setCellValue(
						employeeWorksheet.getProjectName() != null ? employeeWorksheet.getProjectName() : "");
				row.createCell(7)
						.setCellValue(employeeWorksheet.getModule() != null ? employeeWorksheet.getModule() : "");
				row.createCell(8).setCellValue(
						employeeWorksheet.getDependentPerson() != null ? employeeWorksheet.getDependentPerson() : "");
				row.createCell(9).setCellValue(
						employeeWorksheet.getCategoryName() != null ? employeeWorksheet.getCategoryName() : "");
				row.createCell(10).setCellValue(
						employeeWorksheet.getActivityName() != null ? employeeWorksheet.getActivityName() : "");
				row.createCell(11).setCellValue(
						employeeWorksheet.getPriorityName() != null ? employeeWorksheet.getPriorityName() : "");
				row.createCell(12).setCellValue(
						employeeWorksheet.getOutcomeName() != null ? employeeWorksheet.getOutcomeName() : "");
				row.createCell(13).setCellValue(
						employeeWorksheet.getTaskTypeName() != null ? employeeWorksheet.getTaskTypeName() : "");
				row.createCell(14).setCellValue(
						employeeWorksheet.getPlannedAdhocName() != null ? employeeWorksheet.getPlannedAdhocName() : "");
				row.createCell(15)
						.setCellValue(employeeWorksheet.getTaskAlignmentName() != null
								? employeeWorksheet.getTaskAlignmentName()
								: "");

				if (employeeWorksheet.getStartTime() != null) {
					row.createCell(16).setCellValue(employeeWorksheet.getStartTime());
					row.getCell(16).setCellStyle(timeCellStyle);
				}
				if (employeeWorksheet.getEndTime() != null) {
					row.createCell(17).setCellValue(employeeWorksheet.getEndTime());
					row.getCell(17).setCellStyle(timeCellStyle);
				}
				if (employeeWorksheet.getDuration() != null) {
					row.createCell(18).setCellValue(employeeWorksheet.getDuration());
					row.getCell(18).setCellStyle(timeCellStyle);
				}
				row.createCell(19)
						.setCellValue(employeeWorksheet.getRemarks() != null ? employeeWorksheet.getRemarks() : "");
				row.createCell(20).setCellValue(
						employeeWorksheet.getWorkplaceName() != null ? employeeWorksheet.getWorkplaceName() : "");
				row.createCell(21)
						.setCellValue(employeeWorksheet.getStatus() != null ? employeeWorksheet.getStatus() : "");
				row.createCell(22)
						.setCellValue(employeeWorksheet.getAtt_in() != null ? employeeWorksheet.getAtt_in() : "");
				row.createCell(23)
						.setCellValue(employeeWorksheet.getAtt_out() != null ? employeeWorksheet.getAtt_out() : "");
				row.createCell(24)
						.setCellValue(employeeWorksheet.getNet_hours() != null ? employeeWorksheet.getNet_hours() : "");
			});
			IntStream.range(0, headers.length).forEach(i -> sheet.autoSizeColumn(i));

			try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
				workbook.write(baos);
				return baos.toByteArray();
			} catch (IOException e) {
				e.printStackTrace();
				throw new IOException("Error generating Excel file", e);
			} finally {
				workbook.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error generating Excel file", e);
		}
	}

}
