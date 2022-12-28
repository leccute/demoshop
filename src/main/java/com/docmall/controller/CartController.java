package com.docmall.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.docmall.domain.CartVO;
import com.docmall.domain.CartVOList;
import com.docmall.domain.MemberVO;
import com.docmall.service.CartService;
import com.docmall.util.FileUtils;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/cart/*")
@Log4j
public class CartController {

	//주입
	@Setter(onMethod_ = {@Autowired})
	
	private CartService cartService;
	
	//업로드 폴더 주입
	@Resource(name = "uploadPath") //servlet-context.xml에 설정.
	private String uploadPath; //"C:\\JAVA\\ezen_dev\\upload\\goods\\"
	
	//장바구니 담기. 오라클 merge문법 https://gent.tistory.com/406
	/*동일한 사용자가 동일한 상품을 장바구니에 추가할 경우	1)장바구니 테이블에 정보가 존재하지 않으면 insert, 
	2)정보가 존재하면 데이터 수량 변경(업데이트)*/ 
	@ResponseBody
	@GetMapping("/cart_add")
	public ResponseEntity<String> cart_add(CartVO vo, HttpSession session){
		
	
		ResponseEntity<String> entity = null;
		
		//상품코드, 수량
		
		//세션: 로그인 사용자 정보
		String mem_id = ((MemberVO) session.getAttribute("loginStatus")).getMem_id();
		vo.setMem_id(mem_id);
		
		cartService.cart_add(vo);
		
		entity = new ResponseEntity<String>("success", HttpStatus.OK);
		

		
		return entity;
	}
	
	//장바구니 목록
	@GetMapping("/cart_list")
	public String cart_list(HttpSession session, Model model) {
		
		String mem_id = ((MemberVO) session.getAttribute("loginStatus")).getMem_id();
		
		
		List<CartVOList> cartList = cartService.cart_list(mem_id);
		cartList.forEach(vo -> { //함수적 인터페이스(함수, 배열에 forEach 지원, for문도 사용 가능.)
			vo.setPdt_img_folder(vo.getPdt_img_folder().replace("\\", "/")); 
		});
		model.addAttribute("cartList", cartList);
		
		return "/cart/cartList";
	}
	
	//상품 목록에서 이미지 보여주기.
	@ResponseBody //ajax 형식
	@GetMapping("/displayFile")
	public ResponseEntity<byte[]> displayFile(String folderName, String fileName) throws IOException{
		
		//C:\\JAVA\\ezen_dev\\upload\\goods\\2022\\11\\22\\
		return FileUtils.getFile(uploadPath + folderName, fileName);
	}
	
	//장바구니 수량 변경
	@ResponseBody
	@GetMapping("/cart_qty_change")
	public ResponseEntity<String> cart_qty_change(@RequestParam("cart_code") Long cart_code, @RequestParam("cart_amount") int cart_amount){
		
		ResponseEntity<String> entity = null;
		
		 cartService.cart_qty_change(cart_code, cart_amount);
		
		entity = new ResponseEntity<String>("success", HttpStatus.OK);
		
		return entity;
	}
	
	//장바구니 수량 삭제
	@GetMapping("/cart_delete")
	@ResponseBody
	public ResponseEntity<String> cart_delete(@RequestParam("cart_code") Long cart_code){
		
		ResponseEntity<String> entity = null;
		
		cartService.cart_delete(cart_code);
		
		entity = new ResponseEntity<String>("success", HttpStatus.OK);
		
		return entity;
	}
	
	//장바구니 비우기
	@GetMapping("/cart_empty")
	public String cart_empty(HttpSession session) {
		
		String mem_id = ((MemberVO) session.getAttribute("loginStatus")).getMem_id();
		cartService.cart_empty(mem_id);
		
		return "redirect:/cart/cart_list";
	}
	
	//선택 장바구니 삭제
	@ResponseBody
	@PostMapping("/cart_sel_delete")
	public ResponseEntity<String> cart_sel_delete(@RequestParam("cart_code_arr[]") List<Long>cart_code_arr){
		ResponseEntity<String> entity = null;
		
		//방법1. (개별 1개 삭제하는 걸, 체크 한 수 만큼 반복하기)
		/*
		for(int i = 0; i<cart_code_arr.size(); i++) {
			cartService.cart_delete(cart_code_arr.get(i));
		}
		*/
		
		//방법2. db 한번만 가져오면 되니까 성능이 더 좋다.mybatis foreach : https://java119.tistory.com/85
		cartService.cart_sel_delete(cart_code_arr);
		
		entity = new ResponseEntity<String>("success", HttpStatus.OK);
		
		return entity;
	}
	
	
}
