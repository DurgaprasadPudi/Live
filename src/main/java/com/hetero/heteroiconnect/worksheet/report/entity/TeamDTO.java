package com.hetero.heteroiconnect.worksheet.report.entity;

public class TeamDTO {
	private int teamId;
	private String teamName;


	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public TeamDTO(int teamId, String teamName) {
		super();
		this.teamId = teamId;
		this.teamName = teamName;
	}

	public TeamDTO(Integer integer, String teamName2) {
		// TODO Auto-generated constructor stub
	}

}
