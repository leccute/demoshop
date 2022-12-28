package com.docmall.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.docmall.domain.AdOrderDetailVOList;
import com.docmall.domain.OrderDetailProductVO;
import com.docmall.domain.OrderVO;
import com.docmall.dto.Criteria;
import com.docmall.dto.PageDTO;
import com.docmall.service.AdOrderService;
import com.docmall.util.FileUtils;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/admin/order/*")
public class AdOrderController {

	//서비스 주입
	@Setter(onMethod_ = {@Autowired})
	AdOrderService adOrderService;
	
	//업로드 폴더 주입
	@Resource(name = "uploadPath") //servlet-context.xml에 설정.
	private String uploadPath; //"C:\\JAVA\\ezen_dev\\upload\\goods\\"
	
	//주문목록. 클라이언트에서 페이지번호 클릭, 검색 클릭, 주문 상태 클릭(얘네들에 필요한 내용들이 파라미터에 있어야 함)
	@GetMapping("/orderList")
	public void orderList( //@ModelAttribute  파라미터로 들어 온 값을 jsp에서 가져다 쓰기 위해서.
						Criteria cri, 
						@ModelAttribute("startDate") String startDate,
						@ModelAttribute("endDate") String endDate,
						@ModelAttribute("odr_status") String odr_status, 
						Model model) {
		
		//목록이니까 페이징 기능.(검색기능 함께)
		List<OrderVO> orderList = adOrderService.getOrderList(cri, odr_status, startDate, endDate);
		model.addAttribute("orderList", orderList);
		
		//총 데이터 갯수 + 페이징 정보
		int TotalCount = adOrderService.getOrderTotalCount(cri, odr_status, startDate, endDate);
		model.addAttribute("pageMaker", new PageDTO(cri, TotalCount));
		
		//진행상태 카운트
		model.addAttribute("orderStatus1", adOrderService.orderStatusCount("주문접수"));
		model.addAttribute("orderStatus2", adOrderService.orderStatusCount("결제완료"));
		model.addAttribute("orderStatus3", adOrderService.orderStatusCount("배송준비중"));
		model.addAttribute("orderStatus4", adOrderService.orderStatusCount("배송처리"));
		model.addAttribute("orderStatus5", adOrderService.orderStatusCount("배송완료"));
		model.addAttribute("orderStatus6", adOrderService.orderStatusCount("주문취소"));
		model.addAttribute("orderStatus7", adOrderService.orderStatusCount("미주문"));
		model.addAttribute("orderStatus8", adOrderService.orderStatusCount("취소요청"));
		model.addAttribute("orderStatus9", adOrderService.orderStatusCount("취소완료"));
		model.addAttribute("orderStatus10", adOrderService.orderStatusCount("교환요청"));
		model.addAttribute("orderStatus11", adOrderService.orderStatusCount("교환완료"));
	}
	
	//주문 상태 변경
	@GetMapping("/orderStatusChange")
	@ResponseBody //서버의 결과를 클라이언트로 보낼 떄 사용 : ajax
	public ResponseEntity<String> orderStatusChange(@RequestParam("odr_status") String odr_status, @RequestParam("odr_code") Long odr_code) {
		ResponseEntity<String> entity = null;
		
		log.info("주문상태" + odr_code + odr_status);
		
		int result = adOrderService.orderStatusChange(odr_status, odr_code);
		
		log.info("결과: "+ result);
		
		if(result == 1) {
			entity = new ResponseEntity<String>("success", HttpStatus.OK);
		}else{
			entity = new ResponseEntity<String>("fail", HttpStatus.OK);
		}
		
		return entity; 
	}
	
	//선택 주문 상태 변경
	//클라이언트에서 ajax 방식으로 배열형태의 정보가 전송되었을 때, 스프링단의 메소드에서 파라미터 작업
	@PostMapping("/orderCheckedStatusChange")
	@ResponseBody
	public ResponseEntity<String> orderCheckedStatusChange(@RequestParam("odr_code_arr[]") List<Long>odr_code_arr,
			@RequestParam("odr_status_arr[]") List<String>odr_status_arr){
		
		log.info("선택주문번호: " + odr_code_arr);
		log.info("선택주문변경: " + odr_status_arr);
		
		ResponseEntity<String> entity = null;
		
		for(int i = 0; i < odr_code_arr.size(); i++) {
			adOrderService.orderStatusChange(odr_status_arr.get(i), odr_code_arr.get(i));
		}
		
		entity = new ResponseEntity<String>("success", HttpStatus.OK);
		
		return entity;
	}
	
	//클라이언트의 ajax 기능의 load()메서드로부터 요청받아 jsp의 실행 결과가 보내진다.
	//주문 내역 상세 보기 1
	@GetMapping("/orderDetail1")
	public void orderDetail1(Model model, Long odr_code) {
		
		//날짜 폴더가 windows 환경에 따라 2022\\11\\23 이렇게 되어 있고, \는 특수문자로 해석 불가능하여 이미지가 안나옴.
		//ProductVO에 있는 \를 (\한 번만 쓰면 sql 시퀀스 어쩌구에 따라 에러, 따라서 \\ 두 번 써줘야 함) /로 변경하면 제대로 나온다
		
		 List<AdOrderDetailVOList> odList = adOrderService.getOrderDetailList1(odr_code);
		 
		 odList.forEach(vo -> { //함수적 인터페이스(함수, 배열에 forEach 지원, for문도 사용 가능.)
			vo.setPdt_img_folder(vo.getPdt_img_folder().replace("\\", "/")); 
		});
		
		//주문상세정보. AdOrderDetailVOList
		model.addAttribute("odList1", odList);
		
		//결제정보. PaymentVO
		model.addAttribute("paymentVO", adOrderService.getPaymentVO(odr_code));
		
		//주문정보. OrderVO
		model.addAttribute("orderVO", adOrderService.getOrderVO(odr_code));
		
		//관리자메모.
		
		log.info("주문상세내역");
	}
	
	//주문 내역 상세 보기 2
	@GetMapping("/orderDetail2")
	public void orderDetail2(Model model, Long odr_code) {
		
		
		List<OrderDetailProductVO> odList = adOrderService.getOrderDetailList2(odr_code);
		
		odList.forEach(vo -> { //함수적 인터페이스(함수, 배열에 forEach 지원, for문도 사용 가능.)
			vo.getProductVO().setPdt_img_folder(vo.getProductVO().getPdt_img_folder().replace("\\", "/")); 
		});

		
		//주문상세정보. OrderDetailProductVO
		model.addAttribute("odList2", odList);
		
		//결제정보. PaymentVO
		model.addAttribute("paymentVO", adOrderService.getPaymentVO(odr_code));
		
		//주문정보. OrderVO
		model.addAttribute("orderVO", adOrderService.getOrderVO(odr_code));
		
		//관리자메모.
		
		log.info("주문상세내역");
	}
	
	//상품 목록에서 이미지 보여주기.
	@ResponseBody //ajax 형식
	@GetMapping("/displayFile")
	public ResponseEntity<byte[]> displayFile(String folderName, String fileName) throws IOException{
		
		//C:\\JAVA\\ezen_dev\\upload\\goods\\2022\\11\\22\\
		return FileUtils.getFile(uploadPath + folderName, fileName);
	}
	
	//관리자 메모
	@PostMapping("/pay_memo")
	@ResponseBody
	public ResponseEntity<String> pay_memo(String pay_memo, Integer pay_code){
		ResponseEntity<String> entity = null;
		
		//업데이트문 작업.
		adOrderService.pay_memo(pay_memo, pay_code);
		
		entity = new ResponseEntity<String>("success", HttpStatus.OK);
		return entity;
	}
	
	//주문 삭제
	@GetMapping("/orderDelete")
	public String orderDelete(Criteria cri, Long odr_code, RedirectAttributes rttr) {
		
		adOrderService.orderInfoDelete(odr_code);
		
		rttr.addFlashAttribute("msg", "정상 삭제 되었습니다.");
		
		return "redirect:/admin/order/orderList" + cri.getListLink();
	}
	
	//주문 상품 개별 삭제
	@GetMapping("/orderDetailProductDelete")
	public ResponseEntity<String> orderDetailProductDelete (Long odr_code,  Integer pdt_num, int odr_price){
		ResponseEntity<String> entity = null; 
				
		//작업
		adOrderService.orderDetailProductDelete(odr_code, pdt_num, odr_price);
		
		entity = new ResponseEntity<String>("success", HttpStatus.OK);
		
		return entity;
	}
	
}
