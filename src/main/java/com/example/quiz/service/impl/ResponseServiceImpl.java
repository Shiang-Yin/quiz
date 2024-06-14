package com.example.quiz.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.quiz.constants.OptionType;
import com.example.quiz.constants.ResMessage;
import com.example.quiz.entity.Quiz;
import com.example.quiz.entity.Response;
import com.example.quiz.repository.QuizDao;
import com.example.quiz.repository.ResponseDao;
import com.example.quiz.service.ifs.ResponseService;
import com.example.quiz.vo.BasicRes;
import com.example.quiz.vo.FeedbackReq;
import com.example.quiz.vo.FeedbackRes;
import com.example.quiz.vo.Fillin;
import com.example.quiz.vo.FillinReq;
import com.example.quiz.vo.Question;
import com.example.quiz.vo.Text;
import com.example.quiz.vo.TextReq;
import com.example.quiz.vo.TextRes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ResponseServiceImpl implements ResponseService {

	@Autowired
	private QuizDao quizDao;

	@Autowired
	private ResponseDao responseDao;

	@Override
	public BasicRes fillin(FillinReq req) {
		// �ѼƱ��^��
		BasicRes checkResult = checkParam(req);
		if (checkResult != null) {
			return checkResult;
		}
//		�ˬd��g�ݨ����H�O�_�����ƶ�g(�H�q�ܸ��X���f�d)
		if (responseDao.existsByQuizIdAndPhone(req.getQuizId(), req.getPhone())) {
			return new BasicRes(ResMessage.DUPLICATED_FILLIN.getCode(), ResMessage.DUPLICATED_FILLIN.getMessage());
		}
		// �ˬdquiz_id(�ݨ��s�X)�O�_�s�b��DB���A�ýեX�ڭ쥻�bquiz����questions(����̭���T)
		// �]������|���req�������׻P�D�ت��ﶵ�O�_�ŦX�A�ҥH�n��findById
		Optional<Quiz> op = quizDao.findById(req.getQuizId());
		if (op.isEmpty()) {
			return new BasicRes(ResMessage.QUIZ_NOT_FOUND.getCode(), ResMessage.QUIZ_NOT_FOUND.getMessage());
		}
		// �N�r���ন�S�w���O��CLASS:readValue(String ,xxx () ) ;
		// �ন�S�w���O��List :readValue( XX , new TypeReference <>(){})
		Quiz quiz = op.get();
		// �qquiz�����Xquestion���r��
		String questionStr = quiz.getQuestions();
		// �ϥ�ObjectMapper �A�NquestionStr�୼List<Question>
		ObjectMapper mapper = new ObjectMapper();
		// fillinStr�n���Ŧr��A���M�w�]�ȷ|�Onull
		// �YfillinStr=null �A �������fillinStr =
		// mapper.writeValueAsString(req.getqIdAnswerMap());
		// �����o�쵲�G��^��fillinStr�ɷ|�A����
		String fillinStr = "";
//		List<Question> quList=mapper.readValue(questionStr, new TypeReference<>(){});
//		questionStr, new TypeReference<���A>(�i��list[�Ҧp:List.of()]){��H���O}
		// �W��������(try...catch)�A�N�i�ܦ��U������
		try {
			List<Question> quList = mapper.readValue(questionStr, new TypeReference<>() {
			});
//			��for�j���ˬdquestion�����Ҧ���
			for (Question item : quList) {
//				�����D�O�_����A�Breq����getqIdAnswerMap������qId������
//				�ˬd�ﵪ�׬O�_�P�ﶵ�@�P
//				�⵪�צr��(req.getqIdAnswerMap() ����value) �Τ���(;)���Φ��}�C
//				�z�Litem����id��key�Ө��oreq.getqIdAnswerMap()��������value��
//				�ϥ�get(key),map�|�ھ�key���o������value��
				// �ھ� �ݨ�(Question)��id�^�����D (question��id�O���D���D�� �Ҧp :1�B���Ƴܤ���?�Boptions�B...)
				// question����L���ڳ����]�A�ڴN�u��"�D��"�Ӥw�A�M��ھ��D���g�U�ڪ�����
				String answerStr = req.getqIdAnswerMap().get(item.getId());
//				�M��A�NanswerStr(����)���Φ��}�C(�]���h�藍�u�@�ӵ��סA�@�˥H";"���j�ӮѼg)  //�ڪ����פ���
				String[] answerArray = answerStr.split(";");
//				if(item.isNecessary() && (!req.getqIdAnswerMap().containsKey(item.getId())
//						||!StringUtils.hasText(answerStr)))
//				�P86��X��  �ܦ�  �� (�����(true) *�B* ���פS�� null�B""�B" "
				if (item.isNecessary() && !StringUtils.hasText(answerStr)) {
					return new BasicRes(ResMessage.ANSWER_IS_REQUIRED.getCode(),
							ResMessage.ANSWER_IS_REQUIRED.getMessage());
				}

				// �ư�(���L)²���D�Foption type �O text
				if (item.getType().equalsIgnoreCase(OptionType.TEXT.getType())) {
					continue;
				}
				// �ư�option type �O���A�����צ��h�Ӯ�
				// �D���O���B���ת��פj��1��
				if (item.getType().equalsIgnoreCase(OptionType.SINGLE_CHOICE.getType()) && answerArray.length > 1) {
					return new BasicRes(ResMessage.SHOOSE_ONE_ANSER.getCode(),
							ResMessage.SHOOSE_ONE_ANSER.getMessage());
				}

//				option�ﶵ���οﶵ����
				String[] optionArray = item.getOptions().split(";");
//				���Τ���A�Ӥ��
				List<String> optionList = List.of(optionArray);

//				��C�ӵ��׸�ﶵ���:�N�O��ﵪ�׸���D�ﶵ�O�_�@�P
				for (String str : answerArray) {
					/*
					 * containsTest���Ϊk //���]item.getOptions()���ȬO"A;B;C;D" //���]answerArray=[A,B] //for
					 * �j�餤�N�O��A�MB���O�_�]�t�b�r��̭��Aitem.getOptions()�� //�B�n�ư��Otext�� //contains
					 * �i�H��test��containsTest���Ϊk
					 */
//					if(item.isNecessary() && !optionList.contains(str)
//							&& !item.getType().equalsIgnoreCase(OptionType.TEXT.getType()))
					if (item.isNecessary() && !optionList.contains(str)
							|| (!item.isNecessary() && StringUtils.hasText(str) && !optionList.contains(str))) {
						return new BasicRes(ResMessage.ANSWER_OPTIONS_IS_NOT_MATCH.getCode(),
								ResMessage.ANSWER_OPTIONS_IS_NOT_MATCH.getMessage());
					}
//					else if(!item.isNecessary() && (StringUtils.hasText(str) && !optionList.contains(str))){
//						return new BasicRes(ResMessage.ANSWER_OPTIONS_IS_NOT_MATCH.getCode(),
//								ResMessage.ANSWER_OPTIONS_IS_NOT_MATCH.getMessage());
//					}
				}

			}
			fillinStr = mapper.writeValueAsString(req.getqIdAnswerMap());

		} catch (JsonProcessingException e) {
			return new BasicRes(ResMessage.JSON_PROCESSING_EXCEPTION.getCode(),
					ResMessage.JSON_PROCESSING_EXCEPTION.getMessage());
		}

		responseDao.save(new Response(req.getQuizId(), req.getName(), req.getPhone(), //
				req.getEmail(), req.getAge(), fillinStr));

		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());

	}
//	====================================================================================================

	private BasicRes checkParam(FillinReq req) {
//		�ˬd�Ѽ� 
		// ���]���D�s���p��0�A��^���~
		if (req.getQuizId() <= 0) {
			return new BasicRes(ResMessage.PARAM_QUIZ_ID_ERROR.getCode(), ResMessage.PARAM_QUIZ_ID_ERROR.getMessage());
		}
		// ��g�ӤH�T��(�W�l�B����B�H�c�����ŭ�)
		if (!StringUtils.hasText(req.getName())) {
			return new BasicRes(ResMessage.PARAM_NAME_IS_REQUIRED.getCode(),
					ResMessage.PARAM_NAME_IS_REQUIRED.getMessage());
		}
		if (!StringUtils.hasText(req.getPhone())) {
			return new BasicRes(ResMessage.PARAM_PHONE_IS_REQUIRED.getCode(),
					ResMessage.PARAM_PHONE_IS_REQUIRED.getMessage());
		}
		if (!StringUtils.hasText(req.getEmail())) {
			return new BasicRes(ResMessage.PARAM_EMAIL_IS_REQUIRED.getCode(),
					ResMessage.PARAM_EMAIL_IS_REQUIRED.getMessage());
		}
		if (req.getAge() < 12 || req.getAge() > 99) {
			return new BasicRes(ResMessage.PARAM_AGE_NOT_QUALIFIED.getCode(),
					ResMessage.PARAM_AGE_NOT_QUALIFIED.getMessage());
		}

		return null;
	}

//=======================================================================================================
	@Override
	public FeedbackRes feedback(FeedbackReq req) {

		ObjectMapper mapper = new ObjectMapper();
		// �q QuizDao ���d�� Quiz ��H
		Optional<Quiz> quiz = quizDao.findById(req.getQuizId());
		if (quiz.isEmpty()) {
			return new FeedbackRes(ResMessage.QUIZ_NOT_FOUND.getCode(), ResMessage.QUIZ_NOT_FOUND.getMessage());
		}
		Quiz quizGet = quiz.get();
//        ���oList<Question>���r��
//        quiz(entity)��question�O�r��A���O�D��Question���ܦh�A�ҥH�N�o�ର�}�C
		String questionStr = quizGet.getQuestions();
//        �A�N�L�নlist<question>
//        List<Question> quList=mapper.readValue(questionStr, new TypeReference<>() {});�I�@�U��try...catch
		try {
			List<Question> quList = mapper.readValue(questionStr, new TypeReference<>() {
			});

//			����C�Ӱ��D���s�X�M�D�ة��quAnsMap��
			Map<Integer, Map<String, String>> quIdAndQuAnsMap = new HashMap<>();
//			lambda�g�k
//			�o�̪�item�N�O  for(Question item:quList)����item�O�@�˪�
			quList.forEach(item -> {
				Map<String, String> quAndAnsMap = new HashMap<>();
				// �S�����שҥH����null
				quAndAnsMap.put(item.getTitle(), null);
				quIdAndQuAnsMap.put(item.getId(), quAndAnsMap);
			});

//			�����P�@���ݨ����^�X(�Adao���w�q������^�ӳo�̼������)�A�b�o�̬O�]��req��id�O�Presponse�O�@�˪��A���O�ݨ��s�X
			List<Response> resList = responseDao.findByQuizId(req.getQuizId());
//			�]�����X�Ӹ��|���ܦh�^�X�A�ҥH�n���M���C�@���^�X
//			resList�Y�O�]��lambda�g�k�|�A�����ͥX�@��try...catch...
			for (Response item : resList) {
				// ���X���D���X(�bfillinReq��)
				// �D�� ����
				Map<Integer, String> qIdAnswerMap = mapper.readValue(item.getFillin(), new TypeReference<>() {
				});
				// lambda�g�k
				// quIdAndQuAnsMap����key�O�D��(�PqIdAnswerMap��key�ȬO�ۦP��)�A�ҥH�p�G���A�N�i���O����g����
//				quIdAndQuAnsMap: �]�t�Ҧ��D�ت��s��
//				qIdAnswerMap :�u�]�t���ת��s��(k)
				qIdAnswerMap.forEach((k, v) -> {
					if (quIdAndQuAnsMap.containsKey(k)) {
						// �z�Lk���XquIdAndQuAnsMap��������value
						// �o�̪�map�O"�D��"�P"����"��map
						Map<String, String> map = quIdAndQuAnsMap.get(k);
						// �]���D�ؤ��ܡA�����N���A�ҥHkey�Ⱥ���
						map.forEach((k1, v1) -> {
							map.put(k1, v);
						});
					}
				});
			}

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * =============================================================================
		 * ========= // ���N��ƴ����X�� String quizGetName = quizGet.getName(); String
		 * quizGetDescription = quizGet.getDescription(); LocalDate quizGetStartDate =
		 * quizGet.getStartDate(); LocalDate quizGetEndDate = quizGet.getEndDate(); // �q
		 * ResponseDao ���d�� Response ��H�C�� // List<Response> responses =
		 * responseDao.findByAll(req.getQuizId()); List<Response> res =
		 * responseDao.findAll();
		 * 
		 * FeedbackDetail feedbackDetail = new FeedbackDetail(); for(Response item:res)
		 * { feedbackDetail.setUserName(item.getName());
		 * feedbackDetail.setPhone(item.getPhone());
		 * feedbackDetail.setEmail(item.getEmail()); }
		 * 
		 * for(FeedbackRes) {
		 * 
		 * }
		 * =============================================================================
		 * =============
		 */
		return null;
//		return new FeedbackRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(), feedbackDetail);
	}

	@Override
	public TextRes text(TextReq req) {

		// �q QuizDao ���d�� Quiz ��H
		Optional<Quiz> op = quizDao.findById(req.getQuizId());
		if (op.isEmpty()) {
			return new TextRes(ResMessage.QUIZ_NOT_FOUND.getCode(), ResMessage.QUIZ_NOT_FOUND.getMessage());
		}
		Quiz quiz = op.get();
		// �ھ� quizId �d�� Response ��H�C��
		List<Response> responses = responseDao.findByQuizId(req.getQuizId());

		String quizTitle = quiz.getName();
		String quizDescription = quiz.getDescription();
		LocalDate quizStartDate = quiz.getStartDate();
		LocalDate quizEndDate = quiz.getEndDate();
		int code = ResMessage.SUCCESS.getCode();
		String message = ResMessage.SUCCESS.getMessage();

//        =====================================================
//		�ഫ(�r�������O�����O��r��)
		ObjectMapper mapper = new ObjectMapper();
//		�q�ݨ������X���D(�쥻�����O�r��A��L�O�@�ӫܦh�ܹy���ҥH�୼list)
		String question = quiz.getQuestions();
//		List<Question> quList = mapper.readValue(question, new TypeReference<List<Question>>(){});
		List<Question> quList = null;
		
		try {
			quList = mapper.readValue(question, new TypeReference<List<Question>>(){});

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		// �]�@�ӷs��list�N��Ƴ���i��(�]�N�����F�ڭ�textRes�����A�F)
		List<Text> textList = new ArrayList<>();

		// Response���]�t�F���D�M����
		for (Response response : responses) {
			//��J�̦W�l
			String fillinName = response.getName();
			String fillinPhone = response.getPhone();
			
			//���צbfillinList��
			String fillinStr = response.getFillin();
			List<Fillin> fillinList = null;
			try {
				fillinList = mapper.readValue(fillinStr, new TypeReference<>(){});
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

			//�Q�K�ڪ����D(�]�����DQuestion���t��"�D��id"��"�D��")�A�p�G�u��Response�ڬO������ݨ��̭���ԲӪ����
				for(Fillin ansItem:fillinList) {
					//�b�j�餺�s�ؤ@�ӷs��text���ت��O���F : ���C��text�ͦ��ɳ��O�W�ߪ��A�B�̭��]�t�������b�K�Q�ɳ��|�[�i��
					Text text = new Text();
					//�]���ڦb�ڪ�Text�����]�mset�A�ҥH�ȬO�i�H��ʪ�
					//�M��A�N�کһݭn����get�X�ө�Jset
					text.setQuizName(quizTitle);
					text.setDescription(quizDescription);
					text.setStartDate(quizStartDate);
					text.setEndDate(quizEndDate);
					text.setFillinName(fillinName);
					text.setPhone(fillinPhone);
					text.setEmail(response.getEmail());
					text.setAge(response.getAge());
					text.setFillinDateTime(response.getFillinDateTime());
					text.setQuId(ansItem.getqId());
					text.setTitle(ansItem.getQuestion());
					
					//�]�����ת�key�]�O��qustion��id����
					//�qResponse�����Map<k(�D�ؽs��),v(����)>
					//�M��]���D�ؽs��(key��)����Question��id
//					String answer = fillinStr.get(item.getId);
					text.setAns(ansItem.getAnswer());

					//�N��R�n��text�[��textList��(�]��text���u�@���A�ҥH�~��list)
					textList.add(text);
				}
				
				
		}

		// �Ыؤ@�ӷs���ШD�A�N�ڪ��ȳ���^�h�A�o�˧ڪ�res�~�⧹��
		//���ƻ�textList�٤��⧹���O? 
		//�]��textList�S��set�^�ڪ�textRes�A�o�˷|��[]�A�ҥH�O�o��^�h
		//�]���ڪ��ШD�O(public ""TextRes"" text(TextReq req))
		//�p�G�ڪ��ШD�ܦ��F Text ���ڴN�i�H������^����list<Text>
		TextRes textRes = new TextRes();
		textRes.setCode(code);
		textRes.setMessage(message);
		textRes.setText(textList);
		//�i�H�����g return new TextRes(code,message,textList);(�i����345~348�B350)
		return textRes;
	}
}