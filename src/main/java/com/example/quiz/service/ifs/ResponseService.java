package com.example.quiz.service.ifs;

import com.example.quiz.vo.BasicRes;
import com.example.quiz.vo.FeedbackReq;
import com.example.quiz.vo.FeedbackRes;
import com.example.quiz.vo.FillinReq;
import com.example.quiz.vo.TextReq;
import com.example.quiz.vo.TextRes;

public interface ResponseService {

	public BasicRes fillin(FillinReq req);
	
	public FeedbackRes feedback(FeedbackReq req);
	
	public TextRes text(TextReq req);
	
}
