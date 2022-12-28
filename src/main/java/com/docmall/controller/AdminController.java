package com.docmall.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.docmall.domain.AdminVO;
import com.docmall.service.AdminService;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RequestMapping("/admin/*")
@Controller
@Log4j
public class AdminController {

	//service주입
	@Setter(onMethod_ = {@Autowired})
	private AdminService adminService;
	
	//주입작업. spring-security.xml의 "bCryptPasswordEncoder" bean 주입을 받음.
	@Setter(onMethod_ = {@Autowired})
	private PasswordEncoder passwordEncoder;
	
	//관리자 로그인페이지
	@GetMapping("")
	public String adLogin() {
		
		return "/admin/adLogin";
	}
	
	@GetMapping("/admin_menu")
	public String admin_menu() {
		
		return "/admin/admin_menu";
	}
	
	@PostMapping("/admin_ok")
	public String admin_ok(AdminVO vo, HttpSession session, RedirectAttributes rttr) {
		
		//얘도 인터셉트에서 예외 처리 하지 않으면, 로그인 정보가 저장되지 않은 상태이기 때문에 이 메소드에 진입도 못하고 다시 로그인 화면으로 돌아가게 된다.
		
		AdminVO adminVO = adminService.admin_ok(vo.getAdmin_id());
		
		String url = "";
		String msg = "";
		if(adminVO != null) {
			if(passwordEncoder.matches(vo.getAdmin_pw(), adminVO.getAdmin_pw())) {
				
				// 일반회원 로그인에 사용한 세션이름과 다르게 해야 한다.
				session.setAttribute("adminStatus", adminVO);
				
				//현재 접속(로그인)시간 업데이트 작업 : 작업 순서가 중요한 작업. ex.if문 이전에 작업하면, 로그인 실패해도 업데이트 된다.
				adminService.loginUpdate(vo.getAdmin_id());
				
				String dest = (String)session.getAttribute("dest");
				
				url = (dest == null)? "/admin/admin_menu" : dest;
				msg = "관리자로그인 성공";
				
				log.info("인증정보 양호");
				
			}else {
				url = "/admin/";
				msg = "관리자 비밀번호가 일치하지 않습니다.";
			}
		}else {
			url = "/admin/";
			msg = "관리자 아이디가 일치하지 않습니다.";
		}
		
		rttr.addFlashAttribute("msg", msg);
		
		return "redirect:" + url;
	}
	
	//로그아웃
	@GetMapping("/logout")
	public String logout(HttpSession session, RedirectAttributes rttr) {
		
		session.invalidate();
		
		return "redirect:/admin/";
	}
}
