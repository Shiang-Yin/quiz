package com.example.quiz.vo;

import java.util.List;

import com.example.quiz.entity.Response;

public class ResponseRes extends BasicRes {

	private List<Response> responseList;

	public ResponseRes() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ResponseRes(int code, String message) {
		super(code, message);
		// TODO Auto-generated constructor stub
	}

	public ResponseRes(int code, String message,List<Response> responseList) {
		super(code, message);
		this.responseList = responseList;
	}

	public ResponseRes(List<Response> responseList) {
		super();
		this.responseList = responseList;
	}

	public List<Response> getResponseList() {
		return responseList;
	}

	public void setResponseList(List<Response> responseList) {
		this.responseList = responseList;
	}

}
