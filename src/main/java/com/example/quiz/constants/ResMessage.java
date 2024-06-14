package com.example.quiz.constants;

/**********************************
 *用於hard code(硬代碼/寫死/固定值)      *
 *  enum是無法new的				    *
 * 列舉...放前端->寫死;放後端->可以自由增刪 *
 **********************************/
public enum ResMessage {

//	成功固定就是200
//	如果沒有這串，int dode會是紅蚯蚓
//	***********標準格式***********
	SUCCESS(200, "Success!!"),//
	PARAM_QUIZ_NAME_ERROR(400,"Param quiz name error!!"),//
	PARAM_DESCRIPTION_ERROR(400,"Param Description error!!"),//
	PARAM_STARDATE_ERROR(400,"Param start Date error!!"),//
	PARAM_ENDDATE_ERROR(400,"Param end Date error!!"),//
	PARAM_QUESTION_LIST_NOT_FOUND(404,"Param question list is not found!!"),//
	PARAM_QUESTION_ID_ERROR(400,"Param question id error!!"),//
	PARAM_QUESTION_TITLE_ERROR(400,"Param question title error!!"),//
	PARAM_QUESTION_CONTENT_ERROR(400,"Param question content error!!"),//
	PARAM_QUESTION_TYPE_ERROR(400,"Param question type error!!"),//
	JSON_PROCESSING_EXCEPTION(400,"Json processing exceotion!!"),//
	DELETE_IS_NULL(400,"delete is null!!"),//
	UPDATE_ID_NOT_FOUND(404,"update id not found!!"),//
//	從此行往下開始是用戶填寫表單
	QUIZ_NOT_FOUND(404,"Quiz not found"),//
	PARAM_QUIZ_ID_ERROR(400,"Param quiz id error!!"),//
	PARAM_QUESTION_ID_NOT_FOUND(404,"Param question id not found!!"),//
	PARAM_NAME_IS_REQUIRED(400,"Param name is required!!"),//
	PARAM_PHONE_IS_REQUIRED(400,"Param phone is required!!"),//
	PARAM_EMAIL_IS_REQUIRED(400,"Param email is required!!"),//
	PARAM_AGE_NOT_QUALIFIED(400,"Param age not qualified!!"),//
	FILLIN_IS_REQUIRED(400,"Fillin is required!!"),//
	PARAM_FILLIN_LIST_NOT_FOUND(404,"Param question list is not found!!"),//
	SHOOSE_ONE_ANSER(400,"Choose one anser!!"),//
	SHOOSE_LEAST_ONE_ANSER(400,"Choose least one anser!!"),//
	TEXT_NOT_NULL(400,"Text not null!!"),//
	ANSWER_IS_REQUIRED(400,"Answer is required"),//
	ANSWER_OPTIONS_IS_NOT_MATCH(400,"Answer and options is not match!!!"),//
	IS_NOT_MATCH(400,"is not match!!!"),//
	ANSWER_OPTIONS_TYPE_IS_NOT_MATCH(400,"Answer and options_type is not match!!!"),//
	DUPLICATED_FILLIN(400,"duplicated fillin")
	;

	
//	這裡的code指200、400、401、403、404(請求的權限)
	private int code;

	private String message;

//	這個建構方法主要是為了SUCCESS取消紅蚯蚓
//	private是私有所以在這裡也無其他作用了
	private ResMessage(int code, String message) {
		this.code = code;
		this.message = message;
	}

	
//	enum只會用到get，set在此無用
	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	

}
