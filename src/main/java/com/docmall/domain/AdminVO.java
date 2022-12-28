package com.docmall.domain;

import java.util.Date;

import lombok.Data;

@Data
public class AdminVO {
	/*
	 CREATE TABLE ADMIN_TBL(
	ADMIN_ID			VARCHAR2(15)						PRIMARY KEY,
	ADMIN_PW			CHAR(60)						    NOT NULL,
	ADMIN_NAME			VARCHAR2(15)						NOT NULL,
	ADMIN_DATE_LATE	    DATE DEFAULT	SYSDATE			    NOT NULL
); 
	 */
	
	// admin_id, admin_pw, admin_name, admin_date_late
	
	private String admin_id;
	private String admin_pw;
	private String admin_name;
	private Date admin_date_late;
}
