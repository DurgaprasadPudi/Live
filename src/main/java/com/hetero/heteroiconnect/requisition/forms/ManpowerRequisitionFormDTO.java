package com.hetero.heteroiconnect.requisition.forms;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ManpowerRequisitionFormDTO {
    private Integer companyId;
    private String department;
    private String location;
    private String experienceRequired;
    private String jobTitle;
    private String qualification;
    private String reqJobDescription;
    private String skills;
    private String deptHeadSign;
    private String deptHeadName;
    private String deptHeadDate;
    private String unitHeadSign;
    private String unitHeadName;
    private String unitHeadDate;
    private String hrComments;
    private int employeeId;
    private String name;
    private int reqId;
    private String replacedQualification;
    private String dateOfResignation;
    private String designation;
    private String experience;
    private String dateOfRelieving;

    // Add no-args constructor
    public ManpowerRequisitionFormDTO() {}

    public ManpowerRequisitionFormDTO(Integer companyId, String department, String location, String experienceRequired,
            String jobTitle, String qualification, String reqJobDescription, String skills, String deptHeadSign,
            String deptHeadName, String deptHeadDate, String unitHeadSign, String unitHeadName, String unitHeadDate,
            String hrComments, int employeeId, String name, int reqId, String replacedQualification,
            String dateOfResignation, String designation, String experience, String dateOfRelieving) {
        this.companyId = companyId;
        this.department = department;
        this.location = location;
        this.experienceRequired = experienceRequired;
        this.jobTitle = jobTitle;
        this.qualification = qualification;
        this.reqJobDescription = reqJobDescription;
        this.skills = skills;
        this.deptHeadSign = deptHeadSign;
        this.deptHeadName = deptHeadName;
        this.deptHeadDate = deptHeadDate;
        this.unitHeadSign = unitHeadSign;
        this.unitHeadName = unitHeadName;
        this.unitHeadDate = unitHeadDate;
        this.hrComments = hrComments;
        this.employeeId = employeeId;
        this.name = name;
        this.reqId = reqId;
        this.replacedQualification = replacedQualification;
        this.dateOfResignation = dateOfResignation;
        this.designation = designation;
        this.experience = experience;
        this.dateOfRelieving = dateOfRelieving;
    }

    public Integer getCompanyId() { return companyId; }
    public void setCompanyId(Integer companyId) { this.companyId = companyId; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getExperienceRequired() { return experienceRequired; }
    public void setExperienceRequired(String experienceRequired) { this.experienceRequired = experienceRequired; }
    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }
    public String getReqJobDescription() { return reqJobDescription; }
    public void setReqJobDescription(String reqJobDescription) { this.reqJobDescription = reqJobDescription; }
    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }
    public String getDeptHeadSign() { return deptHeadSign; }
    public void setDeptHeadSign(String deptHeadSign) { this.deptHeadSign = deptHeadSign; }
    public String getDeptHeadName() { return deptHeadName; }
    public void setDeptHeadName(String deptHeadName) { this.deptHeadName = deptHeadName; }
    public String getDeptHeadDate() { return deptHeadDate; }
    public void setDeptHeadDate(String deptHeadDate) { this.deptHeadDate = deptHeadDate; }
    public String getUnitHeadSign() { return unitHeadSign; }
    public void setUnitHeadSign(String unitHeadSign) { this.unitHeadSign = unitHeadSign; }
    public String getUnitHeadName() { return unitHeadName; }
    public void setUnitHeadName(String unitHeadName) { this.unitHeadName = unitHeadName; }
    public String getUnitHeadDate() { return unitHeadDate; }
    public void setUnitHeadDate(String unitHeadDate) { this.unitHeadDate = unitHeadDate; }
    public String getHrComments() { return hrComments; }
    public void setHrComments(String hrComments) { this.hrComments = hrComments; }
    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getReqId() { return reqId; }
    public void setReqId(int reqId) { this.reqId = reqId; }
    public String getReplacedQualification() { return replacedQualification; }
    public void setReplacedQualification(String replacedQualification) { this.replacedQualification = replacedQualification; }
    public String getDateOfResignation() { return dateOfResignation; }
    public void setDateOfResignation(String dateOfResignation) { this.dateOfResignation = dateOfResignation; }
    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }
    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }
    public String getDateOfRelieving() { return dateOfRelieving; }
    public void setDateOfRelieving(String dateOfRelieving) { this.dateOfRelieving = dateOfRelieving; }
}