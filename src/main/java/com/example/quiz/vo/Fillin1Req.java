package com.example.quiz.vo;

import java.util.List;
import java.util.Map;

import com.example.quiz.entity.Response;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Fillin1Req {

	@JsonProperty("quiz_id")
	private int quizId;

	private String name;

	private String phone;

	private String email;

	private int age;
	

	@JsonProperty("fillin_list")
	private List<Fillin> fillinList;

	public Fillin1Req() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Fillin1Req(int quizId, String name, String phone, String email, int age, List<Fillin> fillinList) {
		super();
		this.quizId = quizId;
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.age = age;
		this.fillinList = fillinList;
	}

	public int getQuizId() {
		return quizId;
	}

	public void setQuizId(int quizId) {
		this.quizId = quizId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public List<Fillin> getFillinList() {
		return fillinList;
	}

	public void setFillinList(List<Fillin> fillinList) {
		this.fillinList = fillinList;
	}

}
