package com.docmall.domain;

import java.util.Date;

import lombok.Data;

//주문자 정보와 배송지 정보
@Data
public class OrderVO {
	
	private Long odr_code; //mapper에서 시퀀스값이 들어가도록 실행할 건데, 같은 시퀀스를 여러 군데에서 사용할 것이기 때문에, selectKey로 설정해서 미리 넣어준다.
	
	private String mem_id; //세션: 로그인아이디
	
	private String odr_name; 
	private String odr_zipcode; 
	private String odr_addr; 
	private String odr_addr_d; 
	private String odr_phone; 
	private int odr_total_price; 
	private Date odr_date;
	
	private String odr_status; // 주문상태 : 배송상태
	private String payment_status; // 결제상태 : 입금상태
}
