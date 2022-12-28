package com.docmall.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.docmall.kakaopay.ApproveResponse;
import com.docmall.kakaopay.ReadyResponse;

import lombok.extern.log4j.Log4j;

//카카오페이 API 서버에 실제 요청하는 작업
//서비스 인터페이스 작업 안함

@Service
@Log4j
public class KakaoPayServiceImpl {

	/*
	 카카오페이 API에 요청하기 위한 작업
	  1)HttpURLConnection을 이용한 HTTP 통신
	  2)RestTemplate를 이용한 통신(스프링에서 권장): https://minkwon4.tistory.com/178
	 */
	
	/* 첫번째 요청주소(결제 준비)
	 	1)요청 주소
	 	POST /v1/payment/ready HTTP/1.1
		Host: kapi.kakao.com
		2)헤더 정보
		Authorization: KakaoAK ${APP_ADMIN_KEY}
		Content-type: application/x-www-form-urlencoded;charset=utf-8 
	 */
	
	//3)요청에 필요한 파라미터 작업
	public ReadyResponse payReady(Long odr_code, String itemName, int quantity, String mem_id, int totalAmount) {
		
		//주문번호는 임의적으로 생성해도 된다. 주문테이블의 주문번호와는 상관이 없다. (테이블에서 가져오고 싶으면 시퀀스를 이용해서 가져오면 될 것.)
		//String order_id ="100"; //매번 카카오페이 결제시 주문번호를 생성.
		
		log.info("주문번호: " + odr_code);
		log.info("주문상품명:" + itemName);
		log.info("수량:" + quantity);
		log.info("아이디: " + mem_id);
		log.info("총금액: " + totalAmount);
		
		//카카오페이에 요청할 request 정보 구성 작업
		//컬렉션 클래스를 이용하여, 구성한다.
		//Map컬렉션 : 하나의 키에 하나의 값만 저장하는 특징. MultiValueMap: 하나의 키에 여러 값
		//Map<String, String> map = new LinkedHashMap<String, String>();
		
		//하나의 키에 여러개의 값을 저장하는 특징. 스프링에서 제공.
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
		parameters.add("cid", "TC0ONETIME"); //가맹점 코드, 10자.  제휴를 맺게 되면, 회사 담당자에게 문의를 하여 가맹점 코드를 변경한다.
		parameters.add("partner_order_id", String.valueOf(odr_code)); //가맹점 주문번호, 최대 100자
		parameters.add("partner_user_id", mem_id); //회원아이디, 최대 100자
		parameters.add("item_name", itemName); //상품명, 최대 100자
		parameters.add("quantity", String.valueOf(quantity)); //상품 수량
		parameters.add("total_amount", String.valueOf(totalAmount)); //상품 총액
		parameters.add("tax_free_amount", "0"); //상품 비과세 금액
		parameters.add("approval_url", "http://localhost:8888/order/orderApproval"); //결제 성공 시 redirect url, 최대 255자
		parameters.add("cancel_url", "http://localhost:8888/order/orderCancel"); //결제 취소 시 redirect url, 최대 255자
		parameters.add("fail_url", "http://localhost:8888/order/orderFail"); //결제 실패 시 redirect url, 최대 255자
		
		//https://jung-story.tistory.com/132
		
		//HttpEntity<T>: HttpHeader, HttpBody를 포함하는 특징.
		//HttpEntity<T> 클래스를 상속받아 구현한 클래스가 있다. RequestEntity, ResponseEntity
		//ResponseEntity 클래스 : HttpHeader, HttpBody, HttpStatus 3가지 정보를 관리.
		//헤더와 파라미터 정보를 구성												MultiValueMap<String, String> 들어가야 하는데, 반복이라 생략 가능		
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
		
		//외부 API통신
		RestTemplate template = new RestTemplate();
		//1차 요청주소
		String url = "https://kapi.kakao.com/v1/payment/ready";
		
		//postForObject메소드
		//파라미터 설명 : 첫번째 파라미터 : 카카오페이 요청주소, 두번째 파라미터: 요청시 사용할 파라미터와 헤더 정보 구성, 세번째 파라미터 : 리턴받는 클래스 정보
		//리턴값: 
		ReadyResponse readyResponse = template.postForObject(url, requestEntity, ReadyResponse.class);
		
		return readyResponse;
	}
	
	
	
	
	//HttpHeaders 클래스: 헤더 정보를 구성하는 기능.
	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "KakaoAK 30565ce56011b2577edae718f3afdb2b");
		headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		return headers;
	}
	
	
	/*두번째 요청(결제 승인 요청)
	    1)요청주소
	 	POST /v1/payment/approve HTTP/1.1
		Host: kapi.kakao.com
		2)헤더정보
		Authorization: KakaoAK ${APP_ADMIN_KEY}
		Content-type: application/x-www-form-urlencoded;charset=utf-8 
	 */
	
	public ApproveResponse payApprove(Long odr_code, String tid, String pg_token, String mem_id) {
		
		//String order_id = "100";
		
		//두번째 요청을 하기 위한 파라미터 작업
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
		parameters.add("cid", "TC0ONETIME"); //가맹점 코드, 10자
		parameters.add("tid", tid); //결제 고유번호, 결제 준비 API 응답에 포함
		parameters.add("partner_order_id", String.valueOf(odr_code)); //가맹점 주문번호, 결제 준비 API 요청과 일치해야 함
		parameters.add("partner_user_id", mem_id); //가맹점 회원 id, 결제 준비 API 요청과 일치해야 함
		parameters.add("pg_token", pg_token); //결제승인 요청을 인증하는 토큰. 사용자 결제 수단 선택 완료 시, approval_url로 redirection해줄 때 pg_token을 query string으로 전달
		
		//헤더와 파라미터 정보를 구성												MultiValueMap<String, String> 들어가야 하는데, 반복이라 생략 가능		
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
		
		//외부 API통신
		RestTemplate template = new RestTemplate();
		//2차 요청주소
		String url = "https://kapi.kakao.com/v1/payment/approve";
		
		ApproveResponse approveResponse = template.postForObject(url, requestEntity, ApproveResponse.class);
		
		return approveResponse;
	}
}
