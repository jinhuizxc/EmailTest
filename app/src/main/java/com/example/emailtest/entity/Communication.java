package com.example.emailtest.entity;

import java.io.Serializable;

public class Communication implements Serializable{

	private static final long serialVersionUID = -4643698637217489886L;
	private int id;
	private String names;
	private String numbers;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNames() {
		return names;
	}
	public void setNames(String names) {
		this.names = names;
	}
	public String getNumbers() {
		return numbers;
	}
	public void setNumbers(String numbers) {
		this.numbers = numbers;
	}
	@Override
	public String toString() {
		return "Communication [id=" + id + ", names=" + names + ", numbers="
				+ numbers + "]";
	}
	
}
