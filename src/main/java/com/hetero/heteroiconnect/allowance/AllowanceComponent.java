package com.hetero.heteroiconnect.allowance;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AllowanceComponent {
	private int id;
    private LocalDate effectiveDate;
    private Integer allowanceId;
    private BigDecimal allowanceAmount;
    private String allowanceName;
    
    private String createdBy;
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public LocalDate getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(LocalDate effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public Integer getAllowanceId() {
		return allowanceId;
	}
	public void setAllowanceId(Integer allowanceId) {
		this.allowanceId = allowanceId;
	}
	public BigDecimal getAllowanceAmount() {
		return allowanceAmount;
	}
	public void setAllowanceAmount(BigDecimal allowanceAmount) {
		this.allowanceAmount = allowanceAmount;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getAllowanceName() {
		return allowanceName;
	}
	public void setAllowanceName(String allowanceName) {
		this.allowanceName = allowanceName;
	}
    
}
