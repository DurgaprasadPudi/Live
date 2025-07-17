package com.hetero.heteroiconnect.touradvance;

import java.util.List;

public class EmployeeTourDTO {
	private EmployeesTourDetailsDTO EmployeesTourDetailsDTO;
	private List<EmployeeTourTeamMemberDTO> employeeTourTeamMemberDTO;
	

	public EmployeesTourDetailsDTO getEmployeesTourDetailsDTO() {
		return EmployeesTourDetailsDTO;
	}

	public void setEmployeesTourDetailsDTO(EmployeesTourDetailsDTO employeesTourDetailsDTO) {
		EmployeesTourDetailsDTO = employeesTourDetailsDTO;
	}

	public List<EmployeeTourTeamMemberDTO> getEmployeeTourTeamMemberDTO() {
		return employeeTourTeamMemberDTO;
	}

	public void setEmployeeTourTeamMemberDTO(List<EmployeeTourTeamMemberDTO> employeeTourTeamMemberDTO) {
		this.employeeTourTeamMemberDTO = employeeTourTeamMemberDTO;
	}


	
}
