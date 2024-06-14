package com.example.quiz.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.persistence.Column;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.example.quiz.constants.OptionType;
import com.example.quiz.constants.ResMessage;
import com.example.quiz.entity.Quiz;
import com.example.quiz.repository.QuizDao;
import com.example.quiz.service.ifs.QuizService;
import com.example.quiz.vo.BasicRes;
import com.example.quiz.vo.CreateReq;
import com.example.quiz.vo.DeleteReq;
import com.example.quiz.vo.Question;
import com.example.quiz.vo.SearchReq;
import com.example.quiz.vo.SearchRes;
import com.example.quiz.vo.updateOrCreateReq;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class QuizServiceImpl implements QuizService{
	
	@Autowired
	private QuizDao quizDao;

	@Override
	public BasicRes create(CreateReq req) {
//		�ˬd�Ѽ�
		BasicRes checkResult = checkParams(req);
//		��k�@
//		checkResult==null�ɡA��ܰѼ��ˬd���T
		if(checkResult!=null) {
			return checkResult;
		}
//		��k�G
		/*if(checkResult.getCode() != 200) {
			return checkResult;
		}*/
//	=======================================================================	
//		�NCreateReq����private List<Question> questionList;�ഫ���r��(String)
//		�]���bQuiz��  @Column(name = "questions")private String questions;�O�r��(String)
//		�z�L [objectMapper]�i�H�⪫��(���O)�୼json�榡�r��
		ObjectMapper mapper=new ObjectMapper();
//		�@�}�l���o��A�|�����L�C�A�ҥH�ݭntry/catch�A�I�@�U�N�i�H�۰����ڭ��ഫ�F
//		String questionStr = mapper.writeValueAsString(req.getQuestionList());
		try {
//			�X��1�B �N questionStr�a�Jquiz����questionStr�A�p54��
//			String questionStr = mapper.writeValueAsString(req.getQuestionList());
//			Quiz quiz=new Quiz(req.getName(),req.getDescription(),req.getStartDate(),
//					req.getEndDate(),questionStr,req.isPublished());
			Quiz quiz=new Quiz(req.getName(),req.getDescription(),req.getStartDate(),
					req.getEndDate(),mapper.writeValueAsString(req.getQuestionList()),
					req.isPublished());
//			�X��2�B save����quiz����quiz���@�j����N�A�p58��
/*			quizDao.save(new Quiz(req.getName(),req.getDescription(),req.getStartDate(),
					req.getEndDate(),mapper.writeValueAsString(req.getQuestionList()),
					req.isPublished()));
 * 			
 */
			quizDao.save(quiz);
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			return new BasicRes(ResMessage.JSON_PROCESSING_EXCEPTION.getCode(),
					ResMessage.JSON_PROCESSING_EXCEPTION.getMessage());
		}
		
//		return null;
		return new BasicRes(ResMessage.SUCCESS.getCode(),
				ResMessage.SUCCESS.getMessage());
	}
	
	private BasicRes checkParams(CreateReq req) {
//		����  ���ˬd�ݨ��Ѽ�
//		�]���OString�A�ҥH�ϥ�StringUtils.hasText(�r��)
//		StringUtils.hasText(�r��):�|�ˬd�r��O�_��null�A�Ŧr��A���ťզr��A�Y�O�ŦX3�ب䤤�@�ءA�|��^false
//		�e���[�@�� [ ! ] :��ܤϦV�N��A�Y�r���ˬd���G�Ofalse�ɡA�N�|�i�Jif����@�϶�
		if(!StringUtils.hasText(req.getName())) {
			return new BasicRes(ResMessage.PARAM_QUIZ_NAME_ERROR.getCode(),
					ResMessage.PARAM_QUIZ_NAME_ERROR.getMessage());
		}
		if(!StringUtils.hasText(req.getDescription())) {
			return new BasicRes(ResMessage.PARAM_DESCRIPTION_ERROR.getCode(),
					ResMessage.PARAM_DESCRIPTION_ERROR.getMessage());
		}
//		�}�l�ɶ�����b����(�t)���e
//		(req�����}�l�ɶ����ভ��[isBefore]����(�t[isEqual])���e)�]�i������isAfter(���n�`�N���ᶶ��)
//		localDate.new()  �O���o�t�η�e�ɶ�
// 		LocalDate.now(): ���o�t�η�e�ɶ�
// 		req.getStartDate().isBefore(LocalDate.now()): �Y req �����}�l�ɶ�"��"���e�ɶ��A�|�o�� true
// 		req.getStartDate().isEqual(LocalDate.now()): �Y req �����}�l�ɶ�"��"���e�ɶ��A�|�o�� true
// 		�]���}�l�ɶ�����b����(�t)���e�A�ҥH�W��Ӥ���Y�O���@�ӵ��G�� true�A�h��ܶ}�l�ɶ��n���e(�t)�ɶ���
//		req.getStartDate().isAfter(LocalDate.now()) :�Yreq ���}�l�ɶ����e�ɶ��ߡA�|�o��true
//		!req.getStartDate().isAfter(LocalDate.now()):�b�e���[�W[ ! ]�A��ܡA�p�G��e�ɶ����e�ɶ���
//		�A�h�|�o��fale�A�Ө�L�ɬq����true(�p���e�ɶ���)
		if(req.getStartDate()==null || !req.getStartDate().isAfter(LocalDate.now()) ) {
			return new BasicRes(ResMessage.PARAM_STARDATE_ERROR.getCode(),
					ResMessage.PARAM_STARDATE_ERROR.getMessage());
		}
//		��{���X�����EndDate�ɡA���StartDate�@�w�O�j���e�ɶ�(�S�������~)
//		�ҥH�����ɶ��u�ݭn�A�P�_�A�b�}�l�ɶ�����N�n(�j�󵥩�}�l�ɶ�)
//		�}�l�ɶ� > ��e�ɶ� ; �����ɶ� >= �}�l�ɶ�  ==>  �����ɶ� >= �}�l�ɶ� > ��e�ɶ�
// 		1�B�����ɶ�����p�󵥩��e�ɶ�  !req.getEndDate().isAfter(LocalDate.now()) �o�ӥi�H�ٲ����g
//		2�B�����ɶ�����p(��)��}�l�ɶ�		
		if(req.getEndDate()==null || req.getEndDate().isBefore(req.getStartDate())) {
			return new BasicRes(ResMessage.PARAM_ENDDATE_ERROR.getCode(),
					ResMessage.PARAM_ENDDATE_ERROR.getMessage());
		}
//		�ˬd���D���Ѽ�
		if(CollectionUtils.isEmpty(req.getQuestionList())) {
			return new BasicRes(ResMessage.PARAM_QUESTION_LIST_NOT_FOUND.getCode(),
					ResMessage.PARAM_QUESTION_LIST_NOT_FOUND.getMessage());
		}
//		�@�i�ݨ��i��|���h�Ӱ��D�A�ҥH�n�v���ˬd�C�@�D���Ѽ�(�ҥH��for)
		for(Question item:req.getQuestionList()) {
			if(item.getId()<=0) {
				return new BasicRes(ResMessage.PARAM_QUESTION_ID_ERROR.getCode(),
						ResMessage.PARAM_QUESTION_ID_ERROR.getMessage());
			}
			if(!StringUtils.hasText(item.getTitle())) {
				return new BasicRes(ResMessage.PARAM_QUESTION_TITLE_ERROR.getCode(),
						ResMessage.PARAM_QUESTION_TITLE_ERROR.getMessage());
			}
//			if(!StringUtils.hasText(item.getOptions())) {
//				return new BasicRes(ResMessage.PARAM_QUESTION_CONTENT_ERROR.getCode(),
//						ResMessage.PARAM_QUESTION_CONTENT_ERROR.getMessage());
//			}
			if( !StringUtils.hasText(item.getType())) {
				return new BasicRes(ResMessage.PARAM_QUESTION_TYPE_ERROR.getCode(),
						ResMessage.PARAM_QUESTION_TYPE_ERROR.getMessage());
			}
//			type�w�g�������ˬd�F�A���ڭ̷|�]���������P��option�]�|���Ҥ��P
//			��Option_type �O���Φh��ɡAoption�N����O�Ŧr��(�H�U�ˬd��/�h�����U�A�p���Ŧr���^���~)
//			����option_type�O��r�ɡA�h���\�Ŧr��
			if(item.getType().equals(OptionType.SINGLE_CHOICE.getType())
					|| item.getType().equals(OptionType.MULIT_CHOICE.getType())) {
				if(!StringUtils.hasText(item.getOptions())) {
					return new BasicRes(ResMessage.PARAM_QUESTION_CONTENT_ERROR.getCode(),
							ResMessage.PARAM_QUESTION_CONTENT_ERROR.getMessage());
				}
			}
//			�]�i�o�˼g(���h�諸�����ˬd)  ����@���߮ɡA����G�]����(�ҥH�n�`�N����@��()�H�α���G��&&)
//		  ********   ( ����@(1) || ����@(2) ) &&����G   **************
/*		if( ( item.getType().equals(OptionType.SINGLE_CHOICE.getType())
					|| item.getType().equals(OptionType.MULIT_CHOICE.getType()) )
					&& !StringUtils.hasText(item.getOptions()) ) {
		
					return new BasicRes(ResMessage.PARAM_QUESTION_CONTENT_ERROR.getCode(),
							ResMessage.PARAM_QUESTION_CONTENT_ERROR.getMessage());
							
			}
 * 			
 */
			
		}
//		��k�@
		return null;
//		��k�G
		/*return new BasicRes(ResMessage.SUCCESS.getCode(),
				ResMessage.SUCCESS.getMessage());*/
	}

//	=================================================================
// *****************�i***********�J***********�j*********��******************
	@Override
	public SearchRes search(SearchReq req) {
		
//		����list�����ȴ��X��
		String name=req.getName();
		LocalDate start=req.getStartDate();
		LocalDate end=req.getEndDate();
//		���X��A���ˬd
		if(!StringUtils.hasText(name)) {
			//�n�T�O�W�r�@�w�O�Ŧr��A���঳���ťզr���null�A�N������"�S����J�����"
			//�ҥH�N���̳��ഫ���Ŧr��""�A�÷j�M������T
//			JPA��containing����k�A����ȬO�Ŧr��ɡA�|�j������
			name="";
		}
		
		if(start==null ) {
			start=LocalDate.of(1970, 1, 1);
		}
		if(end==null) {
			end=LocalDate.of(2999, 12, 31);
		}
		return new SearchRes(ResMessage.SUCCESS.getCode(),
				ResMessage.SUCCESS.getMessage(),
				quizDao.findByNameContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqual(
						name, start, end));
	}

	@Override
	public BasicRes delete(DeleteReq req) {
//		�ˬd�Ѽ�
//		�]���O���X(list)�A�ҥH�n�ˬd�ɭn�ϥ�CollectionUtils
		
		/*
		if(CollectionUtils.isEmpty(req.getIdList())) {
			return new BasicRes(ResMessage.DELETE_IS_NULL.getCode(),
					ResMessage.DELETE_IS_NULL.getMessage());
		}
//		�R���ݨ�
		quizDao.deleteAllById(req.getIdList());
		return new BasicRes(ResMessage.SUCCESS.getCode(),
				ResMessage.SUCCESS.getMessage());
				
				*/
		
//		�t��
		
		if(!CollectionUtils.isEmpty(req.getIdList())) {
			try {
//				�R���ݨ�
				quizDao.deleteAllById(req.getIdList());
			}catch(Exception e){
				//��deleteAllById��k���Aid���Ȥ��s�b�ɡAjpa�|����
				//�]���R�����e�AJPA�|��"�j��"�a�J��id�A�A�h���R�����ʧ@(�ҥH�p�G�S���j����N�|����)
				//���]��ڤW�]�S�R�������T�A�ҥH�]�N�i�H���ݭn�o��exception���B�z
			}
		}
//		�]��(1)�L�R�����(null)(2)��J�F��Ƥ��s�b��id   �A�����|�s������T�A�ҥH�i�H��O���\
		return new BasicRes(ResMessage.SUCCESS.getCode(),
				ResMessage.SUCCESS.getMessage());
		
	}

//	====================================================================================
//	*************�s************�W**********��************��***********�s*******************
//	�o�q�i�H�����W����create
	@Override
	public BasicRes updateOrCreate(updateOrCreateReq req) {
//		�ˬd�Ѽ�
		BasicRes checkResult = checkParams(req);
//		��k�@
//		checkResult==null�ɡA��ܰѼ��ˬd���T
		if(checkResult!=null) {
			return checkResult;
		}
		/* ObjectMapper mapper=new ObjectMapper();  �o�q�X�G�T�w���ܪ�
		 * �ɶq"���n"�b�C���ǦC��/�ϧǦC�ƨϥήɳ� new ObjectMapper();�A�o�˪��N���O���Q���A
		 * ��_�@�ΦP�@�ӹ�ҡA��̪��į�i�H�ۮt�ܦh���C
		 * ObjectMapper �O thread-safe ������ �A�Ω󪫥��ഫ
		 */
		//�����Ϊk�i�Ծ\�U���s�����}�����e:
		//https://blog.csdn.net/u011213044/article/details/120329436
		ObjectMapper mapper=new ObjectMapper();
		try {
			//.writeValueAsString   : �N����ন�r�ꫬ�A
			String questionStr=mapper.writeValueAsString(req.getQuestionList());
			
//			Quiz quiz = new Quiz(req.getName(),req.getDescription(),
//					req.getStartDate(),req.getEndDate(),
//					questionStr,req.isPublished());    //�g�k1
			
			if(req.getId()>0) {    
				// �i�H�Pboo�X�֡A�ܦ�  �@�q
				// if(req.getId()>0 && !quizDao.existsById(req.getId())) {
				// return new BasicRes(ResMessage.UPDATE_ID_NOT_FOUND.getCode(),
				// ResMessage.UPDATE_ID_NOT_FOUND.getMessage());
				// }
//  *************************************************************************************
//		�H�U��ؤ覡�ܤ@(findById�PexistsById)
//		�ϥΤ�k�@(1�BfindById) �A�J����ơA�|�^�Ǥ@�㵧�����(�i���ƶq�|���e�j)
//		�ϥΤ�k�G(2�BexistsById) �A�]���z�LexistsById�ӧP�_�s���s�b�A�ҥH�^�Ǫ���ƥû����u�|�O�@��bit(0(false)/1(true))
//	**************************************************************************************
				
				//1�B�z�LfindById ��o���@��(PK)  �A�Y���ȫh�^�Ǿ㵧���
				/*----------------------------------
				 * �]���h�@�A�ҥH�N��k�@�����ѱ�
				Optional<Quiz> op=quizDao.findById(req.getId());
				//�P�_�O�_�����
				if(op.isEmpty()) {
					return new BasicRes(ResMessage.UPDATE_ID_NOT_FOUND.getCode(),
							ResMessage.UPDATE_ID_NOT_FOUND.getMessage());
				}
				
				Quiz quiz=op.get();
				//�]�w�s��(�ȱqreq��)
				//��k1:�Nreq�����s�ȳ]�w���ª�quiz���A���]�wid�A�]��id�@��
				
				quiz.setName(req.getName());
				quiz.setDescription(req.getDescription());
				quiz.setStartDate(req.getStartDate());
				quiz.setEndDate(req.getEndDate());
				quiz.setQuestions(questionStr);
				quiz.setPublished(req.isPublished());
				--------------------------------------------*/
				
				//2�B �z�LexistsById:�^�Ǥ@��bit����
//				boolean boo = quizDao.existsById(req.getId());
//				//�p�Gboo���s�b�A�h�϶ǵLid
				//�o��n�P�_�qreq�a�i�Ӫ�id�O�_�u���s�b��ob��
				//�Y���s�b�A����{���X�b�G�Njpa��save��k�ɷ|�ܦ��s�W
				//(���g�o�q�A�b���򪺾ާ@���A�䤣��ɥi��|�������s�W�A�ӨS������ڭ��~��ާ@)
				if(!quizDao.existsById(req.getId())) {
					return new BasicRes(ResMessage.UPDATE_ID_NOT_FOUND.getCode(),
							ResMessage.UPDATE_ID_NOT_FOUND.getMessage());
				}
//				quiz.setId(req.getId()); //�g�k1
			}	
				//��k2:�]���Onew�@�ӷs��quiz�A�ҥH�n��id��i�h
			
			//�g�k2
			//�o�˼g�k�O�]�� SQL�����i��j��(select)�ݬݬO�_��id�s�b��OB���A�p�G���N�w���ܧ�A�p�G�S���N�s�W
			Quiz quiz = new Quiz(req.getId(),req.getName(),req.getDescription(),
					req.getStartDate(),req.getEndDate(),
					questionStr,req.isPublished());   //����if�W��  
			//jpa�o�̪�save�i�H�P�ɰ���s�W�P��s(���|���i��j���A�@����s�W/�s�W)
			quizDao.save(quiz);
				
		} catch (JsonProcessingException e) {
			return new BasicRes(ResMessage.JSON_PROCESSING_EXCEPTION.getCode(),
					ResMessage.JSON_PROCESSING_EXCEPTION.getMessage());
		}
		
		return new BasicRes(ResMessage.SUCCESS.getCode(),
				ResMessage.SUCCESS.getMessage());
	}
	
	private BasicRes checkParams(updateOrCreateReq req) {
//		����  ���ˬd�ݨ��Ѽ�
		if(!StringUtils.hasText(req.getName())) {
			return new BasicRes(ResMessage.PARAM_QUIZ_NAME_ERROR.getCode(),
					ResMessage.PARAM_QUIZ_NAME_ERROR.getMessage());
		}
		if(!StringUtils.hasText(req.getDescription())) {
			return new BasicRes(ResMessage.PARAM_DESCRIPTION_ERROR.getCode(),
					ResMessage.PARAM_DESCRIPTION_ERROR.getMessage());
		}
//		�}�l�ɶ�����b����(�t)���e
//		(req�����}�l�ɶ����ভ��[isBefore]����(�t[isEqual])���e)�]�i������isAfter(���n�`�N���ᶶ��)
		if(req.getStartDate()==null || !req.getStartDate().isAfter(LocalDate.now()) ) {
			return new BasicRes(ResMessage.PARAM_STARDATE_ERROR.getCode(),
					ResMessage.PARAM_STARDATE_ERROR.getMessage());
		}
//		�ˬd�����ɶ�
		if(req.getEndDate()==null || req.getEndDate().isBefore(req.getStartDate())) {
			return new BasicRes(ResMessage.PARAM_ENDDATE_ERROR.getCode(),
					ResMessage.PARAM_ENDDATE_ERROR.getMessage());
		}
//		�ˬd���D���Ѽ�
		if(CollectionUtils.isEmpty(req.getQuestionList())) {
			return new BasicRes(ResMessage.PARAM_QUESTION_LIST_NOT_FOUND.getCode(),
					ResMessage.PARAM_QUESTION_LIST_NOT_FOUND.getMessage());
		}
//		�@�i�ݨ��i��|���h�Ӱ��D�A�ҥH�n�v���ˬd�C�@�D���Ѽ�(�ҥH��for)
		for(Question item:req.getQuestionList()) {
			if(item.getId()<=0) {
				return new BasicRes(ResMessage.PARAM_QUESTION_ID_ERROR.getCode(),
						ResMessage.PARAM_QUESTION_ID_ERROR.getMessage());
			}
			if(!StringUtils.hasText(item.getTitle())) {
				return new BasicRes(ResMessage.PARAM_QUESTION_TITLE_ERROR.getCode(),
						ResMessage.PARAM_QUESTION_TITLE_ERROR.getMessage());
			}
//			if(!StringUtils.hasText(item.getOptions())) {
//				return new BasicRes(ResMessage.PARAM_QUESTION_CONTENT_ERROR.getCode(),
//						ResMessage.PARAM_QUESTION_CONTENT_ERROR.getMessage());
//			}
			if( !StringUtils.hasText(item.getType())) {
				return new BasicRes(ResMessage.PARAM_QUESTION_TYPE_ERROR.getCode(),
						ResMessage.PARAM_QUESTION_TYPE_ERROR.getMessage());
			}
//			type�w�g�������ˬd�F�A���ڭ̷|�]���������P��option�]�|���Ҥ��P
//			��Option_type �O���Φh��ɡAoption�N����O�Ŧr��(�H�U�ˬd��/�h�����U�A�p���Ŧr���^���~)
//			����option_type�O��r�ɡA�h���\�Ŧr��
			if(item.getType().equals(OptionType.SINGLE_CHOICE.getType())
					|| item.getType().equals(OptionType.MULIT_CHOICE.getType())) {
				if(!StringUtils.hasText(item.getOptions())) {
					return new BasicRes(ResMessage.PARAM_QUESTION_CONTENT_ERROR.getCode(),
							ResMessage.PARAM_QUESTION_CONTENT_ERROR.getMessage());
				}
			}
			
		}
		return null;
	}

}
