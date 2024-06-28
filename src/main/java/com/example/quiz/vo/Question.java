package com.example.quiz.vo;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Question {

	//@Min(value=0): 表示id的是數值要大於等於0
	@Min(value = 1,message = "Param question id error!!")
	@NotNull(message = "Param question id error!!")
	private int id;

	@NotBlank(message ="Param question title error!!")
	private String title;

	
	private String options;

	@NotBlank(message = "Param question type error!!")
	private String type;

	@JsonProperty("is_necessary")
	private boolean necessary;

	public Question() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Question(int id, String title, String options, String type, boolean necessary) {
		super();
		this.id = id;
		this.title = title;
		this.options = options;
		this.type = type;
		this.necessary = necessary;
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

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

}
