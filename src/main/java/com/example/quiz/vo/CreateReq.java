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

//	�ݨ����u�@�Ӱ��D�A�ҥH�i�H���L�զ��@�Ӫ���A�å�list�]�_��
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
//	booleanu �@�w�O�p�gb�A�w�]�Ȭ�flase(�p�G�O�j�gB�L���O���L���A�ӬOclass)

	@JsonProperty("is_published")
	private boolean published;// �O�_�n�x�s(���x�s/�x�s�õo��)

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
