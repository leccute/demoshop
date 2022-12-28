package com.docmall.domain;

import lombok.ToString;

import lombok.Setter;

import lombok.Getter;

@Getter
@Setter
@ToString
public class CategoryVO {
/*
  CATE_CODE       			NUMBER        				    PRIMARY KEY,
  CATE_CODE_PRT    		    NUMBER,                                          
  CATE_NAME           		VARCHAR2(50)         			NOT NULL
  
  cate_code,  cate_code_prt, cate_name
 */
	
	private Integer cate_code;
	private Integer cate_code_prt;
	private String cate_name;
}
