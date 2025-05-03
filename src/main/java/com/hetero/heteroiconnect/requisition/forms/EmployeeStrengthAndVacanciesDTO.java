package com.hetero.heteroiconnect.requisition.forms;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeStrengthAndVacanciesDTO {
    private int id;
    private String designation;
    private String presentStrength;
    private String approvedVacancies;
    private String vacancyA;
    private String vacancyB;
    private String vacancyC;
    private String vacancyD;
    private String remarks;

    // Add no-args constructor
    public EmployeeStrengthAndVacanciesDTO() {}

    public EmployeeStrengthAndVacanciesDTO(int id, String designation, String presentStrength, String approvedVacancies,
            String vacancyA, String vacancyB, String vacancyC, String vacancyD, String remarks) {
        this.id = id;
        this.designation = designation;
        this.presentStrength = presentStrength;
        this.approvedVacancies = approvedVacancies;
        this.vacancyA = vacancyA;
        this.vacancyB = vacancyB;
        this.vacancyC = vacancyC;
        this.vacancyD = vacancyD;
        this.remarks = remarks;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }
    public String getPresentStrength() { return presentStrength; }
    public void setPresentStrength(String presentStrength) { this.presentStrength = presentStrength; }
    public String getApprovedVacancies() { return approvedVacancies; }
    public void setApprovedVacancies(String approvedVacancies) { this.approvedVacancies = approvedVacancies; }
    public String getVacancyA() { return vacancyA; }
    public void setVacancyA(String vacancyA) { this.vacancyA = vacancyA; }
    public String getVacancyB() { return vacancyB; }
    public void setVacancyB(String vacancyB) { this.vacancyB = vacancyB; }
    public String getVacancyC() { return vacancyC; }
    public void setVacancyC(String vacancyC) { this.vacancyC = vacancyC; }
    public String getVacancyD() { return vacancyD; }
    public void setVacancyD(String vacancyD) { this.vacancyD = vacancyD; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}