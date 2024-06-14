package com.example.quiz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.quiz.entity.Quiz;
import com.example.quiz.repository.QuizDao;
import com.example.quiz.vo.Question;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class QuizApplicationTests {

	@Autowired
	private QuizDao quizDao;
	
	@Test
	void contextLoads() {
	}

	@Test
	public void test1() {
		List<Quiz> res = quizDao.findByDescriptionContaining("������");
		System.out.println(res.size());
	}
	
	@Test
	public void containsTest() {
		String str = "AB;B;C;D"; //�ﶵ
		String strAns = "A;AB;E" ; //����
		String[] strArr = str.split(";"); //�ﶵ���� ["AB" ,"B","C","D"]
		String[] strAnsArr = strAns.split(";"); //���פ��� ["A","AB","E"]
		List<String> strList=List.of(strArr);
		for(String item:strAnsArr) {
			System.out.println(item + ":" + strList.contains(item));
		}
	}
	
	@Test
	public void objectMapperTest() {
		
		String str = "[{\"id\":1,\"title\":\"�¯���\",\"options\":\"����;���;�Q�s;�C��\",\"type\":\"���\",\"is_necessary\":true}]";
		String strq = "{\"id\":1,\"title\":\"�¯���\",\"options\":\"����;���;�Q�s;�C��\",\"type\":\"���\",\"is_necessary\":true}";
		
		ObjectMapper mapper = new ObjectMapper();
//		List<Question> list = mapper.readValue(str, List.class);
		try {
//			q=mapper
			
			List<Question> list = mapper.readValue(str, List.class);
			
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void listText() {
		List<Integer> aList=new ArrayList<>();
		aList.add(1);
		aList.add(2);
		aList.add(3);
		List<Integer> bList=new ArrayList<>();
		bList.add(1);
		bList.add(2);
		bList.add(3);
		
		
		System.out.println(aList);
		System.out.println(bList);
		
		if(aList.equals(bList)) {
			System.out.println("�۵�");
		}else {
			System.out.println("���۵�");
		}
	}
	
	@Test
	public void text3() {
		List<String> list=List.of("A","B","C","D","E");
		String str="AAJBBBCEBEAADCDEIH";
		
		//��k�@
		for(String item:list) {
			
			System.out.println(item);
			int oldCount = str.length();
			int newCount = str.replace(item, "").length();
			System.out.println(item+"�X�{"+(oldCount - newCount));
		}
		
		//��k�G(�٨S�ѧ�)
////		Map<Character, Long> charCountMap = new HashMap<>();
//		Map<Character, Long> charCountMap = str.chars()
//		        .mapToObj(c -> (char) c)
//		        .filter(list::contains)
//		        .collect(Collectors.groupingBy(c -> c, Collectors.counting()));
//		
//		list.forEach(ch -> 
//		System.out.println("Character " + ch + " appears " +//
//		charCountMap.getOrDefault(ch, 0L) +// 
//				" times in the string.")
//		);
	}
	
	@Test
	public void text4() {
		ObjectMapper mapper = new ObjectMapper();
		
		List<String> ansList=new ArrayList<String>();
//		���]�ڲ{�b���ܦh�D������
		ansList.add("����");
		ansList.add("���");
		ansList.add("�������K");
		System.out.println(ansList);
		

		List<String> str=List.of("���","������K","����","���","�������K");
		List<String> list=new ArrayList<String>();
		for(String item:str) {
			list.add(item);
		}
		System.out.println(list);
		
		Map<String, Integer> countMap = new HashMap<>();
        
        // �p��X�{����
        for (String s : list) {
            countMap.put(s, countMap.getOrDefault(s, 0) + 1);
        }

        // ���L���G
        for (String s : ansList) {
            int count = countMap.getOrDefault(s, 0);
            System.out.println(s + " �X�{ " + count + " ��");

        }
	}
}
