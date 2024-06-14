package com.example.quiz.vo;

import java.util.Map;

public class FeedbackText {

	private Map<Integer, String> textStatistics;

	private String textStat;

	public FeedbackText() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FeedbackText(Map<Integer, String> textStatistics, String textStat) {
		super();
		this.textStatistics = textStatistics;
		this.textStat = textStat;
	}

	public Map<Integer, String> getTextStatistics() {
		return textStatistics;
	}

	public void setTextStatistics(Map<Integer, String> textStatistics) {
		this.textStatistics = textStatistics;
	}

	public String getTextStat() {
		return textStat;
	}

	public void setTextStat(String textStat) {
		this.textStat = textStat;
	}

}
