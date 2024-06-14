package com.example.quiz.vo;

public class TextQuestionAnswer {

	private int id;
	
	private String title;
	
	private String answer;

	public TextQuestionAnswer() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TextQuestionAnswer(int id, String title, String answer) {
		super();
		this.id = id;
		this.title = title;
		this.answer = answer;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

}
