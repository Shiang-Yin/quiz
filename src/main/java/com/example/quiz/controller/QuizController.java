package com.example.quiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.quiz.service.ifs.QuizService;
import com.example.quiz.vo.BasicRes;
import com.example.quiz.vo.CreateReq;
import com.example.quiz.vo.DeleteReq;
import com.example.quiz.vo.SearchReq;
import com.example.quiz.vo.SearchRes;
import com.example.quiz.vo.updateOrCreateReq;

@CrossOrigin
@RestController
public class QuizController {

	@Autowired
	private QuizService quizService;

	//�Ыطs��T
	@PostMapping(value = "quiz/create")
	public BasicRes create(@RequestBody CreateReq req) {
		return quizService.create(req);
	}

	//��s�P�гy
	@PostMapping(value = "quiz/updateOrCreate")
	public BasicRes updateOrCreate(@RequestBody updateOrCreateReq req) {
		return quizService.updateOrCreate(req);
	}

	//�j��
	@PostMapping(value = "quiz/search")
	public SearchRes search(@RequestBody SearchReq req) {
		return quizService.search(req);
	}

	//�R��
	@PostMapping(value = "quiz/delete")
	public BasicRes delete(@RequestBody DeleteReq req) {
		return quizService.delete(req);
	}

}
