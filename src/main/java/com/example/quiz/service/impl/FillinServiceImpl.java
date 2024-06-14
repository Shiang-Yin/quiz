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
		// 參數接回來
		BasicRes checkResult = checkParam(req);
		if (checkResult != null) {
			return checkResult;
		}
//		====================================================================
//		檢查填寫問卷的人是否有重複填寫(以電話號碼為審查)
		if (responseDao.existsByQuizIdAndPhone(req.getQuizId(), req.getPhone())) {
			return new BasicRes(ResMessage.DUPLICATED_FILLIN.getCode(), ResMessage.DUPLICATED_FILLIN.getMessage());
		}
		// 檢查quiz_id(問卷編碼)是否存在於DB中，並調出我原本在quiz中的questions(獲取裡面資訊)
		// 因為後續會對比req中的答案與題目的選項是否符合，所以要用findById
		Optional<Quiz> op = quizDao.findById(req.getQuizId());
		if (op.isEmpty()) {
			return new BasicRes(ResMessage.QUIZ_NOT_FOUND.getCode(), ResMessage.QUIZ_NOT_FOUND.getMessage());
		}
		// 將字串轉成特定類別的CLASS:readValue(String ,xxx () ) ;
		// 轉成特定類別的List :readValue( XX , new TypeReference <>(){})
		Quiz quiz = op.get();
		// 從quiz中取出question的字串
		String questionStr = quiz.getQuestions();
		// 使用ObjectMapper ，將questionStr轉乘List<Question>
		String fillinStr = "";
		// 取得qid的所有號碼
		List<Integer> qIds = new ArrayList<>();
		List<Integer> ansQId = new ArrayList<>();
		// 將我得到的答案資訊傳回此list
		List<Fillin> filledAnswers = new ArrayList<>();

		try {
			// question原本(在quiz中)是字串型態，為了得到question裡面的資訊，所以需要先轉成list
			List<Question> quList = mapper.readValue(questionStr, new TypeReference<>() {
			});
//			for (Fillin ans : req.getFillin()) {
//				//我先假設一個 boolean(初始狀態為false)
//	            boolean idMatched = false;
//	            //如果我有找到就會進入這個迴圈
//	            for (Question item : quList) {
//	                if (item.getId() == ans.getqId()) {
//	                	//這裡是當它有找到條件時
//	                    idMatched = true;
//	                    break;
//	                }
//	            }
//	            //如果沒有找到就不進入迴圈，直接停止
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

//			用for迴圈檢查question中的所有值
			for (Question item : quList) {

//				boolean match=false;//先初始化
				for (Fillin ans : questionFillin) {
//					//id不一樣直接跳過
//					if(item.getId()!=ans.getqId()) {
//						continue;
//					}
//					//如果重複填寫，則取締一比其他都去除
//					if(qIds.contains(ans.getqId())) {
//						continue;
//					}
//	========================================================================
					/**
					 * //將question的id與fillin的id相比對，如果不相等就跳出警告 qIds.add(item.getId());
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

							// 檢查答案
							String answerStr = ans.getAnswer();
							String[] answerArray = answerStr.split(";");

							String[] optionArray = item.getOptions().split(";");
							List<String> optionList = List.of(optionArray);

							// 題型是單選且答案長度大於1時
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
						break; // 停止迴圈
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
//		檢查參數 
		// 假設問題編號小於0，返回錯誤
		if (req.getQuizId() <= 0) {
			return new BasicRes(ResMessage.PARAM_QUIZ_ID_ERROR.getCode(), ResMessage.PARAM_QUIZ_ID_ERROR.getMessage());
		}
		// 填寫個人訊息(名子、手機、信箱不為空值)
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
		// 為了取得查看問卷是否存在
		Optional<Quiz> op = quizDao.findById(req.getQuizId());
		if (op.isEmpty()) {
			return new StatisticsRes(ResMessage.QUIZ_NOT_FOUND.getCode(), //
					ResMessage.QUIZ_NOT_FOUND.getMessage());
		}
		Quiz ques = op.get();
		// 取得quiz中的問卷編號、標題、開始時間、結束時間
		int quizId = ques.getId();
		String name = ques.getName();
		LocalDate startdate = ques.getStartDate();
		LocalDate enddate = ques.getEndDate();

		// 利用findById的方式，調出response(response是一份問卷，quiz有很多張問卷(response),
		//所以response也算是quiz的一個集合)
		List<Response> responseList = responseDao.findByQuizId(req.getQuizId());
		
		// 建立一個新的hasMap,用來放置我的<題目編號,<問題選項,計算次數>>
		Map<Integer, Map<String, Integer>> ansStatistics = new HashMap<>();

		//放置 :當條件為文件時，顯示所有填寫的資料(顯示:題目號碼、題目、答案)
		List<FeedbackText> feedbackText=new ArrayList<FeedbackText>();
		//    題號     題目
		Map<Integer, String> textStatistics=new HashMap<>();

		// 在問卷中抓取我的填答資訊
		for (Response item : responseList) {
			// 將填答資料轉從字串(String)轉換成(list)
			try {
				List<Fillin> fillStrList = mapper.readValue(item.getFillin(), new TypeReference<>() {
				});

				
				for (Fillin fillin : fillStrList) {
					//   題目    答案統計 
					Map<String, Integer> quAndCount = new HashMap<>();
					//1、先提取出選項
					String optionStr = fillin.getQuestion();
					//1.1 將選項用";"去除
					String[] optionArray = optionStr.split(";");
					//2、再提取出答案
					String answer = fillin.getAnswer();
					//將答案的前後都加上";"
					answer = ";" + answer + ";";
					//開始進行計算
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
					
					
					
//					// 題取出選項
////					String optionStr = fillin.getQuestion();
////					String[] optionArray = optionStr.split(";");
//					// 題取出答案
//					String answer = fillin.getAnswer();
//					// 將答案前後都加上";"
//					answer = ";" + answer + ";";
//					// 比對答案與選項
//					// 把題目當作目標，來篩選答案出現次數
//					// 為了避免每個選項是另一個答案的其中一個部分(例如: "綠茶"與"綠茶拿鐵")
//					// 所以我們可以將每個選項的前後都加上一個;(分號)(變成 ;綠茶;綠茶拿鐵; 原本 綠茶;綠茶拿鐵)
////					List<String> totalAns=new ArrayList<String>();
////					statResults.clear();
//					// 答案的統計計算放入地方
//					List<StatResults> statResults = new ArrayList<StatResults>();
//					statResults.clear();
//					for (String option : quOptionList) {
//						
//						// 串分號
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