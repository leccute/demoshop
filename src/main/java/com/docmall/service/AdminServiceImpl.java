package com.docmall.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.docmall.domain.AdminVO;
import com.docmall.mapper.AdminMapper;

import lombok.Setter;

@Service
public class AdminServiceImpl implements AdminService {

	//mapper주입
	@Setter(onMethod_ = {@Autowired})
	private AdminMapper adminMapper;

	//로그인
	@Override
	public AdminVO admin_ok(String admin_id) {
		// TODO Auto-generated method stub
		return adminMapper.admin_ok(admin_id);
	}

	//로그인 시간 업데이트
	@Override
	public void loginUpdate(String admin_id) {
		adminMapper.loginUpdate(admin_id);
		
	}
}
