package com.example.quiz.vo;

public class Fillin {

	// question_id
	private int qId;

	//���D
	private String title;

	//���D�����
	private String question;

	//����
	private String answer;

	private String type;

	private boolean necessary;

	public Fillin() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Fillin(int qId, String title, String question, String answer, String type, boolean necessary) {
		super();
		this.qId = qId;
		this.title = title;
		this.question = question;
		this.answer = answer;
		this.type = type;
		this.necessary = necessary;
	}

	public int getqId() {
		return qId;
	}

	public void setqId(int qId) {
		this.qId = qId;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isNecessary() {
		return necessary;
	}

	public void setNecessary(boolean necessary) {
		this.necessary = necessary;
	}

}
