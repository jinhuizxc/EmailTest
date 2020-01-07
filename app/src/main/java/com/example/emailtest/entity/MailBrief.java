package com.example.emailtest.entity;

public class MailBrief {
	private String id;
	private String fromPersonal;
	private String subject;
	private String mailContent;
	private String malieId;
	private String flieMath;
	private String sendTime;
	private String mailType;
	private String UserId;
	private String Mindex;
	
	private String TOO;
	private String CC;
	private String BCC;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFromPersonal() {
		return fromPersonal;
	}
	public void setFromPersonal(String fromPersonal) {
		this.fromPersonal = fromPersonal;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMailContent() {
		return mailContent;
	}
	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}
	public String getMalieId() {
		return malieId;
	}
	public void setMalieId(String malieId) {
		this.malieId = malieId;
	}
	public String getFlieMath() {
		return flieMath;
	}
	public void setFlieMath(String flieMath) {
		this.flieMath = flieMath;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public String getMailType() {
		return mailType;
	}
	public void setMailType(String mailType) {
		this.mailType = mailType;
	}
	public String getUserId() {
		return UserId;
	}
	public void setUserId(String userId) {
		UserId = userId;
	}
	public String getMindex() {
		return Mindex;
	}
	public void setMindex(String mindex) {
		Mindex = mindex;
	}
	public String getTOO() {
		return TOO;
	}
	public void setTOO(String tOO) {
		TOO = tOO;
	}
	public String getCC() {
		return CC;
	}
	public void setCC(String cC) {
		CC = cC;
	}
	public String getBCC() {
		return BCC;
	}
	public void setBCC(String bCC) {
		BCC = bCC;
	}
	@Override
	public String toString() {
		return "MailBrief [id=" + id + ", fromPersonal=" + fromPersonal
				+ ", subject=" + subject + ", mailContent=" + mailContent
				+ ", malieId=" + malieId + ", flieMath=" + flieMath
				+ ", sendTime=" + sendTime + ", mailType=" + mailType
				+ ", UserId=" + UserId + ", Mindex=" + Mindex + ", TOO=" + TOO
				+ ", CC=" + CC + ", BCC=" + BCC + "]";
	}
	
}
