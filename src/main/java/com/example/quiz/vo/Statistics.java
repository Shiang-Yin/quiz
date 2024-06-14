package com.example.quiz.vo;

import java.util.Map;

// 一張問卷中的"一題"
public class Statistics {

	// 題號
	private int qId;

	// 題目
	private String topic;

	// 選項
	private String options;


	public Statistics() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Statistics(int qId, String topic, String options, Map<Integer, Map<String, Integer>> ansStatistics) {
		super();
		this.qId = qId;
		this.topic = topic;
		this.options = options;
//		this.ansStatistics = ansStatistics;
	}

	public int getqId() {
		return qId;
	}

	public void setqId(int qId) {
		this.qId = qId;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

//	public Map<Integer, Map<String, Integer>> getAnsStatistics() {
//		return ansStatistics;
//	}
//
//	public void setAnsStatistics(Map<Integer, Map<String, Integer>> ansStatistics) {
//		this.ansStatistics = ansStatistics;
//	}

}
