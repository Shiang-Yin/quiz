package com.example.quiz.vo;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class StatisticsRes extends BasicRes {

	// 第幾問卷
	private int qId;

	// 標題
	private String name;

	private LocalDate starDate;

	private LocalDate endDate;

//	private List<Statistics> statistics;

	// 統計<獲得 問題的id 問題的選項 問題的統計>
	private Map<Integer, Map<String, Integer>> ansStatistics;

	// 當題目為文字時
//	private Map<Integer, Map<String, String>> textStatistics;
	private List<FeedbackText> feedbackText;

	public StatisticsRes() {
		super();
		// TODO Auto-generated constructor stub
	}

	public StatisticsRes(int code, String message) {
		super(code, message);
		// TODO Auto-generated constructor stub
	}

	public StatisticsRes(int code, String message, int qId, String name, LocalDate starDate, LocalDate endDate,
			Map<Integer, Map<String, Integer>> ansStatistics, List<FeedbackText> feedbackText) {
		super(code, message);
		this.qId = qId;
		this.name = name;
		this.starDate = starDate;
		this.endDate = endDate;
		this.ansStatistics = ansStatistics;
		this.feedbackText = feedbackText;
	}

//	public StatisticsRes(int code, String message, int qId, String name, LocalDate starDate, LocalDate endDate,
//			Map<Integer, Map<String, Integer>> ansStatistics, Map<Integer, Map<String, String>> textStatistics) {
//		super(code, message);
//		this.qId = qId;
//		this.name = name;
//		this.starDate = starDate;
//		this.endDate = endDate;
//		this.ansStatistics = ansStatistics;
//		this.textStatistics = textStatistics;
//	}

	public StatisticsRes(int code, String message, int qId, String name, LocalDate starDate, LocalDate endDate,
			Map<Integer, Map<String, Integer>> ansStatistics) {
		super(code, message);
		this.qId = qId;
		this.name = name;
		this.starDate = starDate;
		this.endDate = endDate;
		this.ansStatistics = ansStatistics;
	}

	public int getqId() {
		return qId;
	}

	public void setqId(int qId) {
		this.qId = qId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getStarDate() {
		return starDate;
	}

	public void setStarDate(LocalDate starDate) {
		this.starDate = starDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public Map<Integer, Map<String, Integer>> getAnsStatistics() {
		return ansStatistics;
	}

	public void setAnsStatistics(Map<Integer, Map<String, Integer>> ansStatistics) {
		this.ansStatistics = ansStatistics;
	}

	public List<FeedbackText> getFeedbackText() {
		return feedbackText;
	}

	public void setFeedbackText(List<FeedbackText> feedbackText) {
		this.feedbackText = feedbackText;
	}

//	public Map<Integer, Map<String, String>> getTextStatistics() {
//		return textStatistics;
//	}
//
//	public void setTextStatistics(Map<Integer, Map<String, String>> textStatistics) {
//		this.textStatistics = textStatistics;
//	}

}
