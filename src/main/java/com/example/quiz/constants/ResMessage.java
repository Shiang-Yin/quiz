package com.example.quiz.constants;

/**********************************
 *�Ω�hard code(�w�N�X/�g��/�T�w��)      *
 *  enum�O�L�knew��				    *
 * �C�|...��e��->�g��;����->�i�H�ۥѼW�R *
 **********************************/
public enum ResMessage {

//	���\�T�w�N�O200
//	�p�G�S���o��Aint dode�|�O���L�C
//	***********�зǮ榡***********
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
//	�q���橹�U�}�l�O�Τ��g���
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

	
//	�o�̪�code��200�B400�B401�B403�B404(�ШD���v��)
	private int code;

	private String message;

//	�o�ӫغc��k�D�n�O���FSUCCESS�������L�C
//	private�O�p���ҥH�b�o�̤]�L��L�@�ΤF
	private ResMessage(int code, String message) {
		this.code = code;
		this.message = message;
	}

	
//	enum�u�|�Ψ�get�Aset�b���L��
	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	

}
