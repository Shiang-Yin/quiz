package com.example.quiz.service.ifs;

import com.example.quiz.vo.BasicRes;
import com.example.quiz.vo.CreateReq;
import com.example.quiz.vo.DeleteReq;
import com.example.quiz.vo.SearchReq;
import com.example.quiz.vo.SearchRes;
import com.example.quiz.vo.updateOrCreateReq;

public interface QuizService {

//	創建資料
	public BasicRes create(CreateReq req);
//	新增資料更新資料
	public BasicRes updateOrCreate(updateOrCreateReq req);
	
//	搜索資料
	public SearchRes search(SearchReq req);

//	刪除資料   只需要判斷PK即可
	public BasicRes delete(DeleteReq req);
	
//	新增資料更新資料
//	public void update(UpdateReq req);
}
