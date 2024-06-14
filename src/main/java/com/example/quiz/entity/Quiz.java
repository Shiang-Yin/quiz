package com.example.quiz.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "quiz")
public class Quiz {

	/**
	 * 當屬性是Integer的時候，必加@GeneratedValue 但如果屬性是int時，可加可不加，但...
	 * 如果沒加，當我在test中save時，他不會回傳到這裡(但sql資料依樣會變更) 加了，才會回傳 所以通常都會加上@GeneratedValue
	 */

//	只要sql中有使用到AI(Auto Incrememtal自動生成編碼(id雖然可以自動生成，但我們也可以自己自訂義))，
//	就得要@GeneratedValue這段
//	AI只能使用在具有pk的身上，且一個表單只能有一個勾選AI，且類型一定要是int(數字)
//	strategy:指AI的生成策略
//	GenerationType.IDENTITY:代表PK數字由資料來自動增加
//	什麼時候要用到AI?  當我得"id無意義"的時候，單純只做流水號的時候
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "id")
	private int id;

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

//	資料庫裡面的date類型使用指有下面三種
//	(1)  日期   (2)  時間    (3)  日期加時間
//	(1)ocalDate(2)LocalTime(3)LocalDateTime
	@Column(name = "start_date")
	private LocalDate startDate;

	@Column(name = "end_date")
	private LocalDate endDate;

	@Column(name = "questions")
	private String questions;

//	為何不使用isPublished? 因為get出來後 原本因該是要isIsPublished，但是她出來後卻只有isPublished
//	boolean生成get後前面會加is
	@Column(name = "published")
	private boolean published;

	public Quiz() {
		super();
	}

//	建構方法可以"不需要"點選id因為這裡的id是跟著我們的name生成流水號
	public Quiz(String name, String description, LocalDate startDate, LocalDate endDate, String questions,
			boolean published) {
		super();
		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.questions = questions;
		this.published = published;
	}

//	為了避免id可能會在使用到，所以在新增一個具有所有屬性的建構方法
	public Quiz(int id, String name, String description, LocalDate startDate, LocalDate endDate, String questions,
			boolean published) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.questions = questions;
		this.published = published;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public void setStartDate(LocalDate starDate) {
		this.startDate = starDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public String getQuestions() {
		return questions;
	}

	public void setQuestions(String questions) {
		this.questions = questions;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

}
