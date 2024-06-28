package com.example.quiz.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.quiz.service.ifs.FillinServic;
import com.example.quiz.service.ifs.ResponseService;
import com.example.quiz.vo.BasicRes;
import com.example.quiz.vo.FeedbackReq;
import com.example.quiz.vo.FillinReq;
import com.example.quiz.vo.ResponseReq;
import com.example.quiz.vo.ResponseRes;
import com.example.quiz.vo.StatisticsRes;
import com.example.quiz.vo.TextReq;
import com.example.quiz.vo.TextRes;

@CrossOrigin
@RestController
public class ResponseController {

	@Autowired
	private ResponseService responseService;
	
	@Autowired
	private FillinServic fillinService;
	

	@PostMapping(value = "response/response")
	public ResponseRes res(@RequestBody ResponseReq req) {
		return responseService.res(req);
	}
	
//	@PostMapping(value = "response/filin")
//	public BasicRes filin(@RequestBody FillinReq req) {
//		return responseService.fillin(req);
//	}
	
//	@PostMapping(value = "response/text")
//	public TextRes text(@RequestBody TextReq req) {
//		return responseService.text(req);
//	}
	
	@PostMapping(value = "response/fillinText")
	public BasicRes fillin(@RequestBody FillinReq req) {
		return fillinService.fillin(req);
	}
	
	@PostMapping(value = "response/feedback")
	public StatisticsRes statistics(@RequestBody FeedbackReq req) {
		return fillinService.statistics(req);
	}
}
