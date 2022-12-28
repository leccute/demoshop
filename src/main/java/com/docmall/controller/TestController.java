package com.docmall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j;

//유효성 검사 시험 + 인터셉터 기능 연습하기 위한 컨트롤러
@Controller
@Log4j
@RequestMapping("/test/*")
public class TestController {

	/*시험 봤던,,, 유효성 검사 내용
	 
	 
	@GetMapping("/join")
	public void join() {
		
	}
	
	@PostMapping("/join")
	public String check() {
		
		return "redirect:/test/join";
	}
	
	 */
	
	//인터셉터 기능 연습하기 위한 컨트롤러
	
	@GetMapping("/testA")
	public void testA() {
		log.info("두번째 호출 메소드: testA");
	}
	
	@GetMapping("/testB")
	public void testB() {
		log.info("두번째 호출 메소드: testB");
	}
	
	@GetMapping("/testC")
	public void testC() {
		log.info("인터셉터 영향 안받는 호출 메소드: testC");
	}
}
