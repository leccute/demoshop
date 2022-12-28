package com.docmall.kakaopay;

import lombok.Data;

//결제 준비를 위한 정보를 응답받음(첫번째 요청(결제에 직접적으로 필요한 요소들))

@Data
public class ReadyResponse {

	private String tid; //결제 고유 번호, 20자
	
	//요청한 클라이언트가 PC 웹일 경우, 카카오톡으로 결제 요청 메시지(TMS)를 보내기 위한 사용자 정보 입력 화면 Redirect URL
	private String next_redirect_pc_url; 
	
	private String partner_order_id;
}
