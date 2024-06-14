package com.example.quiz.vo;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FillinReq {

//	問卷號碼
	@JsonProperty("quiz_id")
	private int quizId;

	private String name;

	private String phone;

	private String email;

	private int age;

	// qu_id and answer map : answer有多個食用分號(;)串接 (2024-06-05更動，不在使用，暫留)
	private Map<Integer, String> qIdAnswerMap;

	private List<Fillin> fillin;

	public FillinReq() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FillinReq(int quizId, String name, String phone, String email, int age, Map<Integer, String> qIdAnswerMap) {
		super();
		this.quizId = quizId;
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.age = age;
		this.qIdAnswerMap = qIdAnswerMap;
	}

	public FillinReq(int quizId, String name, String phone, String email, int age, List<Fillin> fillin) {
		super();
		this.quizId = quizId;
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.age = age;
		this.fillin = fillin;
	}

	public FillinReq(int quizId, String name, String phone, String email, int age, Map<Integer, String> qIdAnswerMap,
			List<Fillin> fillin) {
		super();
		this.quizId = quizId;
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.age = age;
		this.qIdAnswerMap = qIdAnswerMap;
		this.fillin = fillin;
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

	public Map<Integer, String> getqIdAnswerMap() {
		return qIdAnswerMap;
	}

	public void setqIdAnswerMap(Map<Integer, String> qIdAnswerMap) {
		this.qIdAnswerMap = qIdAnswerMap;
	}

	public List<Fillin> getFillin() {
		return fillin;
	}

	public void setFillin(List<Fillin> fillin) {
		this.fillin = fillin;
	}

}
