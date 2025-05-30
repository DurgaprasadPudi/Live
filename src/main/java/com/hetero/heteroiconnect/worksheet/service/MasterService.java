package com.hetero.heteroiconnect.worksheet.service;

import java.util.List;

import com.hetero.heteroiconnect.worksheet.model.Master;

public interface MasterService {
	public List<Master> getTaskAlignment();

	public List<Master> getCategory();

	public List<Master> getPriority();

	public List<Master> getTaskType();

	public List<Master> getOutcome();

	public List<Master> getActivities(int categoryId);

	public List<Master> getPlannedorAdhoc();

	public List<Master> getWorkPlace();

	public List<Master> getTeam(int reportingId, int loginby);

	public List<Master> getDependentName(String name);

	public List<Master> getDept(int reportingId);
}