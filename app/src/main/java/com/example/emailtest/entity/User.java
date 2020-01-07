package com.example.emailtest.entity;

import java.io.Serializable;

public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4643698637217489886L;
	private int id;
	private String username;
	private String pwd;
	private String ismain;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	public String getIsmain() {
		return ismain;
	}
	public void setIsmain(String ismain) {
		this.ismain = ismain;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", pwd=" + pwd
				+ ", ismain=" + ismain + "]";
	}
	
}
