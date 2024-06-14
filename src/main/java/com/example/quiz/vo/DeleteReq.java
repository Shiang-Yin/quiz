package com.example.quiz.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeleteReq {

	@JsonProperty("id_List")
	private List<Integer> idList;

//	預設建構方法
	public DeleteReq() {
		super();
		// TODO Auto-generated constructor stub
	}

//	有參數的建構方法
	public DeleteReq(List<Integer> idList) {
		super();
		this.idList = idList;
	}

	public List<Integer> getIdList() {
		return idList;
	}

	public void setIdList(List<Integer> idList) {
		this.idList = idList;
	}

}
