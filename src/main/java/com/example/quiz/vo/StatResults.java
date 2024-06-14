package com.example.quiz.vo;

public class StatResults {

	private String optionItem;

	private int count;

	public StatResults() {
		super();
		// TODO Auto-generated constructor stub
	}

	public StatResults(String optionItem, int count) {
		super();
		this.optionItem = optionItem;
		this.count = count;
	}

	public String getOptionItem() {
		return optionItem;
	}

	public void setOptionItem(String optionItem) {
		this.optionItem = optionItem;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
