package com.example.quiz.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeleteReq {

	@JsonProperty("id_List")
	private List<Integer> idList;

//	�w�]�غc��k
	public DeleteReq() {
		super();
		// TODO Auto-generated constructor stub
	}

//	���Ѽƪ��غc��k
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
