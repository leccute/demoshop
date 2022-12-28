package com.docmall.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.docmall.domain.CartVO;
import com.docmall.domain.CartVOList;
import com.docmall.domain.MemberVO;
import com.docmall.domain.OrderDetailProductVO;
import com.docmall.domain.OrderDetailVO;
import com.docmall.domain.OrderVO;
import com.docmall.domain.PaymentVO;
import com.docmall.dto.Criteria;
import com.docmall.dto.PageDTO;
import com.docmall.kakaopay.ApproveResponse;
import com.docmall.kakaopay.ReadyResponse;
import com.docmall.service.CartService;
import com.docmall.service.KakaoPayServiceImpl;
import com.docmall.service.OrderService;
import com.docmall.util.FileUtils;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/order/*")
public class OrderController {

	//service 주입
	@Setter(onMethod_ = {@Autowired})
	private OrderService orderService;
	
	//업로드 폴더 주입
	@Resource(name = "uploadPath") //servlet-context.xml에 설정.
	private String uploadPath; //"C:\\JAVA\\ezen_dev\\upload\\goods\\"
	
	//장바구니서비스 주입
	@Setter(onMethod_ = {@Autowired})
	private CartService cartService;
	
	//카카오페이 요청 정보 주입
	@Setter(onMethod_ = {@Autowired})
	private KakaoPayServiceImpl kakaoPayServiceImpl;
	
	//주문할 내역 정보 + 주문작성하기
	//1)상품리스트-바로 주문(장바구니 사용 안함)  2)장바구니 주문
	//바로주문주소 : /order/orderListInfo?pdt_num4&odr_amount1&type=direct
	//장바구니 주문 주소 : /order/orderListInfo?type=cart
	@GetMapping("/orderListInfo")      
	public void orderListInfo(@ModelAttribute("type") String type, @RequestParam(value = "pdt_num", required = false) Integer pdt_num, 
						@RequestParam(value = "odr_amount", required = false) Integer odr_amount, HttpSession session, Model model) {
		//pdt_num, odr_amount VO에서는 int로 했는데, 여기선 Integer: 장바구니 주문에서는 넘어오지 않는 파라미터, 기본타입은 null일때 에러/참조타입이어야 에러 안남.
		//@RequestParam(value = "odr_amount", required = false) 이렇게 required = false 해놔도 int로 해두면 에러난다. (기본값은 true)
		
		//상품 목록 : 바로구매 / 장바구니 : 주문하기 || 두 개의 주문서 작성 페이지를 똑같이 구성해도 되고, 다르게 구성해도 되고.
		//주문내역: 총금액 || DB에서 가져올 수도 있고, 장바구니창에서 가져올 수도 있고. 어떻게 가져와서 보여줄지 고민해봐야 한다.
		
		String mem_id = ((MemberVO)session.getAttribute("loginStatus")).getMem_id();
		
		List<CartVOList> orderList = null;
		
		
		
		if(type.equals("direct")) { //바로 주문  Integer pdt_num, Integer odr_amount
			
			//amount 등을 이용하기 위해 만든다.
			CartVO cartVO = new CartVO(null, pdt_num, mem_id, odr_amount);
			
			CartVOList cartVOList = orderService.directOrder(cartVO);
			
			orderList = new ArrayList<>();
			orderList.add(cartVOList);
			
			orderList.forEach(vo -> { //함수적 인터페이스(함수, 배열에 forEach 지원, for문도 사용 가능.)
				vo.setPdt_img_folder(vo.getPdt_img_folder().replace("\\", "/")); 
			});
			
		}else if(type.equals("cart")) { //장바구니
			orderList = orderService.cart_list(mem_id);
		
			orderList.forEach(vo -> { //함수적 인터페이스(함수, 배열에 forEach 지원, for문도 사용 가능.)
				vo.setPdt_img_folder(vo.getPdt_img_folder().replace("\\", "/")); 
			});
		}
		
		
		
		//주문내역
		model.addAttribute("orderList", orderList);
	}
	
	//상품 목록에서 이미지 보여주기.
	@ResponseBody //ajax 형식
	@GetMapping("/displayFile")
	public ResponseEntity<byte[]> displayFile(String folderName, String fileName) throws IOException{
		
		//C:\\JAVA\\ezen_dev\\upload\\goods\\2022\\11\\22\\
		return FileUtils.getFile(uploadPath + folderName, fileName);
	}
	
	//1)클라이언트에서 카카오페이 결제버튼 클릭하면   // 서버측에서 보내는 데이터가 json 형식이다 라고 알려주는.
	@GetMapping(value = "/orderPay", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}) //"application/json" 이렇게 써도 된다.
	public @ResponseBody ReadyResponse payReady(String type, OrderVO o_vo, PaymentVO p_vo, OrderDetailVO od_vo, int totalamount, HttpSession session, Model model) {
	//OrderDetailVO(결제하기누르면 나오는 정보들을 넣는 테이블, 디테일 테이블에 맞게끔)를 만들어서, 여기서 파라미터로 받아와야 한다. 그리고 어떻게 이용하라는 건데 일단... 몰겟다.
	//카카오페이 결제 버튼을 누르면, 1차 요청-응답 / 2차 요청-응답 에 필요한 모든 정보를 받아와야 한다.
		
		
		log.info("주문정보: " + o_vo);
		log.info("결제 정보: " + p_vo);
		log.info("총 주문 금액: " + totalamount);
		
		
		//주문번호가 시퀀스 작업(중요)
		o_vo.setOdr_code(orderService.getOrderSequence());
		
		
		String mem_id = ((MemberVO)session.getAttribute("loginStatus")).getMem_id();
		
		//장바구니 내역 불러오기.(1차 요청하기 위해 필요, 주입 필요: private CartService cartService;)
		//바로구매에서는 장바구니를 사용하지 않는다.
		List<CartVOList> cartList = cartService.cart_list(mem_id);
		
		//땡땡땡 상품 외 5개(카카오페이 결제 상품명)
		//String itemName = cartList.get(0).getPdt_num() + " 외 " + String.valueOf(cartList.size()-1) + "개";
		String itemName = "";
		int quantity = 0;
		
		//주문번호가 시퀀스 작업(중요)
		od_vo.setOdr_code(o_vo.getOdr_code());
		
		o_vo.setMem_id(mem_id);
		p_vo.setMem_id(mem_id);
		p_vo.setPay_nobank_user("");//얘와..  무통장 결제 부분 때문에 sql구문에 추가했는데, 카카오 결제로 진행할 땐 이 둘은 null 처리되어 에러가 된다.
		p_vo.setPay_nobank("");//얘는.. 따라서, 카카오 페이일 때, 공백값을 설정하여 null이 떨어지지 않게 작업한 것.
		od_vo.setOdr_code(o_vo.getOdr_code());
		
		//주문정보 저장하기(DB 작업) 여기다가 작업을 해야, DB에 문제가 생겨서 1차 승인이 되지 않았을 때 "결제완료" 이런 메세지가 안간다.
		//헐 근데, 에러가 나서 결제하기가 아예 안떴는데 DB작업이 됐고, 장바구니도 지워짐. 이럴 땐 update, delete해야 하지 않나
		//이 작업을 안하면, 아까처럼 밑에서 작업하는 게 더 나은데... 이 작업은 어떻게 해야하쥐,,,,?
		//일단, sql구문을 잘 만들어서 에러 안나게 해보자....
		if(type.equals("cart")) {//장바구니 구매(주문 상세 정보를 장바구니 참조하여, 확보)
			//1)주문 테이블 삽입, 2-1)주문 상세 테이블 삽입, 3)결제 테이블 삽입, 4)장바구니 삭제
			orderService.orderSaveWithCart(o_vo, p_vo);
			
		}else if(type.equals("direct")) {//바로구매(주문 상세 정보를 화면단(jsp)에서 받아서, 확보)
			//1)주문 테이블 삽입, 2-2)주문 상세 테이블 삽입, 3)결제 테이블 삽입
			orderService.orderSaveDirect(o_vo, p_vo, od_vo); //OrderDetailVO 준비할 것.
		}
		
		
		if(type.equals("cart")) {
			
			for(int i = 0; i < cartList.size(); i++) {
				itemName += cartList.get(i).getPdt_name() + ", ";
			}
			
			itemName = itemName.substring(0, itemName.lastIndexOf(",") - 1); //마지막 콤마 이전까지.(근데 마지막 콤마 나오던데,,)
			quantity = cartList.size();
			
		}else if(type.equals("direct")) {
			itemName = "개별상품";
			quantity = 1;
		}
		
		
		//카카오페이 서버에 1차 준비 요청 
		ReadyResponse readyResponse = kakaoPayServiceImpl.payReady(o_vo.getOdr_code(), itemName, quantity, mem_id, totalamount);
		
		log.info("결제 고유 번호: " + readyResponse.getTid());
		log.info("결제 요청 URL: " + readyResponse.getNext_redirect_pc_url()); //카카오페이 API서버에서 보내오는 url
		
		
		session.setAttribute("tid", readyResponse.getTid());
		session.setAttribute("odr_code", o_vo.getOdr_code());
		
		return readyResponse; //jsp - ajax.
	}
	
	//2)ajax 요청이 성공된 후에 호출되는 주소
	//결제 승인 요청
	@GetMapping("/orderApproval")
	public String orderApproval(@RequestParam("pg_token") String pg_token, HttpSession session) {
		
		String mem_id = ((MemberVO)session.getAttribute("loginStatus")).getMem_id();
		
		String tid = (String)(session.getAttribute("tid"));
		Long odr_code = (Long)(session.getAttribute("odr_code"));
		
		ApproveResponse approveResponse = kakaoPayServiceImpl.payApprove(odr_code, tid, pg_token, mem_id);
		//카카오페이에서 넘어 온 정보를 DB에 저장해야 할 경우. -> update 작업 
		//148줄에서 이미 DB작업을 해놨기 때문에, 결제가 이루어지면 넘어오는 정보인  approveResponse 얘를 이미 DB에 저장된 내용에 업데이트 해줘야 한다. 
		
		
		//세션으로 저장한(받아 온) 정보는 사용 후 즉시 소멸시킨다.
		session.removeAttribute("tid");
		session.removeAttribute("odr_code");
		
		//여기다가 작업하면, 코드의 이상으로 db 작업은 이루어지지 않아도 결제 완료라는 메세지가 온다.
//		if(type.equals("cart")) {//장바구니 구매(주문 상세 정보를 장바구니 참조하여, 확보)
//			//1)주문 테이블 삽입, 2-1)주문 상세 테이블 삽입, 3)결제 테이블 삽입, 4)장바구니 삭제
//			orderService.orderSaveWithCart(o_vo, p_vo);
//			
//		}else if(type.equals("direct")) {//바로구매(주문 상세 정보를 화면단(jsp)에서 받아서, 확보)
//			//1)주문 테이블 삽입, 2-2)주문 상세 테이블 삽입, 3)결제 테이블 삽입
//			orderService.orderSaveDirect(o_vo, p_vo, od_vo); //OrderDetailVO 준비할 것.
//		}
		
		return "redirect:/order/orderComplete";
	}
	
	//무통장 결제(결제방법이 무통장결제가 되면, 주문정보 저장/ 바로가기주문.카트주문 / 결제정보 저장하기, return까지.)
	@PostMapping("/orderSave")
	public String orderSave(String type, OrderVO o_vo, PaymentVO p_vo, OrderDetailVO od_vo, HttpSession session, Model model) {
		
		p_vo.setPay_tot_price(p_vo.getPay_nobank_price());
		
		log.info("주문정보: " + o_vo);
		log.info("결제 정보: " + p_vo);
		
		
		//주문번호가 시퀀스 작업(중요)
		o_vo.setOdr_code(orderService.getOrderSequence());
		
		String mem_id = ((MemberVO)session.getAttribute("loginStatus")).getMem_id();
		
		//주문번호가 시퀀스 작업(중요)
		//od_vo.setOdr_code(o_vo.getOdr_code());
		
		o_vo.setMem_id(mem_id);
		p_vo.setMem_id(mem_id);
		od_vo.setOdr_code(o_vo.getOdr_code());
		
		//주문정보 저장하기(DB 작업) 여기다가 작업을 해야, DB에 문제가 생겨서 1차 승인이 되지 않았을 때 "결제완료" 이런 메세지가 안간다.
		//헐 근데, 에러가 나서 결제하기가 아예 안떴는데 DB작업이 됐고, 장바구니도 지워짐. 이럴 땐 update, delete해야 하지 않나
		//이 작업을 안하면, 아까처럼 밑에서 작업하는 게 더 나은데... 이 작업은 어떻게 해야하쥐,,,,?
		//일단, sql구문을 잘 만들어서 에러 안나게 해보자....
		if(type.equals("cart")) {//장바구니 구매(주문 상세 정보를 장바구니 참조하여, 확보)
			//1)주문 테이블 삽입, 2-1)주문 상세 테이블 삽입, 3)결제 테이블 삽입, 4)장바구니 삭제
			orderService.orderSaveWithCart(o_vo, p_vo);
			
		}else if(type.equals("direct")) {//바로구매(주문 상세 정보를 화면단(jsp)에서 받아서, 확보)
			//1)주문 테이블 삽입, 2-2)주문 상세 테이블 삽입, 3)결제 테이블 삽입
			orderService.orderSaveDirect(o_vo, p_vo, od_vo); //OrderDetailVO 준비할 것.
		}
		
		return "redirect:/order/orderComplete";
	}
	
	
	//결제 성공시 (클라이언트에게 보여줄 결제 및 주문이 완료되었습니다.)
	@GetMapping("/orderComplete")
	public void orderComplete() {
		
	}
	
	//결제 취소시
	@GetMapping("/orderCancel")
	public void orderCancel() {
		
	}
	
	//결제 실패시
	@GetMapping("/orderFail")
	public void orderFail() {
		
	}
	
	//주문 내역
	@GetMapping("/orderList")
	public void orderList(HttpSession session, Model model, @ModelAttribute("cri") Criteria cri) {
		String mem_id = ((MemberVO) session.getAttribute("loginStatus")).getMem_id();
		
		//목록이니까 페이징 기능
		List<OrderVO> orderList = orderService.getOrderList(mem_id, cri);
		model.addAttribute("orderList", orderList);
		
		//총 데이터 갯수 + 페이징 정보
		int TotalCount = orderService.getOrderTotalCount(mem_id);
		model.addAttribute("pageMaker", new PageDTO(cri, TotalCount));
	}
	
	//주문 상세orderDetail
	@GetMapping("/orderDetail")
	public void orderDetail(Model model, Long odr_code) {
		
		
		List<OrderDetailProductVO> odList = orderService.getOrderDetailList(odr_code);
		
		odList.forEach(vo -> { //함수적 인터페이스(함수, 배열에 forEach 지원, for문도 사용 가능.)
			vo.getProductVO().setPdt_img_folder(vo.getProductVO().getPdt_img_folder().replace("\\", "/")); 
		});

		model.addAttribute("odList", odList);
		
	
	}

}



