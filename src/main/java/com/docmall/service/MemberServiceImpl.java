package com.docmall.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.docmall.domain.MemberVO;
import com.docmall.mapper.MemberMapper;

import lombok.Setter;

@Service
public class MemberServiceImpl implements MemberService {

	//주입작업
	//@Autowired  롬복사용 안할 경우
	@Setter(onMethod_ =  {@Autowired})  // jdk 1.8 만 onMethod_
	private MemberMapper memberMapper;

	@Override
	public String idCheck(String mem_id) {
		// TODO Auto-generated method stub
		return memberMapper.idCheck(mem_id);
	}

	@Override
	public void join(MemberVO vo) {
		// TODO Auto-generated method stub
		memberMapper.join(vo);
		
	}

	@Override
	public MemberVO login_ok(String mem_id) {
		// TODO Auto-generated method stub
		return memberMapper.login_ok(mem_id);
	}

	@Override
	public String getIDEmailExists(String mem_id, String mem_email) {
		// TODO Auto-generated method stub
		return memberMapper.getIDEmailExists(mem_id, mem_email);
	}

	@Override
	public void changePW(String mem_id, String enc_pw) {
		// TODO Auto-generated method stub
		memberMapper.changePW(mem_id, enc_pw);
	}

	@Override
	public void modify(MemberVO vo) {
		// TODO Auto-generated method stub
		memberMapper.modify(vo);
	}

	//로그인 시간 업데이트
	@Override
	public void loginTimeUpdate(String mem_id) {
		memberMapper.loginTimeUpdate(mem_id);
		
	}

	//총 주문금액(배송완료 기준)
	@Override
	public int getOrderTotalPrice(String mem_id) {
		
		return memberMapper.getOrderTotalPrice(mem_id);
	}
}
