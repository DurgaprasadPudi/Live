
package com.hetero.heteroiconnect.spaceallocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hetero.heteroiconnect.worksheet.exception.FuelAndDriverExpensesException;
import com.hetero.heteroiconnect.worksheet.utility.MessageBundleSource;

@Service
public class SpaceAllocationServiceImpl implements SpaceAllocationService {

	private final SpaceAllocationRepository repository;
	private final MessageBundleSource messageBundleSource;

	public SpaceAllocationServiceImpl(SpaceAllocationRepository repository, MessageBundleSource messageBundleSource) {
		this.repository = repository;
		this.messageBundleSource = messageBundleSource;
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<SpaceAllocationDTO> insertSpaceAllocations(List<SpaceAllocationDTO> list) {
		try {
			return repository.insertSpaceAllocations(list);
		} catch (Exception e) {
			throw new FuelAndDriverExpensesException(
					messageBundleSource.getmessagebycode("space.allocation.inserting.error", new Object[] {}), e);
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<FetchSpaceAllocationDTO> getSpaceAllocations(Integer raisedBy) {
		try {
			return repository.fetchSpaceAllocations(raisedBy);
		} catch (Exception e) {
			throw new FuelAndDriverExpensesException(
					messageBundleSource.getmessagebycode("space.allocation.fetching.error", new Object[] {}), e);
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public Map<String, Object> updateStatusAndComments(int allocationId, String comments) {
		Map<String, Object> response = new HashMap<>();
		try {
			int rows = repository.updateStatusAndComments(allocationId, comments);
			if (rows > 0) {
				response.put("message", "Space allocation status updated successfully");
			} else {
				response.put("message", "No record found for allocation ID: " + allocationId);
			}
			return response;
		} catch (Exception e) {
			throw new FuelAndDriverExpensesException(
					messageBundleSource.getmessagebycode("space.allocation.update.error", new Object[] {}), e);
		}
	}
}
