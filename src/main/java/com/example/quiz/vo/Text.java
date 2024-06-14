package com.example.quiz.vo;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Text {

	private String quizName;

	private String description;

	private LocalDate startDate;

	private LocalDate endDate;

	private String fillinName;

	private String phone;

	private String email;

	private int age;

	private int quId;

	private String title;

	private String ans;

	private LocalDateTime fillinDateTime;

	public Text() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Text(String quizName, String description, LocalDate startDate, LocalDate endDate, String fillinName,
			String phone, String email, int age, int quId, String title, String ans, LocalDateTime fillinDateTime) {
		super();
		this.quizName = quizName;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.fillinName = fillinName;
		this.phone = phone;
		this.email = email;
		this.age = age;
		this.quId = quId;
		this.title = title;
		this.ans = ans;
		this.fillinDateTime = fillinDateTime;
	}

	public String getQuizName() {
		return quizName;
	}

	public void setQuizName(String quizName) {
		this.quizName = quizName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public String getFillinName() {
		return fillinName;
	}

	public void setFillinName(String fillinName) {
		this.fillinName = fillinName;
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

	public LocalDateTime getFillinDateTime() {
		return fillinDateTime;
	}

	public void setFillinDateTime(LocalDateTime fillinDateTime) {
		this.fillinDateTime = fillinDateTime;
	}

	public int getQuId() {
		return quId;
	}

	public void setQuId(int quId) {
		this.quId = quId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAns() {
		return ans;
	}

	public void setAns(String ans) {
		this.ans = ans;
	}

}
