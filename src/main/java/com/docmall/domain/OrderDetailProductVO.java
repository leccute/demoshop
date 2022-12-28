package com.docmall.domain;

import lombok.Data;

//AdOrderDetailVOList 클래스를 새로 생성하지 않고, 기존 클래스를 이용하는 목적으로 만듦.

@Data
public class OrderDetailProductVO {

	private OrderDetailVO orderDetailVO; // 주문 상세 클래스
	private ProductVO productVO; // 상품 클래스
}
