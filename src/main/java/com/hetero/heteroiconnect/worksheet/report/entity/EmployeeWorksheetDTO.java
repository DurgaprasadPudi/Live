package com.hetero.heteroiconnect.worksheet.report.entity;

public class EmployeeWorksheetDTO {

	private int employeeId;
	private String taskDate;
	private String teamName;
	private String employeeName;
	private String timeBlock;
	private String taskDescription;
	private String projectName;
	private String module;
	private String dependentPerson;
	private String categoryName;
	private String activityName;
	private String priorityName;
	private String outcomeName;
	private String taskTypeName;
	private String plannedAdhocName;
	private String taskAlignmentName;
	private String startTime;
	private String endTime;
	private String duration;
	private String remarks;
	private String workplaceName;
	private String status;
	private String Att_in;
	private String Att_out;
	private String Net_hours;



	public EmployeeWorksheetDTO() {
		// TODO Auto-generated constructor stub
	}

	// Getters and setters
	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getTaskDate() {
		return taskDate;
	}

	public void setTaskDate(String taskDate) {
		this.taskDate = taskDate;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getTimeBlock() {
		return timeBlock;
	}

	public void setTimeBlock(String timeBlock) {
		this.timeBlock = timeBlock;
	}

	public String getTaskDescription() {
		return taskDescription;
	}

	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getDependentPerson() {
		return dependentPerson;
	}

	public void setDependentPerson(String dependentPerson) {
		this.dependentPerson = dependentPerson;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getPriorityName() {
		return priorityName;
	}

	public void setPriorityName(String priorityName) {
		this.priorityName = priorityName;
	}

	public String getOutcomeName() {
		return outcomeName;
	}

	public void setOutcomeName(String outcomeName) {
		this.outcomeName = outcomeName;
	}

	public String getTaskTypeName() {
		return taskTypeName;
	}

	public void setTaskTypeName(String taskTypeName) {
		this.taskTypeName = taskTypeName;
	}

	public String getPlannedAdhocName() {
		return plannedAdhocName;
	}

	public void setPlannedAdhocName(String plannedAdhocName) {
		this.plannedAdhocName = plannedAdhocName;
	}

	public String getTaskAlignmentName() {
		return taskAlignmentName;
	}

	public void setTaskAlignmentName(String taskAlignmentName) {
		this.taskAlignmentName = taskAlignmentName;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getWorkplaceName() {
		return workplaceName;
	}

	public void setWorkplaceName(String workplaceName) {
		this.workplaceName = workplaceName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAtt_in() {
		return Att_in;
	}

	public void setAtt_in(String att_in) {
		Att_in = att_in;
	}

	public String getAtt_out() {
		return Att_out;
	}

	public void setAtt_out(String att_out) {
		Att_out = att_out;
	}

	public String getNet_hours() {
		return Net_hours;
	}

	public void setNet_hours(String net_hours) {
		Net_hours = net_hours;
	}

	public EmployeeWorksheetDTO(int employeeId, String taskDate, String teamName, String employeeName, String timeBlock,
			String taskDescription, String projectName, String module, String dependentPerson, String categoryName,
			String activityName, String priorityName, String outcomeName, String taskTypeName, String plannedAdhocName,
			String taskAlignmentName, String startTime, String endTime, String duration, String remarks,
			String workplaceName, String status, String att_in, String att_out, String net_hours) {
		super();
		this.employeeId = employeeId;
		this.taskDate = taskDate;
		this.teamName = teamName;
		this.employeeName = employeeName;
		this.timeBlock = timeBlock;
		this.taskDescription = taskDescription;
		this.projectName = projectName;
		this.module = module;
		this.dependentPerson = dependentPerson;
		this.categoryName = categoryName;
		this.activityName = activityName;
		this.priorityName = priorityName;
		this.outcomeName = outcomeName;
		this.taskTypeName = taskTypeName;
		this.plannedAdhocName = plannedAdhocName;
		this.taskAlignmentName = taskAlignmentName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.duration = duration;
		this.remarks = remarks;
		this.workplaceName = workplaceName;
		this.status = status;
		Att_in = att_in;
		Att_out = att_out;
		Net_hours = net_hours;
	}
	
}
