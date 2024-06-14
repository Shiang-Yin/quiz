package com.example.quiz.service.impl;

import java.time.LocalDate;

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
//		檢查參數
		BasicRes checkResult = checkParams(req);
//		方法一
//		checkResult==null時，表示參數檢查正確
		if(checkResult!=null) {
			return checkResult;
		}
//		方法二
		/*if(checkResult.getCode() != 200) {
			return checkResult;
		}*/
//	=======================================================================	
//		將CreateReq中的private List<Question> questionList;轉換成字串(String)
//		因為在Quiz中  @Column(name = "questions")private String questions;是字串(String)
//		透過 [objectMapper]可以把物件(類別)轉乘json格式字串
		ObjectMapper mapper=new ObjectMapper();
//		一開始打這串，會有紅蚯蚓，所以需要try/catch，點一下就可以自動幫我們轉換了
//		String questionStr = mapper.writeValueAsString(req.getQuestionList());
		try {
//			合併1、 將 questionStr帶入quiz中的questionStr，如54行
//			String questionStr = mapper.writeValueAsString(req.getQuestionList());
//			Quiz quiz=new Quiz(req.getName(),req.getDescription(),req.getStartDate(),
//					req.getEndDate(),questionStr,req.isPublished());
			Quiz quiz=new Quiz(req.getName(),req.getDescription(),req.getStartDate(),
					req.getEndDate(),mapper.writeValueAsString(req.getQuestionList()),
					req.isPublished());
//			合併2、 save中的quiz直接quiz那一大串替代，如58行
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
//		首先  先檢查問卷參數
//		因為是String，所以使用StringUtils.hasText(字串)
//		StringUtils.hasText(字串):會檢查字串是否為null，空字串，全空白字串，若是符合3種其中一種，會返回false
//		前面加一個 [ ! ] :表示反向意思，若字串檢查結果是false時，就會進入if的實作區塊
		if(!StringUtils.hasText(req.getName())) {
			return new BasicRes(ResMessage.PARAM_QUIZ_NAME_ERROR.getCode(),
					ResMessage.PARAM_QUIZ_NAME_ERROR.getMessage());
		}
		if(!StringUtils.hasText(req.getDescription())) {
			return new BasicRes(ResMessage.PARAM_DESCRIPTION_ERROR.getCode(),
					ResMessage.PARAM_DESCRIPTION_ERROR.getMessage());
		}
//		開始時間不能在今天(含)之前
//		(req中的開始時間不能早於[isBefore]今天(含[isEqual])之前)也可直接用isAfter(但要注意先後順序)
//		localDate.new()  是取得系統當前時間
// 		LocalDate.now(): 取得系統當前時間
// 		req.getStartDate().isBefore(LocalDate.now()): 若 req 中的開始時間"早"於當前時間，會得到 true
// 		req.getStartDate().isEqual(LocalDate.now()): 若 req 中的開始時間"等"於當前時間，會得到 true
// 		因為開始時間不能在今天(含)之前，所以上兩個比較若是任一個結果為 true，則表示開始時間要比當前(含)時間早
//		req.getStartDate().isAfter(LocalDate.now()) :若req 中開始時間比當前時間晚，會得到true
//		!req.getStartDate().isAfter(LocalDate.now()):在前面加上[ ! ]，表示，如果當前時間比當前時間晚
//		，則會得到fale，而其他時段都為true(小於當前時間的)
		if(req.getStartDate()==null || !req.getStartDate().isAfter(LocalDate.now()) ) {
			return new BasicRes(ResMessage.PARAM_STARDATE_ERROR.getCode(),
					ResMessage.PARAM_STARDATE_ERROR.getMessage());
		}
//		當程式碼執行至EndDate時，表示StartDate一定是大於當前時間(沒有等於喔~)
//		所以結束時間只需要，判斷，在開始時間之後就好(大於等於開始時間)
//		開始時間 > 當前時間 ; 結束時間 >= 開始時間  ==>  結束時間 >= 開始時間 > 當前時間
// 		1、結束時間不能小於等於當前時間  !req.getEndDate().isAfter(LocalDate.now()) 這個可以省略不寫
//		2、結束時間不能小(早)於開始時間		
		if(req.getEndDate()==null || req.getEndDate().isBefore(req.getStartDate())) {
			return new BasicRes(ResMessage.PARAM_ENDDATE_ERROR.getCode(),
					ResMessage.PARAM_ENDDATE_ERROR.getMessage());
		}
//		檢查問題的參數
		if(CollectionUtils.isEmpty(req.getQuestionList())) {
			return new BasicRes(ResMessage.PARAM_QUESTION_LIST_NOT_FOUND.getCode(),
					ResMessage.PARAM_QUESTION_LIST_NOT_FOUND.getMessage());
		}
//		一張問卷可能會有多個問題，所以要逐鼻檢查每一題的參數(所以用for)
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
//			type已經有做基本檢查了，但我們會因為類型不同而option也會有所不同
//			當Option_type 是單選或多選時，option就不能是空字串(以下檢查單/多選條件下，如為空字串返回錯誤)
//			但當option_type是文字時，則允許空字串
			if(item.getType().equals(OptionType.SINGLE_CHOICE.getType())
					|| item.getType().equals(OptionType.MULIT_CHOICE.getType())) {
				if(!StringUtils.hasText(item.getOptions())) {
					return new BasicRes(ResMessage.PARAM_QUESTION_CONTENT_ERROR.getCode(),
							ResMessage.PARAM_QUESTION_CONTENT_ERROR.getMessage());
				}
			}
//			也可這樣寫(單選多選的條件檢查)  條件一成立時，條件二也成立(所以要注意條件一的()以及條件二的&&)
//		  ********   ( 條件一(1) || 條件一(2) ) &&條件二   **************
/*		if( ( item.getType().equals(OptionType.SINGLE_CHOICE.getType())
					|| item.getType().equals(OptionType.MULIT_CHOICE.getType()) )
					&& !StringUtils.hasText(item.getOptions()) ) {
		
					return new BasicRes(ResMessage.PARAM_QUESTION_CONTENT_ERROR.getCode(),
							ResMessage.PARAM_QUESTION_CONTENT_ERROR.getMessage());
							
			}
 * 			
 */
			
		}
//		方法一
		return null;
//		方法二
		/*return new BasicRes(ResMessage.SUCCESS.getCode(),
				ResMessage.SUCCESS.getMessage());*/
	}

//	=================================================================
// *****************進***********入***********搜*********索******************
	@Override
	public SearchRes search(SearchReq req) {
		
//		先把list中的值提出來
		String name=req.getName();
		LocalDate start=req.getStartDate();
		LocalDate end=req.getEndDate();
//		提出後再做檢查
		if(!StringUtils.hasText(name)) {
			//要確保名字一定是空字串，不能有全空白字串或null，將此視為"沒有輸入條件值"
			//所以將它們都轉換為空字串""，並搜尋全部資訊
//			JPA的containing的方法，條件值是空字串時，會搜索全部
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
//		檢查參數
//		因為是集合(list)，所以要檢查時要使用CollectionUtils
		
		/*
		if(CollectionUtils.isEmpty(req.getIdList())) {
			return new BasicRes(ResMessage.DELETE_IS_NULL.getCode(),
					ResMessage.DELETE_IS_NULL.getMessage());
		}
//		刪除問卷
		quizDao.deleteAllById(req.getIdList());
		return new BasicRes(ResMessage.SUCCESS.getCode(),
				ResMessage.SUCCESS.getMessage());
				
				*/
		
//		另解
		
		if(!CollectionUtils.isEmpty(req.getIdList())) {
			try {
//				刪除問卷
				quizDao.deleteAllById(req.getIdList());
			}catch(Exception e){
				//當deleteAllById方法中，id的值不存在時，jpa會報錯
				//因為刪除之前，JPA會先"搜索"帶入的id，再去做刪除的動作(所以如果沒有搜索到就會報錯)
				//但因實際上也沒刪除任何資訊，所以也就可以不需要這個exception做處理
			}
		}
//		因為(1)無刪除資料(null)(2)輸入了資料不存在的id   ，都不會山到任何資訊，所以可以算是成功
		return new BasicRes(ResMessage.SUCCESS.getCode(),
				ResMessage.SUCCESS.getMessage());
		
	}

//	====================================================================================
//	*************新************增**********或************更***********新*******************
//	這段可以頂掉上面的create
	@Override
	public BasicRes updateOrCreate(updateOrCreateReq req) {
//		檢查參數
		BasicRes checkResult = checkParams(req);
//		方法一
//		checkResult==null時，表示參數檢查正確
		if(checkResult!=null) {
			return checkResult;
		}
		/* ObjectMapper mapper=new ObjectMapper();  這段幾乎固定不變的
		 * 盡量"不要"在每次序列化/反序列化使用時都 new ObjectMapper();，這樣的代價是昂貴的，
		 * 比起共用同一個實例，兩者的效能可以相差很多倍。
		 * ObjectMapper 是 thread-safe 的物件 ，用於物件的轉換
		 */
		//相關用法可詳閱下面連結網址的內容:
		//https://blog.csdn.net/u011213044/article/details/120329436
		ObjectMapper mapper=new ObjectMapper();
		try {
			//.writeValueAsString   : 將資料轉成字串型態
			String questionStr=mapper.writeValueAsString(req.getQuestionList());
			
//			Quiz quiz = new Quiz(req.getName(),req.getDescription(),
//					req.getStartDate(),req.getEndDate(),
//					questionStr,req.isPublished());    //寫法1
			
			if(req.getId()>0) {    
				// 可以與boo合併，變成  一段
				// if(req.getId()>0 && !quizDao.existsById(req.getId())) {
				// return new BasicRes(ResMessage.UPDATE_ID_NOT_FOUND.getCode(),
				// ResMessage.UPDATE_ID_NOT_FOUND.getMessage());
				// }
//  *************************************************************************************
//		以下兩種方式擇一(findById與existsById)
//		使用方法一(1、findById) ，入有資料，會回傳一整筆的資料(可能資料量會較龐大)
//		使用方法二(2、existsById) ，因為透過existsById來判斷存不存在，所以回傳的資料永遠都只會是一個bit(0(false)/1(true))
//	**************************************************************************************
				
				//1、透過findById 獲得為一值(PK)  ，若有值則回傳整筆資料
				/*----------------------------------
				 * 因為則一，所以將方法一先註解掉
				Optional<Quiz> op=quizDao.findById(req.getId());
				//判斷是否有資料
				if(op.isEmpty()) {
					return new BasicRes(ResMessage.UPDATE_ID_NOT_FOUND.getCode(),
							ResMessage.UPDATE_ID_NOT_FOUND.getMessage());
				}
				
				Quiz quiz=op.get();
				//設定新值(值從req來)
				//方法1:將req中的新值設定到舊的quiz中，不設定id，因為id一樣
				
				quiz.setName(req.getName());
				quiz.setDescription(req.getDescription());
				quiz.setStartDate(req.getStartDate());
				quiz.setEndDate(req.getEndDate());
				quiz.setQuestions(questionStr);
				quiz.setPublished(req.isPublished());
				--------------------------------------------*/
				
				//2、 透過existsById:回傳一個bit的值
//				boolean boo = quizDao.existsById(req.getId());
//				//如果boo不存在，則反傳無id
				//這邊要判斷從req帶進來的id是否真的存在於ob中
				//若不存在，後續程式碼在乎俏jpa的save方法時會變成新增
				//(不寫這段，在後續的操作中，找不到時可能會都直接新增，而沒有阻止我們繼續操作)
				if(!quizDao.existsById(req.getId())) {
					return new BasicRes(ResMessage.UPDATE_ID_NOT_FOUND.getCode(),
							ResMessage.UPDATE_ID_NOT_FOUND.getMessage());
				}
//				quiz.setId(req.getId()); //寫法1
			}	
				//方法2:因為是new一個新的quiz，所以要把id放進去
			
			//寫法2
			//這樣寫法是因為 SQL都先進行搜索(select)看看是否有id存在於OB中，如果有就逕行變更，如果沒有就新增
			Quiz quiz = new Quiz(req.getId(),req.getName(),req.getDescription(),
					req.getStartDate(),req.getEndDate(),
					questionStr,req.isPublished());   //移到if上面  
			//jpa這裡的save可以同時做到新增與更新(都會先進行搜索再作後續新增/新增)
			quizDao.save(quiz);
				
		} catch (JsonProcessingException e) {
			return new BasicRes(ResMessage.JSON_PROCESSING_EXCEPTION.getCode(),
					ResMessage.JSON_PROCESSING_EXCEPTION.getMessage());
		}
		
		return new BasicRes(ResMessage.SUCCESS.getCode(),
				ResMessage.SUCCESS.getMessage());
	}
	
	private BasicRes checkParams(updateOrCreateReq req) {
//		首先  先檢查問卷參數
		if(!StringUtils.hasText(req.getName())) {
			return new BasicRes(ResMessage.PARAM_QUIZ_NAME_ERROR.getCode(),
					ResMessage.PARAM_QUIZ_NAME_ERROR.getMessage());
		}
		if(!StringUtils.hasText(req.getDescription())) {
			return new BasicRes(ResMessage.PARAM_DESCRIPTION_ERROR.getCode(),
					ResMessage.PARAM_DESCRIPTION_ERROR.getMessage());
		}
//		開始時間不能在今天(含)之前
//		(req中的開始時間不能早於[isBefore]今天(含[isEqual])之前)也可直接用isAfter(但要注意先後順序)
		if(req.getStartDate()==null || !req.getStartDate().isAfter(LocalDate.now()) ) {
			return new BasicRes(ResMessage.PARAM_STARDATE_ERROR.getCode(),
					ResMessage.PARAM_STARDATE_ERROR.getMessage());
		}
//		檢查結束時間
		if(req.getEndDate()==null || req.getEndDate().isBefore(req.getStartDate())) {
			return new BasicRes(ResMessage.PARAM_ENDDATE_ERROR.getCode(),
					ResMessage.PARAM_ENDDATE_ERROR.getMessage());
		}
//		檢查問題的參數
		if(CollectionUtils.isEmpty(req.getQuestionList())) {
			return new BasicRes(ResMessage.PARAM_QUESTION_LIST_NOT_FOUND.getCode(),
					ResMessage.PARAM_QUESTION_LIST_NOT_FOUND.getMessage());
		}
//		一張問卷可能會有多個問題，所以要逐鼻檢查每一題的參數(所以用for)
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
//			type已經有做基本檢查了，但我們會因為類型不同而option也會有所不同
//			當Option_type 是單選或多選時，option就不能是空字串(以下檢查單/多選條件下，如為空字串返回錯誤)
//			但當option_type是文字時，則允許空字串
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
