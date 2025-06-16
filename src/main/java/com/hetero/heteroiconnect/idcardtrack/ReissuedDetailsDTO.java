package com.hetero.heteroiconnect.idcardtrack;
public class ReissuedDetailsDTO {
 
	private Integer id;
	private String reissuedDate;
	private String comment;
	private String reissuedBy;
	private String reissuedByName;
 
	public ReissuedDetailsDTO() {
		super();
	}
 
	public ReissuedDetailsDTO(Integer id, String reissuedDate, String comment, String reissuedBy,
			String reissuedByName) {
		this.id = id;
		this.reissuedDate = reissuedDate;
		this.comment = comment;
		this.reissuedBy = reissuedBy;
		this.reissuedByName = reissuedByName;
	}
 
	public Integer getId() {
		return id;
	}
 
	public void setId(Integer id) {
		this.id = id;
	}
 
	public String getReissuedDate() {
		return reissuedDate;
	}
 
	public void setReissuedDate(String reissuedDate) {
		this.reissuedDate = reissuedDate;
	}
 
	public String getComment() {
		return comment;
	}
 
	public void setComment(String comment) {
		this.comment = comment;
	}
 
	public String getReissuedBy() {
		return reissuedBy;
	}
 
	public void setReissuedBy(String reissuedBy) {
		this.reissuedBy = reissuedBy;
	}
 
	public String getReissuedByName() {
		return reissuedByName;
	}
 
	public void setReissuedByName(String reissuedByName) {
		this.reissuedByName = reissuedByName;
	}
 
}
 