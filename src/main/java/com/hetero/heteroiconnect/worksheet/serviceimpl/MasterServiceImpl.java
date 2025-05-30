package com.hetero.heteroiconnect.worksheet.serviceimpl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hetero.heteroiconnect.worksheet.model.Master;
import com.hetero.heteroiconnect.worksheet.repository.MasterRepository;
import com.hetero.heteroiconnect.worksheet.service.MasterService;

 

@Service
public class MasterServiceImpl implements MasterService {
	private MasterRepository masterRepository;

	public MasterServiceImpl(MasterRepository masterRepository) {
		this.masterRepository = masterRepository;
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<Master> getTaskAlignment() {
		return masterRepository.getTaskAlignment();
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<Master> getCategory() {
		return masterRepository.getCategory();
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<Master> getPriority() {
		return masterRepository.getPriority();
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<Master> getTaskType() {
		return masterRepository.getTaskType();
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<Master> getPlannedorAdhoc() {
		return masterRepository.getPlannedorAdhoc();
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<Master> getOutcome() {
		return masterRepository.getOutcome();
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<Master> getActivities(int categoryId) {
		return masterRepository.getActivities(categoryId);
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<Master> getWorkPlace() {
		return masterRepository.getWorkPlace();
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<Master> getTeam(int reportingId,int loginby) {
		return masterRepository.getTeam(reportingId,loginby);
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<Master> getDependentName(String name) {
		return masterRepository.getDependentName(name);
	}
	@Transactional(rollbackFor = Throwable.class)
	public List<Master> getDept(int reportingId) {
		return masterRepository.getDept(reportingId);
	}
}