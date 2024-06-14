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
		questionList.add(new Question(1,"���d�\?","�Q����;���ޱ�;�γ�;�N���L",//
				OptionType.SINGLE_CHOICE.getType(),true));
		questionList.add(new Question(2,"����?","�@���\;�G���\;�T���\;�|���\",//
				OptionType.SINGLE_CHOICE.getType(),true));
		questionList.add(new Question(3,"����?","�ަת���;���A����;�z�����a��;��X����",//
				OptionType.SINGLE_CHOICE.getType(),true));
		
		CreateReq req = new CreateReq("���\�Yԣ?","���\�Yԣ?",LocalDate.of(2024, 6, 1),//
				LocalDate.of(2024, 6, 1),questionList,true);
		BasicRes res = quizService.create(req);
//		Assert.isTrue ���ؤϸq���N��A�]�N�O���A��ڤ�����200�ɡA�^��false
//		�ҥHAssert.isTrue(false,"message")����false�O"����true�ɩߥX���`"
//		���ժ��ɭԨϥΡA�i�O�d�i���O�d
//		������մN�O"���h���޿�P�_���ݭn���ˬd"�A������դS��"�椸����unit"=>�D�n�O�n���޿�O�_���~
		Assert.isTrue(res.getCode()==200, "create test false!!");
//		�ݭn�R�����ո�ơA����ذ��kTODO
//		quizDao.deleteById(null);
		
	}
	
//	********�ˬd�W�l���~************
	@Test
	public void createNameErrorTest() {
		List<Question> questionList=new ArrayList<>();
		questionList.add(new Question(1,"���d�\?","�Q����;���ޱ�;�γ�;�N���L",//
				OptionType.SINGLE_CHOICE.getType(),true));
		questionList.add(new Question(2,"����?","�@���\;�G���\;�T���\;�|���\",//
				OptionType.SINGLE_CHOICE.getType(),true));
		questionList.add(new Question(3,"����?","�ަת���;���A����;�z�����a��;��X����",//
				OptionType.SINGLE_CHOICE.getType(),true));
		
		CreateReq req = new CreateReq("","���\�Yԣ?",LocalDate.of(2024, 6, 1),//
				LocalDate.of(2024, 6, 1),questionList,true);
		BasicRes res = quizService.create(req);
//		Assert.isTrue ���ؤϸq���N��A�]�N�O���A��ڤ�����200�ɡA�^��false
//		�ҥHAssert.isTrue(false,true"message")����false�O"����true�ɩߥX���`"(true����ܡA"message")
//		���ժ��ɭԨϥΡA�i�O�d�i���O�d
//		Assert.isTrue(res.getCode()==400, "create test false!!");
//		�o��res.getMessage().equalsIgnoreCase("Param name error!!")��
//		��"Param name error!!"�����n��ResMessage����message�O�@�˪��A�u�n�����@�ˤ@�˷|�X����~
		Assert.isTrue(res.getMessage().equalsIgnoreCase("Param name error!!"), //
				"create test false!!");
		
	}
	
	
//	********�ˬd������~************
	@Test
	public void createDateErrorTest() {
		List<Question> questionList=new ArrayList<>();
		questionList.add(new Question(1,"���d�\?","�Q����;���ޱ�;�γ�;�N���L",//
				OptionType.SINGLE_CHOICE.getType(),true));
		questionList.add(new Question(2,"����?","�@���\;�G���\;�T���\;�|���\",//
				OptionType.SINGLE_CHOICE.getType(),true));
		questionList.add(new Question(3,"����?","�ަת���;���A����;�z�����a��;��X����",//
				OptionType.SINGLE_CHOICE.getType(),true));
		
		CreateReq req = new CreateReq("���\�Yԣ?","���\�Yԣ?",LocalDate.of(2024,5, 30),//
				LocalDate.of(2024, 6, 30),questionList,true);
		BasicRes res = quizService.create(req);
//		Assert.isTrue ���ؤϸq���N��A�]�N�O���A��ڤ�����200�ɡA�^��false
//		�ҥHAssert.isTrue(false,"message")����false�O"����true�ɩߥX���`"
//		���ժ��ɭԨϥΡA�i�O�d�i���O�d
//		Assert.isTrue(res.getCode()==400, "create test false!!");
		Assert.isTrue(res.getMessage().equalsIgnoreCase("Param start Date not null!!"), //
				"create test false!!");
		
	}
	
//	�X�֤j�k~~~
	@Test
	public void createTest1() {
		List<Question> questionList=new ArrayList<>();
		questionList.add(new Question(1,"���d�\?","�Q����;���ޱ�;�γ�;�N���L",//
				OptionType.SINGLE_CHOICE.getType(),true));
		
//		����name erroe		
		CreateReq req = new CreateReq("","���\�Yԣ?",LocalDate.of(2024, 6, 1),//
				LocalDate.of(2024, 6, 1),questionList,true);
		BasicRes res = quizService.create(req);
		Assert.isTrue(res.getMessage().equalsIgnoreCase("Param name error!!"), //
				"create test false!!");
		
//		����start date error
//		���]���ѬO2024/05/30�A�ҥH�}�l�������O���� �Ϊ̬O���Ѥ��e
		req = new CreateReq("���\�Yԣ?","���\�Yԣ?",LocalDate.of(2024,5,30),//
				LocalDate.of(2024, 6, 1),questionList,true);
		res = quizService.create(req);
		Assert.isTrue(res.getMessage().equalsIgnoreCase("Param start Date error!!"), //
				"create test false!!");
		
//		����end deate error
//		������������}�l�����
		req = new CreateReq("���\�Yԣ?","���\�Yԣ?",LocalDate.of(2024,6,1),//
				LocalDate.of(2024,5, 1),questionList,true);
		res = quizService.create(req);
		Assert.isTrue(res.getMessage().equalsIgnoreCase("Param end Date error!!"), //
				"create test false!!");
		
//		�p�G�޿�P�_�����P�_���F����A�̫�~�|���աA"���\������"
		req = new CreateReq("�Yԣ?","�Yԣ?",LocalDate.of(2024, 6, 1),//
				LocalDate.of(2024, 6, 1),questionList,true);
		res = quizService.create(req);
		Assert.isTrue(res.getCode()==200, "create test false!!");
		
	}
	
	@Test
	public void createOrUpdateTest() {
		List<Question> questionList=new ArrayList<>();
		questionList.add(new Question(5,"���d�\?","�Q����;���ޱ�;�γ�;�N���L",//
				OptionType.SINGLE_CHOICE.getType(),true));
		
		quizService.updateOrCreate(new updateOrCreateReq("syso��s�Lid?","syso��s?",LocalDate.of(2024, 8, 1),//
				LocalDate.of(2024, 10, 1),questionList,true));
		
		System.out.println("=======================");
		
	}
}
