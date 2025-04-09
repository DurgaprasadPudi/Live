package com.hetero.heteroiconnect.leavemanagement;

public class Leave {
	
	 private String leave_type;
	 private String from_date;
	 private String to_date;
	 private String halfday;
	 private String hal_date;
	 private String compoff;
	 private String comm_date;
	 private String to_mail;
	 private String cc_mail;
	 private String subject;
	 private String reason;
	 private String hr_att;
	 private String hr_att_user;
	 private String from_loc;
	 private String to_loc;
	 private String othflag;
	 private String buid;
	 private String empID;
	 private double maxleave;
	 private String  empEmail;
	 
	  
	 
	public String getEmpEmail() {
		return empEmail;
	}
	public void setEmpEmail(String empEmail) {
		this.empEmail = empEmail;
	}
	public double getMaxleave() {
		return maxleave;
	}
	public void setMaxleave(double maxleave) {
		this.maxleave = maxleave;
	}
	public String getEmpID() {
		return empID;
	}
	public void setEmpID(String empID) {
		this.empID = empID;
	}
	public String getBuid() {
		return buid;
	}
	public void setBuid(String buid) {
		this.buid = buid;
	}
	public String getLeave_type() {
		return leave_type;
	}
	public void setLeave_type(String leave_type) {
		this.leave_type = leave_type;
	}
	public String getFrom_date() {
		return from_date;
	}
	 
	@Override
	public String toString() {
		return "Leave [leave_type=" + leave_type + ", from_date=" + from_date + ", to_date=" + to_date + ", halfday="
				+ halfday + ", hal_date=" + hal_date + ", compoff=" + compoff + ", comm_date=" + comm_date
				+ ", to_mail=" + to_mail + ", cc_mail=" + cc_mail + ", subject=" + subject + ", reason=" + reason
				+ ", hr_att=" + hr_att + ", hr_att_user=" + hr_att_user + ", from_loc=" + from_loc + ", to_loc="
				+ to_loc + ", othflag=" + othflag + ", buid=" + buid + ", empID=" + empID + ", maxleave=" + maxleave
				+ ", empEmail=" + empEmail + "]";
	}
	public void setFrom_date(String from_date) {
		this.from_date = from_date;
	}
	public String getTo_date() {
		return to_date;
	}
	public void setTo_date(String to_date) {
		this.to_date = to_date;
	}
	public String getHalfday() {
		return halfday;
	}
	public void setHalfday(String halfday) {
		this.halfday = halfday;
	}
	public String getHal_date() {
		return hal_date;
	}
	public void setHal_date(String hal_date) {
		this.hal_date = hal_date;
	}
	public String getCompoff() {
		return compoff;
	}
	public void setCompoff(String compoff) {
		this.compoff = compoff;
	}
	public String getComm_date() {
		return comm_date;
	}
	public void setComm_date(String comm_date) {
		this.comm_date = comm_date;
	}
	public String getTo_mail() {
		return to_mail;
	}
	public void setTo_mail(String to_mail) {
		this.to_mail = to_mail;
	}
	public String getCc_mail() {
		return cc_mail;
	}
	public void setCc_mail(String cc_mail) {
		this.cc_mail = cc_mail;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getHr_att() {
		return hr_att;
	}
	public void setHr_att(String hr_att) {
		this.hr_att = hr_att;
	}
	public String getHr_att_user() {
		return hr_att_user;
	}
	public void setHr_att_user(String hr_att_user) {
		this.hr_att_user = hr_att_user;
	}
	public String getFrom_loc() {
		return from_loc;
	}
	public void setFrom_loc(String from_loc) {
		this.from_loc = from_loc;
	}
	public String getTo_loc() {
		return to_loc;
	}
	public void setTo_loc(String to_loc) {
		this.to_loc = to_loc;
	}
	public String getOthflag() {
		return othflag;
	}
	public void setOthflag(String othflag) {
		this.othflag = othflag;
	}
	 
	 	

}
