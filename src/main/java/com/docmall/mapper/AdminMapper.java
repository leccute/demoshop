package com.docmall.mapper;

import com.docmall.domain.AdminVO;

public interface AdminMapper {

	//로그인
	AdminVO admin_ok(String admin_id);
	
	//로그인 시간 업데이트
	void loginUpdate(String admin_id);
}
