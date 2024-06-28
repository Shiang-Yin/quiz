package com.example.quiz.vo;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class updateOrCreateReq {

	private int id;

	@NotBlank(message = "Param quiz name error!!")
	private String name;

	@NotBlank(message = "Param Description error!!")
	private String description;

	@NotNull(message = "Param start Date error!!")
	//FutureOrPresent:未來時間(大於等於現在時間)  ，所以當小於等於現在時間的話就會返回錯誤
	@FutureOrPresent(message = "Param start Date error!!")
	@JsonProperty("start_date")
	private LocalDate startDate;

	@NotNull(message = "Param end Date error!!")
	@FutureOrPresent(message = "Param end Date error!!")
	@JsonProperty("end_date")
	private LocalDate endDate;

	//@Valid :這樣才能在從這一層進入我們下一層的校驗
	@Valid
	@NotEmpty(message = "Param question list is not found!!")
//	問卷不只一個問題，所以可以給他組成一個物件，並用list包起來
	@JsonProperty("question_list")
	private List<Question> questionList;

	@JsonProperty("is_published")
	private boolean published;// 是否要儲存(僅儲存/儲存並發布)

	public updateOrCreateReq() {
		super();
		// TODO Auto-generated constructor stub
	}

	public updateOrCreateReq(int id, String name, String description, LocalDate startDate, LocalDate endDate,
			List<Question> questionList, boolean published) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.questionList = questionList;
		this.published = published;
	}
	
	public updateOrCreateReq( String name, String description, LocalDate startDate, LocalDate endDate,
			List<Question> questionList, boolean published) {
		super();
		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.questionList = questionList;
		this.published = published;
	}

	public int getId() {
		return id;
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

	public List<Question> getQuestionList() {
		return questionList;
	}

	public boolean isPublished() {
		return published;
	}

}
