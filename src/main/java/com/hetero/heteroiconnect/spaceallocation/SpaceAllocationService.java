package com.hetero.heteroiconnect.spaceallocation;

import java.util.List;
import java.util.Map;

public interface SpaceAllocationService {
	List<SpaceAllocationDTO> insertSpaceAllocations(List<SpaceAllocationDTO> list);

	List<FetchSpaceAllocationDTO> getSpaceAllocations(Integer raisedBy);

	Map<String, Object> updateStatusAndComments(int allocationId, String comments);

}
