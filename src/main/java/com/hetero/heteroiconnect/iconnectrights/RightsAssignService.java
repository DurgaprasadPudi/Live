package com.hetero.heteroiconnect.iconnectrights;

import java.util.List;

public interface RightsAssignService {


	Object getRights();
	
	Object getReqIPBotRights();

	Object getAssignedDataEmployeeid(int employeeid);

	Object addRightAssign(List<RightsAssignDTO> rightsAssignDTOs);

}
