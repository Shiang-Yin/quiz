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
import com.example.quiz.service.ifs.FillinServic;
import com.example.quiz.vo.BasicRes;
import com.example.quiz.vo.FeedbackReq;
import com.example.quiz.vo.FeedbackRes;
import com.example.quiz.vo.FeedbackText;
import com.example.quiz.vo.Fillin;
import com.example.quiz.vo.FillinReq;
import com.example.quiz.vo.Question;
import com.example.quiz.vo.Statistics;
import com.example.quiz.vo.StatisticsRes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FillinServiceImpl implements FillinServic {

	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private QuizDao quizDao;

	@Autowired
	private ResponseDao responseDao;

	@Override
	public BasicRes fillin(FillinReq req) {
//		====================================================================
		// �ѼƱ��^��
		BasicRes checkResult = checkParam(req);
		if (checkResult != null) {
			return checkResult;
		}
//		====================================================================
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
		String fillinStr = "";
		// ���oqid���Ҧ����X
		List<Integer> qIds = new ArrayList<>();
		List<Integer> ansQId = new ArrayList<>();
		// �N�ڱo�쪺���׸�T�Ǧ^��list
		List<Fillin> filledAnswers = new ArrayList<>();

		try {
			// question�쥻(�bquiz��)�O�r�ꫬ�A�A���F�o��question�̭�����T�A�ҥH�ݭn���নlist
			List<Question> quList = mapper.readValue(questionStr, new TypeReference<>() {
			});
//			for (Fillin ans : req.getFillin()) {
//				//�ڥ����]�@�� boolean(��l���A��false)
//	            boolean idMatched = false;
//	            //�p�G�ڦ����N�|�i�J�o�Ӱj��
//	            for (Question item : quList) {
//	                if (item.getId() == ans.getqId()) {
//	                	//�o�̬O�����������
//	                    idMatched = true;
//	                    break;
//	                }
//	            }
//	            //�p�G�S�����N���i�J�j��A��������
//	            if (!idMatched) {
//	                return new BasicRes(ResMessage.PARAM_QUESTION_ID_NOT_FOUND.getCode(), //
//	                		ResMessage.PARAM_QUESTION_ID_NOT_FOUND.getMessage());
//	            }
//	        }
			List<Fillin> questionFillin = req.getFillin();
//			for(Question item:quList) {
//				for(Fillin ans:questionFillin) {
//					qIds.add(item.getId());
//					ansQId.add(ans.getqId());
//					if(qIds.equals(ansQId)) {
//						break;
//					}else {
//						 return new BasicRes(ResMessage.PARAM_QUESTION_ID_NOT_FOUND.getCode(), //
//			                		ResMessage.PARAM_QUESTION_ID_NOT_FOUND.getMessage());
//					}
//				}
//			}

//			��for�j���ˬdquestion�����Ҧ���
			for (Question item : quList) {

//				boolean match=false;//����l��
				for (Fillin ans : questionFillin) {
//					//id���@�˪������L
//					if(item.getId()!=ans.getqId()) {
//						continue;
//					}
//					//�p�G���ƶ�g�A�h���l�@���L���h��
//					if(qIds.contains(ans.getqId())) {
//						continue;
//					}
//	========================================================================
					/**
					 * //�Nquestion��id�Pfillin��id�ۤ��A�p�G���۵��N���Xĵ�i qIds.add(item.getId());
					 * ansQId.add(ans.getqId()); if(!qIds.equals(ansQId)) { return new
					 * BasicRes(ResMessage.PARAM_QUIZ_ID_ERROR.getCode(),
					 * ResMessage.PARAM_QUIZ_ID_ERROR.getMessage()); }
					 **/
//=============================================================================					
					if (item.getId() == ans.getqId()) {
//						match=true;

						if (item.isNecessary() && !StringUtils.hasText(ans.getAnswer())) {
							return new BasicRes(ResMessage.ANSWER_IS_REQUIRED.getCode(),
									ResMessage.ANSWER_IS_REQUIRED.getMessage());
						}
						if (!item.getType().equalsIgnoreCase(OptionType.TEXT.getType())) {

							// �ˬd����
							String answerStr = ans.getAnswer();
							String[] answerArray = answerStr.split(";");

							String[] optionArray = item.getOptions().split(";");
							List<String> optionList = List.of(optionArray);

							// �D���O���B���ת��פj��1��
							if (item.getType().equalsIgnoreCase(OptionType.SINGLE_CHOICE.getType())
									&& answerArray.length > 1) {
								return new BasicRes(ResMessage.SHOOSE_ONE_ANSER.getCode(),
										ResMessage.SHOOSE_ONE_ANSER.getMessage());
							}

							for (String str : answerArray) {
								if (item.isNecessary() && !optionList.contains(str)) {
									return new BasicRes(ResMessage.ANSWER_OPTIONS_IS_NOT_MATCH.getCode(),
											ResMessage.ANSWER_OPTIONS_IS_NOT_MATCH.getMessage());
								}
								if (!item.isNecessary() && StringUtils.hasText(str) && !optionList.contains(str)) {
									return new BasicRes(ResMessage.IS_NOT_MATCH.getCode(),
											ResMessage.IS_NOT_MATCH.getMessage());
								}
							}
						}

						Fillin fillin = new Fillin();
						fillin.setqId(ans.getqId());
						fillin.setTitle(item.getTitle());
						fillin.setQuestion(item.getOptions());
						fillin.setAnswer(ans.getAnswer());
						fillin.setType(item.getType());
						fillin.setNecessary(item.isNecessary());

						filledAnswers.add(fillin);
						break; // ����j��
					}

				}

			}

			fillinStr = mapper.writeValueAsString(filledAnswers);
			responseDao.save(new Response(req.getQuizId(), req.getName(), req.getPhone(), //
					req.getEmail(), req.getAge(), fillinStr));

			return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());

		} catch (JsonProcessingException e) {
			return new BasicRes(ResMessage.JSON_PROCESSING_EXCEPTION.getCode(),
					ResMessage.JSON_PROCESSING_EXCEPTION.getMessage());
		}

	}

//	============================================================================
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

//	============================================================================

	@Override
	public FeedbackRes feedback(FeedbackReq req) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StatisticsRes statistics(FeedbackReq req) {
		// ���F���o�d�ݰݨ��O�_�s�b
		Optional<Quiz> op = quizDao.findById(req.getQuizId());
		if (op.isEmpty()) {
			return new StatisticsRes(ResMessage.QUIZ_NOT_FOUND.getCode(), //
					ResMessage.QUIZ_NOT_FOUND.getMessage());
		}
		Quiz ques = op.get();
		// ���oquiz�����ݨ��s���B���D�B�}�l�ɶ��B�����ɶ�
		int quizId = ques.getId();
		String name = ques.getName();
		LocalDate startdate = ques.getStartDate();
		LocalDate enddate = ques.getEndDate();

		// �Q��findById���覡�A�եXresponse(response�O�@���ݨ��Aquiz���ܦh�i�ݨ�(response),
		//�ҥHresponse�]��Oquiz���@�Ӷ��X)
		List<Response> responseList = responseDao.findByQuizId(req.getQuizId());
		
		// �إߤ@�ӷs��hasMap,�Ψө�m�ڪ�<�D�ؽs��,<���D�ﶵ,�p�⦸��>>
		Map<Integer, Map<String, Integer>> ansStatistics = new HashMap<>();

		//��m :����󬰤��ɡA��ܩҦ���g�����(���:�D�ظ��X�B�D�ءB����)
		List<FeedbackText> feedbackText=new ArrayList<FeedbackText>();
		//    �D��     �D��
		Map<Integer, String> textStatistics=new HashMap<>();

		// �b�ݨ�������ڪ��񵪸�T
		for (Response item : responseList) {
			// �N�񵪸����q�r��(String)�ഫ��(list)
			try {
				List<Fillin> fillStrList = mapper.readValue(item.getFillin(), new TypeReference<>() {
				});

				
				for (Fillin fillin : fillStrList) {
					//   �D��    ���ײέp 
					Map<String, Integer> quAndCount = new HashMap<>();
					//1�B�������X�ﶵ
					String optionStr = fillin.getQuestion();
					//1.1 �N�ﶵ��";"�h��
					String[] optionArray = optionStr.split(";");
					//2�B�A�����X����
					String answer = fillin.getAnswer();
					//�N���ת��e�᳣�[�W";"
					answer = ";" + answer + ";";
					//�}�l�i��p��
					for (String option : optionArray) {
						if(fillin.getType().equals(OptionType.TEXT.getType())) {
							textStatistics.put(fillin.getqId(), fillin.getQuestion());
							FeedbackText textResult=new FeedbackText();
							textResult.setTextStatistics(textStatistics);
							textResult.setTextStat(fillin.getAnswer());
							feedbackText.add(textResult);
						}else {
							String newoption = ";" + option + ";";
							int oldAnswerStr = answer.length();
							int newAnswerStr = answer.replace(newoption, "").length();
							int statTotal = (oldAnswerStr - newAnswerStr) / newoption.length();
							
							// 
							quAndCount = ansStatistics.getOrDefault(fillin.getqId(), quAndCount);
	
							int oldCount = quAndCount.getOrDefault(option, 0);
							quAndCount.put(option, oldCount + statTotal);
							ansStatistics.put(fillin.getqId(), quAndCount);
							
						}
						
					}
					
					
					
//					// �D���X�ﶵ
////					String optionStr = fillin.getQuestion();
////					String[] optionArray = optionStr.split(";");
//					// �D���X����
//					String answer = fillin.getAnswer();
//					// �N���׫e�᳣�[�W";"
//					answer = ";" + answer + ";";
//					// ��ﵪ�׻P�ﶵ
//					// ���D�ط�@�ؼСA�ӿz�ﵪ�ץX�{����
//					// ���F�קK�C�ӿﶵ�O�t�@�ӵ��ת��䤤�@�ӳ���(�Ҧp: "���"�P"������K")
//					// �ҥH�ڭ̥i�H�N�C�ӿﶵ���e�᳣�[�W�@��;(����)(�ܦ� ;���;������K; �쥻 ���;������K)
////					List<String> totalAns=new ArrayList<String>();
////					statResults.clear();
//					// ���ת��έp�p���J�a��
//					List<StatResults> statResults = new ArrayList<StatResults>();
//					statResults.clear();
//					for (String option : quOptionList) {
//						
//						// �����
//						String newoption = ";" + option + ";";
//						int oldAnswerStr = answer.length();
//						int newAnswerStr = answer.replace(newoption, "").length();
//						int statTotal = (oldAnswerStr - newAnswerStr) / newoption.length();
//
//						
//						StatResults results = new StatResults();
//						results.setOptionItem(option);
//						results.setCount(statTotal);
//
//						statResults.add(results);
//					}

//					Statistics statis = new Statistics();
//					statis.setqId(item.getQuizId());
//					statis.setTopic(fillin.getTitle());//
//					statis.setOptions(fillin.getQuestion());
////					statis.setAnsStatistics(ansStatistics);
//					statistics.add(statis);//

				}

			} catch (JsonProcessingException e) {
				return new StatisticsRes(ResMessage.JSON_PROCESSING_EXCEPTION.getCode(),
						ResMessage.JSON_PROCESSING_EXCEPTION.getMessage());
			}
		}

		return new StatisticsRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(),//
				quizId,name, startdate,enddate, ansStatistics,feedbackText);
	}
}