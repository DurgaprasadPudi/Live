package com.hetero.heteroiconnect.worksheet.model;

import java.sql.Time;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class UserWorksheet {
	@NotNull(message = "EmployeeId can't be null")
	@Positive(message = "EmployeeId should be Positive")
	@Min(value = 1, message = "EmployeeId must be greater than or equal to 1")
	private Integer employeeId;
	@NotNull(message = "taskDate can't be null")
	private Date taskDate;
	@NotNull(message = "Team can't be null")
	@NotBlank(message = "Team can't be empty")
	private String team;
	private String department;
	@NotNull(message = "Name can't be null")
	@NotBlank(message = "Name can't be empty")
	private String name;
	@NotNull(message = "TimeBlock can't be null")
	@NotBlank(message = "TimeBlock can't be empty")
	private String timeBlock;
	private String taskDescription;
	@NotNull(message = "ProjectName can't be null")
	@NotBlank(message = "ProjectName can't be empty")
	private String projectName;
	/*
	 * @NotNull(message = "DependentPerson can't be null")
	 * 
	 * @NotBlank(message = "DependentPerson can't be empty")
	 */
	private String dependentPerson;
	@NotNull(message = "CategoryId can't be null")
	@Positive(message = "CategoryId should be Positive")
	@Min(value = 1, message = "CategoryId must be greater than or equal to 1")
	private Integer categoryId;
	@NotNull(message = "ActivityId can't be null")
	@Positive(message = "ActivityId should be Positive")
	@Min(value = 1, message = "ActivityId must be greater than or equal to 1")
	private Integer activityId;
	@NotNull(message = "PriorityId can't be null")
	@Positive(message = "PriorityId should be Positive")
	@Min(value = 1, message = "PriorityId must be greater than or equal to 1")
	private Integer priorityId;
	@NotNull(message = "OutcomeId can't be null")
	@Positive(message = "OutcomeId should be Positive")
	@Min(value = 1, message = "OutcomeId must be greater than or equal to 1")
	private Integer outcomeId;
	@NotNull(message = "TasktypeId can't be null")
	@Positive(message = "TasktypeId should be Positive")
	@Min(value = 1, message = "TasktypeId must be greater than or equal to 1")
	private Integer taskTypeId;
	@NotNull(message = "PlannedadhocId can't be null")
	@Positive(message = "PlannedadhocId should be Positive")
	@Min(value = 1, message = "PlannedadhocId must be greater than or equal to 1")
	private Integer plannedAdhocId;
	@NotNull(message = "TaskalignmentId can't be null")
	@Positive(message = "TaskalignmentId should be Positive")
	@Min(value = 1, message = "TaskalignmentId must be greater than or equal to 1")
	private Integer taskAlignmentId;
	@NotNull(message = "StartTime can't be null")
	private Time startTime;
	@NotNull(message = "EndTime can't be null")
	private Time endTime;
	@NotNull(message = "Duration can't be null")
	private Time duration;
	@NotNull(message = "ReportingManager can't be null")
	private String reportingManager;
	private String remarks;
	@NotNull(message = "Module can't be null")
	private String module;
	@NotNull(message = "WorkPlaceId can't be null")
	@Positive(message = "WorkPlaceId should be Positive")
	@Min(value = 1, message = "WorkPlaceId must be greater than or equal to 1")
	private Integer workPlaceId;
	private Integer sno;

	public Integer getSno() {
		return sno;
	}

	public void setSno(Integer sno) {
		this.sno = sno;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public Date getTaskDate() {
		return taskDate;
	}

	public void setTaskDate(Date taskDate) {
		this.taskDate = taskDate;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getDependentPerson() {
		return dependentPerson;
	}

	public void setDependentPerson(String dependentPerson) {
		this.dependentPerson = dependentPerson;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public Integer getOutcomeId() {
		return outcomeId;
	}

	public void setOutcomeId(Integer outcomeId) {
		this.outcomeId = outcomeId;
	}

	public Integer getTaskTypeId() {
		return taskTypeId;
	}

	public void setTaskTypeId(Integer taskTypeId) {
		this.taskTypeId = taskTypeId;
	}

	public Integer getPlannedAdhocId() {
		return plannedAdhocId;
	}

	public void setPlannedAdhocId(Integer plannedAdhocId) {
		this.plannedAdhocId = plannedAdhocId;
	}

	public Integer getTaskAlignmentId() {
		return taskAlignmentId;
	}

	public void setTaskAlignmentId(Integer taskAlignmentId) {
		this.taskAlignmentId = taskAlignmentId;
	}

	public Integer getWorkPlaceId() {
		return workPlaceId;
	}

	public void setWorkPlaceId(Integer workPlaceId) {
		this.workPlaceId = workPlaceId;
	}

	public Time getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public Time getEndTime() {
		return endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}

	public Time getDuration() {
		return duration;
	}

	public void setDuration(Time duration) {
		this.duration = duration;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getReportingManager() {
		return reportingManager;
	}

	public void setReportingManager(String reportingManager) {
		this.reportingManager = reportingManager;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

}
