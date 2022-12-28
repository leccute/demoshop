package com.docmall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.docmall.domain.CategoryVO;
import com.docmall.service.ProductService;

import lombok.extern.log4j.Log4j;


/*
 @ControllerAdvice: 공통 모델 작업과 Exception(예외처리)에 사용된다.
 
  com.docmall.controller 패키지 안에 존재하는 컨트롤러의 매핑 주소 요청을 받으면, categoryList 메소드가 자동으로 호출된다.
  
   어떤 컨트롤러에서든 1차 카테고리 목록을 읽어오는 작업이 필요할 때, 각 컨트롤러에 들어가서 각각 작업하는 게 아니라 여기서 한 번 만들어서 가져다 쓴다. 
   따라서, 유지 보수에 좋다.
 */
@Log4j
@ControllerAdvice(basePackages = {"com.docmall.controller"}) //@ControllerAdvice가 @Component를 달고 있어서 bean 생성 된다.
public class GlobalControllerAdvice {

	//서비스 주입 작업
	@Autowired
	private ProductService productService;
	
	
	//1차 카테고리 목록을 읽어오는 작업.(관리자 쪽과 같은 코드이지만, 관리자/사용자 나눠서 관리하기 위해 똑같은 거 다시 반복)
	@ModelAttribute //얘는 필수. 얘를 빼면, jsp에서 가져다 쓸 수 없다. 파라미터에 Model이 들어가 있을지라도.
	public void categoryList(Model model) {
		
		log.info("1차 카테고리 정보");
		
		List<CategoryVO> cateList = productService.getCategoryList();
		model.addAttribute("mainCateList", cateList);
	}
	
}
