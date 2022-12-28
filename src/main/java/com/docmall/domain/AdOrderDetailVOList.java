package com.docmall.domain;

import lombok.Data;

//주문관리 : 주문 상세 정보에서 주문 상세 상품 정보를 사용. (OrderDetailVO + ProductVO 2개의 클래스를 조인해서 새로 만든 VO)

@Data
public class AdOrderDetailVOList {

	private Long odr_code;
	private Integer pdt_num;
	private String pdt_name;
	private int pdt_price; 
	private int odr_amount;
	private int odr_price; 
	private String pdt_img_folder; //이미지 저장 폴더명
	private String pdt_img;  //파일의 대표 이미지
}
