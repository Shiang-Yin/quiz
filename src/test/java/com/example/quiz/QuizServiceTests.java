package com.example.quiz;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import com.example.quiz.constants.OptionType;
import com.example.quiz.repository.QuizDao;
import com.example.quiz.service.ifs.QuizService;
import com.example.quiz.vo.BasicRes;
import com.example.quiz.vo.CreateReq;
import com.example.quiz.vo.Question;
import com.example.quiz.vo.updateOrCreateReq;

@SpringBootTest
public class QuizServiceTests {

	@Autowired
	private QuizService quizService;
	
	@Autowired
	private QuizDao quizDao;
	
	@Test
	public void createTest() {
		List<Question> questionList=new ArrayList<>();
		questionList.add(new Question(1,"健康餐?","松阪豬;炸豬排;煎魚;烤雞腿",//
				OptionType.SINGLE_CHOICE.getType(),true));
		questionList.add(new Question(2,"丹丹?","一號餐;二號餐;三號餐;四號餐",//
				OptionType.SINGLE_CHOICE.getType(),true));
		questionList.add(new Question(3,"炒飯?","豬肉炒飯;海鮮炒飯;干貝馬鈴薯;綜合炒飯",//
				OptionType.SINGLE_CHOICE.getType(),true));
		
		CreateReq req = new CreateReq("晚餐吃啥?","午餐吃啥?",LocalDate.of(2024, 6, 1),//
				LocalDate.of(2024, 6, 1),questionList,true);
		BasicRes res = quizService.create(req);
//		Assert.isTrue 有種反義的意思，也就是說，當我不等於200時，回傳false
//		所以Assert.isTrue(false,"message")中的false是"不為true時拋出異常"
//		測試的時候使用，可保留可不保留
//		完整測試就是"有多少邏輯判斷都需要做檢查"，完整測試又稱"單元測試unit"=>主要是要看邏輯是否有誤
		Assert.isTrue(res.getCode()==200, "create test false!!");
//		需要刪除測試資料，有兩種做法TODO
//		quizDao.deleteById(null);
		
	}
	
//	********檢查名子錯誤************
	@Test
	public void createNameErrorTest() {
		List<Question> questionList=new ArrayList<>();
		questionList.add(new Question(1,"健康餐?","松阪豬;炸豬排;煎魚;烤雞腿",//
				OptionType.SINGLE_CHOICE.getType(),true));
		questionList.add(new Question(2,"丹丹?","一號餐;二號餐;三號餐;四號餐",//
				OptionType.SINGLE_CHOICE.getType(),true));
		questionList.add(new Question(3,"炒飯?","豬肉炒飯;海鮮炒飯;干貝馬鈴薯;綜合炒飯",//
				OptionType.SINGLE_CHOICE.getType(),true));
		
		CreateReq req = new CreateReq("","午餐吃啥?",LocalDate.of(2024, 6, 1),//
				LocalDate.of(2024, 6, 1),questionList,true);
		BasicRes res = quizService.create(req);
//		Assert.isTrue 有種反義的意思，也就是說，當我不等於200時，回傳false
//		所以Assert.isTrue(false,true"message")中的false是"不為true時拋出異常"(true時顯示，"message")
//		測試的時候使用，可保留可不保留
//		Assert.isTrue(res.getCode()==400, "create test false!!");
//		這裡res.getMessage().equalsIgnoreCase("Param name error!!")中
//		的"Param name error!!"必須要跟ResMessage中的message是一樣的，只要有不一樣一樣會出錯喔~
		Assert.isTrue(res.getMessage().equalsIgnoreCase("Param name error!!"), //
				"create test false!!");
		
	}
	
	
//	********檢查日期錯誤************
	@Test
	public void createDateErrorTest() {
		List<Question> questionList=new ArrayList<>();
		questionList.add(new Question(1,"健康餐?","松阪豬;炸豬排;煎魚;烤雞腿",//
				OptionType.SINGLE_CHOICE.getType(),true));
		questionList.add(new Question(2,"丹丹?","一號餐;二號餐;三號餐;四號餐",//
				OptionType.SINGLE_CHOICE.getType(),true));
		questionList.add(new Question(3,"炒飯?","豬肉炒飯;海鮮炒飯;干貝馬鈴薯;綜合炒飯",//
				OptionType.SINGLE_CHOICE.getType(),true));
		
		CreateReq req = new CreateReq("午餐吃啥?","午餐吃啥?",LocalDate.of(2024,5, 30),//
				LocalDate.of(2024, 6, 30),questionList,true);
		BasicRes res = quizService.create(req);
//		Assert.isTrue 有種反義的意思，也就是說，當我不等於200時，回傳false
//		所以Assert.isTrue(false,"message")中的false是"不為true時拋出異常"
//		測試的時候使用，可保留可不保留
//		Assert.isTrue(res.getCode()==400, "create test false!!");
		Assert.isTrue(res.getMessage().equalsIgnoreCase("Param start Date not null!!"), //
				"create test false!!");
		
	}
	
//	合併大法~~~
	@Test
	public void createTest1() {
		List<Question> questionList=new ArrayList<>();
		questionList.add(new Question(1,"健康餐?","松阪豬;炸豬排;煎魚;烤雞腿",//
				OptionType.SINGLE_CHOICE.getType(),true));
		
//		測試name erroe		
		CreateReq req = new CreateReq("","午餐吃啥?",LocalDate.of(2024, 6, 1),//
				LocalDate.of(2024, 6, 1),questionList,true);
		BasicRes res = quizService.create(req);
		Assert.isTrue(res.getMessage().equalsIgnoreCase("Param name error!!"), //
				"create test false!!");
		
//		測試start date error
//		假設今天是2024/05/30，所以開始日期不能是今天 或者是今天之前
		req = new CreateReq("午餐吃啥?","午餐吃啥?",LocalDate.of(2024,5,30),//
				LocalDate.of(2024, 6, 1),questionList,true);
		res = quizService.create(req);
		Assert.isTrue(res.getMessage().equalsIgnoreCase("Param start Date error!!"), //
				"create test false!!");
		
//		測試end deate error
//		結束日期不能比開始日期早
		req = new CreateReq("午餐吃啥?","午餐吃啥?",LocalDate.of(2024,6,1),//
				LocalDate.of(2024,5, 1),questionList,true);
		res = quizService.create(req);
		Assert.isTrue(res.getMessage().equalsIgnoreCase("Param end Date error!!"), //
				"create test false!!");
		
//		如果邏輯判斷全都判斷完了之後，最後才會測試，"成功的情境"
		req = new CreateReq("吃啥?","吃啥?",LocalDate.of(2024, 6, 1),//
				LocalDate.of(2024, 6, 1),questionList,true);
		res = quizService.create(req);
		Assert.isTrue(res.getCode()==200, "create test false!!");
		
	}
	
	@Test
	public void createOrUpdateTest() {
		List<Question> questionList=new ArrayList<>();
		questionList.add(new Question(5,"健康餐?","松阪豬;炸豬排;煎魚;烤雞腿",//
				OptionType.SINGLE_CHOICE.getType(),true));
		
		quizService.updateOrCreate(new updateOrCreateReq("syso更新無id?","syso更新?",LocalDate.of(2024, 8, 1),//
				LocalDate.of(2024, 10, 1),questionList,true));
		
		System.out.println("=======================");
		
	}
}
