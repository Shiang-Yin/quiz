package com.example.quiz.vo;

public class FeedbackReq {

//	問卷id
	private int quizId;

	public FeedbackReq() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FeedbackReq(int quizId) {
		super();
		this.quizId = quizId;
	}

	public int getQuizId() {
		return quizId;
	}

}
