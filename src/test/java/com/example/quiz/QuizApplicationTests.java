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
		List<Quiz> res = quizDao.findByDescriptionContaining("金茶午");
		System.out.println(res.size());
	}
	
	@Test
	public void containsTest() {
		String str = "AB;B;C;D"; //選項
		String strAns = "A;AB;E" ; //答案
		String[] strArr = str.split(";"); //選項分割 ["AB" ,"B","C","D"]
		String[] strAnsArr = strAns.split(";"); //答案分割 ["A","AB","E"]
		List<String> strList=List.of(strArr);
		for(String item:strAnsArr) {
			System.out.println(item + ":" + strList.contains(item));
		}
	}
	
	@Test
	public void objectMapperTest() {
		
		String str = "[{\"id\":1,\"title\":\"純茶類\",\"options\":\"紅茶;綠茶;烏龍;青茶\",\"type\":\"單選\",\"is_necessary\":true}]";
		String strq = "{\"id\":1,\"title\":\"純茶類\",\"options\":\"紅茶;綠茶;烏龍;青茶\",\"type\":\"單選\",\"is_necessary\":true}";
		
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
			System.out.println("相等");
		}else {
			System.out.println("不相等");
		}
	}
	
	@Test
	public void text3() {
		List<String> list=List.of("A","B","C","D","E");
		String str="AAJBBBCEBEAADCDEIH";
		
		//方法一
		for(String item:list) {
			
			System.out.println(item);
			int oldCount = str.length();
			int newCount = str.replace(item, "").length();
			System.out.println(item+"出現"+(oldCount - newCount));
		}
		
		//方法二(還沒解完)
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
//		假設我現在有很多題的答案
		ansList.add("紅茶");
		ansList.add("綠茶");
		ansList.add("紅茶拿鐵");
		System.out.println(ansList);
		

		List<String> str=List.of("綠茶","綠茶拿鐵","紅茶","綠茶","紅茶拿鐵");
		List<String> list=new ArrayList<String>();
		for(String item:str) {
			list.add(item);
		}
		System.out.println(list);
		
		Map<String, Integer> countMap = new HashMap<>();
        
        // 計算出現次數
        for (String s : list) {
            countMap.put(s, countMap.getOrDefault(s, 0) + 1);
        }

        // 打印結果
        for (String s : ansList) {
            int count = countMap.getOrDefault(s, 0);
            System.out.println(s + " 出現 " + count + " 次");

        }
	}
}
