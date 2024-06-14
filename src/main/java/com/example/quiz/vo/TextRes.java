package com.example.quiz.vo;

import java.util.List;

public class TextRes extends BasicRes {

	private String name;
	
	private List<Text> text;

	public TextRes() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TextRes(int code, String message) {
		super(code, message);
		// TODO Auto-generated constructor stub
	}

	public TextRes(String name, List<Text> text) {
		super();
		this.name = name;
		this.text = text;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Text> getText() {
		return text;
	}

	public void setText(List<Text> text) {
		this.text = text;
	}



}
