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
		// 參數接回來
		BasicRes checkResult = checkParam(req);
		if (checkResult != null) {
			return checkResult;
		}
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
		ObjectMapper mapper = new ObjectMapper();
		// fillinStr要給空字串，不然預設值會是null
		// 若fillinStr=null ， 後續執行fillinStr =
		// mapper.writeValueAsString(req.getqIdAnswerMap());
		// 把執行得到結果塞回給fillinStr時會，報錯
		String fillinStr = "";
//		List<Question> quList=mapper.readValue(questionStr, new TypeReference<>(){});
//		questionStr, new TypeReference<型態>(可放list[例如:List.of()]){抽象類別}
		// 上面那行選擇(try...catch)，就可變成下面那行
		try {
			List<Question> quList = mapper.readValue(questionStr, new TypeReference<>() {
			});
//			用for迴圈檢查question中的所有值
			for (Question item : quList) {
//				比對該題是否必填，且req中的getqIdAnswerMap對應的qId有答案
//				檢查選答案是否與選項一致
//				把答案字串(req.getqIdAnswerMap() 中的value) 用分號(;)切割成陣列
//				透過item中的id當成key來取得req.getqIdAnswerMap()中對應的value值
//				使用get(key),map會根據key取得對應的value直
				// 根據 問卷(Question)的id回答問題 (question的id是問題的題號 例如 :1、飲料喝什麼?、options、...)
				// question中其他的我都不館，我就只抓"題號"而已，然後根據題號寫下我的答案
				String answerStr = req.getqIdAnswerMap().get(item.getId());
//				然後再將answerStr(答案)切割成陣列(因為多選不只一個答案，一樣以";"分隔來書寫)  //我的答案分割
				String[] answerArray = answerStr.split(";");
//				if(item.isNecessary() && (!req.getqIdAnswerMap().containsKey(item.getId())
//						||!StringUtils.hasText(answerStr)))
//				與86行合併  變成  當 (必填時(true) *且* 答案又為 null、""、" "
				if (item.isNecessary() && !StringUtils.hasText(answerStr)) {
					return new BasicRes(ResMessage.ANSWER_IS_REQUIRED.getCode(),
							ResMessage.ANSWER_IS_REQUIRED.getMessage());
				}

				// 排除(跳過)簡答題；option type 是 text
				if (item.getType().equalsIgnoreCase(OptionType.TEXT.getType())) {
					continue;
				}
				// 排除option type 是單選，但答案有多個時
				// 題型是單選且答案長度大於1時
				if (item.getType().equalsIgnoreCase(OptionType.SINGLE_CHOICE.getType()) && answerArray.length > 1) {
					return new BasicRes(ResMessage.SHOOSE_ONE_ANSER.getCode(),
							ResMessage.SHOOSE_ONE_ANSER.getMessage());
				}

//				option選項分割選項分割
				String[] optionArray = item.getOptions().split(";");
//				分割之後再來比對
				List<String> optionList = List.of(optionArray);

//				把每個答案跟選項比對:就是比對答案跟該題選項是否一致
				for (String str : answerArray) {
					/*
					 * containsTest的用法 //假設item.getOptions()的值是"A;B;C;D" //假設answerArray=[A,B] //for
					 * 迴圈中就是把A和B對比是否包含在字串裡面，item.getOptions()中 //且要排除是text時 //contains
					 * 可以看test的containsTest的用法
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

//=======================================================================================================
	@Override
	public FeedbackRes feedback(FeedbackReq req) {

		ObjectMapper mapper = new ObjectMapper();
		// 從 QuizDao 中查詢 Quiz 對象
		Optional<Quiz> quiz = quizDao.findById(req.getQuizId());
		if (quiz.isEmpty()) {
			return new FeedbackRes(ResMessage.QUIZ_NOT_FOUND.getCode(), ResMessage.QUIZ_NOT_FOUND.getMessage());
		}
		Quiz quizGet = quiz.get();
//        取得List<Question>的字串
//        quiz(entity)的question是字串，但是題目Question有很多，所以將她轉為陣列
		String questionStr = quizGet.getQuestions();
//        再將他轉成list<question>
//        List<Question> quList=mapper.readValue(questionStr, new TypeReference<>() {});點一下轉try...catch
		try {
			List<Question> quList = mapper.readValue(questionStr, new TypeReference<>() {
			});

//			先把每個問題的編碼和題目放到quAnsMap中
			Map<Integer, Map<String, String>> quIdAndQuAnsMap = new HashMap<>();
//			lambda寫法
//			這裡的item就是  for(Question item:quList)中的item是一樣的
			quList.forEach(item -> {
				Map<String, String> quAndAnsMap = new HashMap<>();
				// 沒有答案所以先放null
				quAndAnsMap.put(item.getTitle(), null);
				quIdAndQuAnsMap.put(item.getId(), quAndAnsMap);
			});

//			撈取同一份問卷的回饋(再dao中定義完之後回來這裡撈取資料)，在這裡是因為req的id是與response是一樣的，都是問卷編碼
			List<Response> resList = responseDao.findByQuizId(req.getQuizId());
//			因為取出來資後會有很多回饋，所以要先遍歷每一份回饋
//			resList若是也用lambda寫法會再次產生出一個try...catch...
			for (Response item : resList) {
				// 取出問題反饋(在fillinReq中)
				// 題號 答案
				Map<Integer, String> qIdAnswerMap = mapper.readValue(item.getFillin(), new TypeReference<>() {
				});
				// lambda寫法
				// quIdAndQuAnsMap中的key是題號(與qIdAnswerMap的key值是相同的)，所以如果有，就可知是有填寫答案
//				quIdAndQuAnsMap: 包含所有題目的編號
//				qIdAnswerMap :只包含答案的編號(k)
				qIdAnswerMap.forEach((k, v) -> {
					if (quIdAndQuAnsMap.containsKey(k)) {
						// 透過k取出quIdAndQuAnsMap中的對應value
						// 這裡的map是"題目"與"答案"的map
						Map<String, String> map = quIdAndQuAnsMap.get(k);
						// 因為題目不變，不取代掉，所以key值維持
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
		 * ========= // 先將資料提取出來 String quizGetName = quizGet.getName(); String
		 * quizGetDescription = quizGet.getDescription(); LocalDate quizGetStartDate =
		 * quizGet.getStartDate(); LocalDate quizGetEndDate = quizGet.getEndDate(); // 從
		 * ResponseDao 中查詢 Response 對象列表 // List<Response> responses =
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

		// 從 QuizDao 中查詢 Quiz 對象
		Optional<Quiz> op = quizDao.findById(req.getQuizId());
		if (op.isEmpty()) {
			return new TextRes(ResMessage.QUIZ_NOT_FOUND.getCode(), ResMessage.QUIZ_NOT_FOUND.getMessage());
		}
		Quiz quiz = op.get();
		// 根據 quizId 查詢 Response 對象列表
		List<Response> responses = responseDao.findByQuizId(req.getQuizId());

		String quizTitle = quiz.getName();
		String quizDescription = quiz.getDescription();
		LocalDate quizStartDate = quiz.getStartDate();
		LocalDate quizEndDate = quiz.getEndDate();
		int code = ResMessage.SUCCESS.getCode();
		String message = ResMessage.SUCCESS.getMessage();

//        =====================================================
//		轉換(字串轉類別或類別轉字串)
		ObjectMapper mapper = new ObjectMapper();
//		從問卷中提出問題(原本類型是字串，酖他是一個很多很頓提所以轉乘list)
		String question = quiz.getQuestions();
//		List<Question> quList = mapper.readValue(question, new TypeReference<List<Question>>(){});
		List<Question> quList = null;
		
		try {
			quList = mapper.readValue(question, new TypeReference<List<Question>>(){});

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		// 設一個新的list將資料都放進來(也就對應了我們textRes的型態了)
		List<Text> textList = new ArrayList<>();

		// Response中包含了問題和答案
		for (Response response : responses) {
			//輸入者名子
			String fillinName = response.getName();
			String fillinPhone = response.getPhone();
			
			//答案在fillinList中
			String fillinStr = response.getFillin();
			List<Fillin> fillinList = null;
			try {
				fillinList = mapper.readValue(fillinStr, new TypeReference<>(){});
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

			//利便我的問題(因為問題Question中含有"題目id"及"題目")，如果只用Response我是取不到問卷裡面更詳細的資料
				for(Fillin ansItem:fillinList) {
					//在迴圈內新建一個新的text的目的是為了 : 讓每個text生成時都是獨立的，且裡面包含的元素在便利時都會加進來
					Text text = new Text();
					//因為我在我的Text中有設置set，所以值是可以更動的
					//然後再將我所需要的值get出來放入set
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
					
					//因為答案的key也是跟qustion的id走的
					//從Response中抓取Map<k(題目編號),v(答案)>
					//然後因為題目編號(key值)等於Question的id
//					String answer = fillinStr.get(item.getId);
					text.setAns(ansItem.getAnswer());

					//將填充好的text加到textList中(因為text不只一筆，所以才用list)
					textList.add(text);
				}
				
				
		}

		// 創建一個新的請求，將我的值都塞回去，這樣我的res才算完成
		//為甚麼textList還不算完成呢? 
		//因為textList沒有set回我的textRes，這樣會成[]，所以記得塞回去
		//因為我的請求是(public ""TextRes"" text(TextReq req))
		//如果我的請求變成了 Text 那我就可以直接返回那個list<Text>
		TextRes textRes = new TextRes();
		textRes.setCode(code);
		textRes.setMessage(message);
		textRes.setText(textList);
		//可以直接寫 return new TextRes(code,message,textList);(可頂替345~348、350)
		return textRes;
	}
}