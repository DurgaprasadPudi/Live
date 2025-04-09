package com.hetero.heteroiconnect.worksheet.model;

import java.util.List;

public class TotalWorkingHours {
	private String workDurationCalculation;
	private List<DailyWorkSheet> list;

	public TotalWorkingHours(String workDurationCalculation, List<DailyWorkSheet> list) {
		super();
		this.workDurationCalculation = workDurationCalculation;
		this.list = list;
	}

	public String getWorkDurationCalculation() {
		return workDurationCalculation;
	}

	public void setWorkDurationCalculation(String workDurationCalculation) {
		this.workDurationCalculation = workDurationCalculation;
	}

	public List<DailyWorkSheet> getList() {
		return list;
	}

	public void setList(List<DailyWorkSheet> list) {
		this.list = list;
	}

}
