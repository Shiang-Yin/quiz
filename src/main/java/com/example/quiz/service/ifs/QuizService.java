package com.example.quiz.service.ifs;

import com.example.quiz.vo.BasicRes;
import com.example.quiz.vo.CreateReq;
import com.example.quiz.vo.DeleteReq;
import com.example.quiz.vo.SearchReq;
import com.example.quiz.vo.SearchRes;
import com.example.quiz.vo.updateOrCreateReq;

public interface QuizService {

//	�Ыظ��
	public BasicRes create(CreateReq req);
//	�s�W��Ƨ�s���
	public BasicRes updateOrCreate(updateOrCreateReq req);
	
//	�j�����
	public SearchRes search(SearchReq req);

//	�R�����   �u�ݭn�P�_PK�Y�i
	public BasicRes delete(DeleteReq req);
	
//	�s�W��Ƨ�s���
//	public void update(UpdateReq req);
}
