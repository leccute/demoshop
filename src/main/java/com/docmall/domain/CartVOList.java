package com.docmall.domain;

import lombok.Data;

@Data
public class CartVOList {

	
	private Long cart_code;
	private Integer pdt_num;
	private String pdt_name;
	private String mem_id;
	private Integer pdt_price;
	private int pdt_dis_price; //할인율 적용된 가격 
	private int cart_amount;
	private String pdt_img_folder; //이미지 저장 폴더명
	private String pdt_img;  //파일의 대표 이미지
	private int sales_price; //sql에서 pdt_price * cart_amount 이걸로 처리해둠
	
}
