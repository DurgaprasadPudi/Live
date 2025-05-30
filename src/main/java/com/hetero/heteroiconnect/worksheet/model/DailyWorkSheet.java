package com.hetero.heteroiconnect.worksheet.model;

import java.sql.Time;
import java.util.Date;

public class DailyWorkSheet {
	private Integer sno;
	private Integer employeeId;
	private Date taskDate;
	private String team;
	private String name;
	private String timeBlock;
	private String taskDescription;
	private String projectName;
	private String dependentPerson;
	private String categoryName;
	private String activityName;
	private String priorityName;
	private String outcomeName;
	private String taskTypeName;
	private String plannedAdhocName;
	private String taskAlignmentName;
	private Time startTime;
	private Time endTime;
	private String duration;
	private String remarks;
	private String workPlace;
	private String module;
	private Integer categoryId;
	private Integer activityId;
	private Integer priorityId;
	private Integer outcomeId;
	private Integer taskTypeId;
	private Integer workPlaceId;
	private Integer taskAlignmentId;
	private Integer plannedAdhocId;
	private String dependentPersonName;
	private String teamName;
	private String departmentName;
	private String departmentId;

	public DailyWorkSheet(Integer sno, Integer employeeId, Date taskDate, String team, String name, String timeBlock,
			String taskDescription, String projectName, String dependentPerson, String categoryName,
			String activityName, String priorityName, String outcomeName, String taskTypeName, String plannedAdhocName,
			String taskAlignmentName, Time startTime, Time endTime, String duration, String remarks, String workPlace,
			String module, Integer categoryId, Integer activityId, Integer priorityId, Integer outcomeId,
			Integer taskTypeId, Integer workPlaceId, Integer taskAlignmentId, Integer plannedAdhocId,
			String dependentPersonName, String teamName, String departmentName, String departmentId) {
		super();
		this.sno = sno;
		this.employeeId = employeeId;
		this.taskDate = taskDate;
		this.team = team;
		this.name = name;
		this.timeBlock = timeBlock;
		this.taskDescription = taskDescription;
		this.projectName = projectName;
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
		this.workPlace = workPlace;
		this.module = module;
		this.categoryId = categoryId;
		this.activityId = activityId;
		this.priorityId = priorityId;
		this.outcomeId = outcomeId;
		this.taskTypeId = taskTypeId;
		this.workPlaceId = workPlaceId;
		this.taskAlignmentId = taskAlignmentId;
		this.plannedAdhocId = plannedAdhocId;
		this.dependentPersonName = dependentPersonName;
		this.teamName = teamName;
		this.departmentName = departmentName;
		this.departmentId = departmentId;
	}

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

	public String getWorkPlace() {
		return workPlace;
	}

	public void setWorkPlace(String workPlace) {
		this.workPlace = workPlace;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
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

	public Integer getWorkPlaceId() {
		return workPlaceId;
	}

	public void setWorkPlaceId(Integer workPlaceId) {
		this.workPlaceId = workPlaceId;
	}

	public Integer getTaskAlignmentId() {
		return taskAlignmentId;
	}

	public void setTaskAlignmentId(Integer taskAlignmentId) {
		this.taskAlignmentId = taskAlignmentId;
	}

	public Integer getPlannedAdhocId() {
		return plannedAdhocId;
	}

	public void setPlannedAdhocId(Integer plannedAdhocId) {
		this.plannedAdhocId = plannedAdhocId;
	}

	public String getDependentPersonName() {
		return dependentPersonName;
	}

	public void setDependentPersonName(String dependentPersonName) {
		this.dependentPersonName = dependentPersonName;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

}