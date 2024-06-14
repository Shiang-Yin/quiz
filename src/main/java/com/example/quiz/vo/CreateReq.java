package com.example.quiz.vo;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateReq {

	private String name;

	private String description;

	@JsonProperty("start_date")
	private LocalDate startDate;

	@JsonProperty("end_date")
	private LocalDate endDate;

//	問卷不只一個問題，所以可以給他組成一個物件，並用list包起來
	@JsonProperty("question_list")
	private List<Question> questionList;
//	@JsonProperty("question_id")
//	private int questionId;
//
//	private String content;
//
//	private String type;
//
//	@JsonProperty("is_necessary")
//	private boolean necessary;
//	booleanu 一定是小寫b，預設值為flase(如果是大寫B他不是布林直，而是class)

	@JsonProperty("is_published")
	private boolean published;// 是否要儲存(僅儲存/儲存並發布)

	public CreateReq() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CreateReq(String name, String description, LocalDate startDate, LocalDate endDate,
			List<Question> questionList, boolean published) {
		super();
		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.questionList = questionList;
		this.published = published;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

//	public int getQuestionId() {
//		return questionId;
//	}
//
//	public String getContent() {
//		return content;
//	}
//
//	public String getType() {
//		return type;
//	}
//
//	public boolean isNecessary() {
//		return necessary;
//	}

	public List<Question> getQuestionList() {
		return questionList;
	}

	public boolean isPublished() {
		return published;
	}

}
