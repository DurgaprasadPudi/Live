package com.hetero.heteroiconnect.stationaryandhousekeepingtrack;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class AssignList {
	@NotNull(message = "Inventory ID must not be null")
	@Min(value = 1, message = "Inventory ID must be a positive number")
	private int inventoryId;
	@NotNull(message = "Assign count must not be null")
	@Min(value = 1, message = "Assign count must be at least 1")
	private int assignCount;

	public int getInventoryId() {
		return inventoryId;
	}

	public void setInventoryId(int inventoryId) {
		this.inventoryId = inventoryId;
	}

	public int getAssignCount() {
		return assignCount;
	}

	public void setAssignCount(int assignCount) {
		this.assignCount = assignCount;
	}
}
