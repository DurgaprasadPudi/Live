package com.hetero.heteroiconnect.requisition.forms;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyBasicDetailsDTO {
    private String hodId;
    private String department;
    private String location;
    private String effectiveDate;
    private List<EmployeeStrengthAndVacanciesDTO> employeeStrengthAndVacanciesDTO;

    // Add no-args constructor
    public CompanyBasicDetailsDTO() {}

    public CompanyBasicDetailsDTO(String hodId, String department, String location, String effectiveDate,
            List<EmployeeStrengthAndVacanciesDTO> employeeStrengthAndVacanciesDTO) {
        this.hodId = hodId;
        this.department = department;
        this.location = location;
        this.effectiveDate = effectiveDate;
        this.employeeStrengthAndVacanciesDTO = employeeStrengthAndVacanciesDTO;
    }

    public String getHodId() { return hodId; }
    public void setHodId(String hodId) { this.hodId = hodId; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(String effectiveDate) { this.effectiveDate = effectiveDate; }
    public List<EmployeeStrengthAndVacanciesDTO> getEmployeeStrengthAndVacanciesDTO() { return employeeStrengthAndVacanciesDTO; }
    public void setEmployeeStrengthAndVacanciesDTO(
            List<EmployeeStrengthAndVacanciesDTO> employeeStrengthAndVacanciesDTO) {
        this.employeeStrengthAndVacanciesDTO = employeeStrengthAndVacanciesDTO;
    }
}