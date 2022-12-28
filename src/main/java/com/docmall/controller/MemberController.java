package com.docmall.controller;

import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.docmall.domain.MemberVO;
import com.docmall.dto.EmailDTO;
import com.docmall.dto.LoginDTO;
import com.docmall.service.CartService;
import com.docmall.service.EmailService;
import com.docmall.service.MemberService;
import com.docmall.service.OrderService;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

//회원기능담당.
//인터셉트 걸었다 -> 로그인 정보 가지고 오는 애들, get/post 같이 있는 애들 중에는 get. 
//다른 컨트롤러에서도 loginStatus를 불러오는 애들에 사용해주기.

@Log4j
@RequestMapping("/member/*")
@Controller
public class MemberController {

	//주입작업
	@Setter(onMethod_ = {@Autowired})
	private MemberService memberService;
	
	//카트 서비스 주입
	@Setter(onMethod_ = {@Autowired})
	private CartService cartService;
	
	//주문 서비스 주입
	@Setter(onMethod_ = {@Autowired})
	private OrderService orderService;
	
	//주입작업. spring-security.xml의 "bCryptPasswordEncoder" bean 주입을 받음.
	@Setter(onMethod_ = {@Autowired})
	private PasswordEncoder passwordEncoder;
	
	//주입작업
	@Setter(onMethod_ = {@Autowired})
	private EmailService emailService;
	
	
	
	
	//회원가입폼
	@GetMapping("/join")
	public void join() {
		log.info("회원가입폼");
	}
	
	//회원정보저장
	@PostMapping("/join")
	public String join(MemberVO vo, RedirectAttributes rttr) {
		
		//평문텍스트 비밀번호 --> 암호화
		// 1234 --> 암호화(60byte)
		String cryptEncoderPw = passwordEncoder.encode(vo.getMem_pw());
		
		vo.setMem_pw(cryptEncoderPw);
		
		
		
		// 자바문법.  null 이었을 경우에는 참조연산 . 사용불가능.
		// <input type="checkbox"> 체크를 하지않으면, 정보가 전송되지 않아 null 처리가 된다.
		if(vo.getMem_accept_e() != null && vo.getMem_accept_e().equals("on")) {
			vo.setMem_accept_e("Y");
		}else {
			vo.setMem_accept_e("N");
		}
		
		memberService.join(vo);
		
		return "redirect:/member/login";
	}
	
	//아이디 중복체크
	@ResponseBody  // ajax요청일 경우 사용.
	@GetMapping("/idCheck")
	public ResponseEntity<String> idCheck(@RequestParam("mem_id") String mem_id) {
		ResponseEntity<String> entity = null;
		
		
		String isUseID = "";
		if(memberService.idCheck(mem_id) != null) {
			isUseID = "no"; //아이디가 사용중.
		}else {
			isUseID = "yes"; // 아이디가 사용안함.
		}
		
		entity = new ResponseEntity<String>(isUseID, HttpStatus.OK);
		
		return entity;
	}
	
	//로그인폼
	@GetMapping("/login")
	public void login() {
		log.info("로그인 폼");
	}
	
	//로그인인증
	@PostMapping("/loginPost")
	public String loginPost(LoginDTO dto, HttpSession session, RedirectAttributes rttr) {
		
		/*
		 사용자에게 로그인 정보가 일치하지 않을 경우 메시지에 따른 작업방법이 각각 다르다.
		 1)아이디 또는 비번이 어떤것이 일치하지 않든, 로그인정보가 일치하지 않습니다.
		   SQL> SELECT * FROM MEMBER_TBL WHERE MEM_ID = #{mem_id} AND MEM_PW = #{mem_pw}
		   -> 비번이 암호화되어 위의 쿼리문으로 인증작업을 할수가 없다.
		   
		 2) ㄱ)아이디가 불일치 : 아이디가 일치하지 않는다. ㄴ)비밀번호 불일치 : 비번이 일치하지 않는다.  (적용)
		 */
		MemberVO vo = memberService.login_ok(dto.getMem_id());
		
		String url = "";
		String msg = "";
		
		if(vo != null) {	//아이디가 일치함.
			String passwd = dto.getMem_pw(); //사용자가 입력한 비밀번호
			String db_passwd = vo.getMem_pw(); // DB에서 가져온 암호화된 비밀번호
			
			if(passwordEncoder.matches(passwd, db_passwd)) { // 비번이 일치됨.
				
				//로그인 시간 업데이트 작업
				memberService.loginTimeUpdate(dto.getMem_id());
				
				session.setAttribute("loginStatus", vo);
				
				String dest = (String) session.getAttribute("des");
				url = (dest != null)? dest : "/";
				
				msg = "로그인이 되었습니다.";
			}else { // 비번이 일치되지 않음
				url = "/member/login";
				msg = "비번이 일치하지 않습니다.";
			}
		}else {	// 아이디가 일치하지 않음.
			url = "/member/login";
			msg = "아이디가 일치하지 않습니다.";
		}
		
		
		rttr.addFlashAttribute("msg", msg);
		
		return "redirect:" + url;
	}
	
	//로그아웃
	@GetMapping("/logout")
	public String logout(HttpSession session, RedirectAttributes rttr) {
		
		session.invalidate(); // 세션 소멸
		rttr.addFlashAttribute("msg", "로그아웃됨.");
		
		return "redirect:/";
	}
	
	//아이디, 비번찾기 폼
	@GetMapping("/lostpass")
	public void lostpass() {
		
	}
	
	//비번찾기 : 임시비밀번호 발급
	@PostMapping("/searchPw")
	public String searchPw(@RequestParam("mem_id") String mem_id, 
						   @RequestParam("mem_email") String mem_email,
						   RedirectAttributes rttr) 
	{
		
		
		//1.아이디와메일정보가 일치되는 지 존재유무 확인
		String db_mem_id = memberService.getIDEmailExists(mem_id, mem_email);
		String temp_pw = "";
		
		String url = "";
		String msg = "";
		
		if(db_mem_id != null) {
			
			//2. 임시비밀번호 생성
			UUID uid = UUID.randomUUID();
			
			log.info("임시비번: " + uid);
			
			temp_pw = uid.toString().substring(0, 6);  //6자리.
			
			
			
			// 3. 사용자의 회원정보 비밀번호를 암호화하여 DB에수정작업
			memberService.changePW(db_mem_id, passwordEncoder.encode(temp_pw));
			
			//4. 사용자에게 임시비밀번호 메일발송
			EmailDTO dto = new EmailDTO("DocMall", "DocMall", mem_email, "DocMall 임시비밀번호 입니다.", "");
			
			try {
				emailService.sendMail(dto, temp_pw);
				url = "/member/login";
				msg = "메일이 발송되었습니다.";
				
			}catch(Exception ex) {
				ex.printStackTrace();
			}
			
		}else {
			url = "/member/lostpass";
			msg = "입력하신 정보가 일치하지 않습니다. 확인해주세요.";
		}
		
		rttr.addFlashAttribute("msg", msg);
		
		return "redirect:" + url;
	}
	
	@GetMapping("/confirmPW")
	public void confirmPW() {
		
	}
	
	@PostMapping("/confirmPW")
	public String confirmPW(@RequestParam("mem_pw") String mem_pw, HttpSession session, RedirectAttributes rttr) {
		
		//로그인 상태에서 세션을 통하여, 사용자 아이디를 참조할수가 있다.
		String mem_id = ((MemberVO) session.getAttribute("loginStatus")).getMem_id();
		
		MemberVO vo = memberService.login_ok(mem_id);
		
		String url = "";
		String msg = "";
		
		if(vo != null) {
			
			String db_passwd = vo.getMem_pw(); // DB에서 가져온 암호화된 비밀번호
			
			if(passwordEncoder.matches(mem_pw, db_passwd)) { // 비번이 일치됨.						
				url = "/member/modify";
			}else { // 비번이 일치되지 않음
				url = "/member/confirmPW";
				msg = "비번이 일치하지 않습니다.";
			}
		}
		
		rttr.addFlashAttribute("msg", msg);
		
		return "redirect:" + url;
	}
	
	@GetMapping("/modify")
	public void modify(Model model, HttpSession session) {
		
		String mem_id = ((MemberVO) session.getAttribute("loginStatus")).getMem_id();
		
		MemberVO vo = memberService.login_ok(mem_id);
		
		model.addAttribute("memberVO", vo);
	}
	
	@PostMapping("/modify")
	public String modify(MemberVO vo, HttpSession session, RedirectAttributes rttr) {
		
		String enc_mem_pw = ((MemberVO) session.getAttribute("loginStatus")).getMem_pw();
		
		
		String url = "";
		String msg = "";
		
		// 비밀번호를 보안관점에서 다시 확인.
		if(passwordEncoder.matches(vo.getMem_pw(), enc_mem_pw)) {
			memberService.modify(vo);
			url = "/";
			msg = "회원정보수정 됨.";
		}else {
			url = "/member/modify";
			msg = "비밀번호 일치하지 않습니다.";
		}
		
		rttr.addFlashAttribute("msg", msg);
		
		return "redirect:" + url;
	}
	
	//마이페이지
	@GetMapping("/mypage")
	public void mypage(HttpSession session, Model model) {
		String mem_id = ((MemberVO) session.getAttribute("loginStatus")).getMem_id();
		
		model.addAttribute("odr_totalPrice", memberService.getOrderTotalPrice(mem_id));
		model.addAttribute("cart_count", cartService.getCartProductCountByUserID(mem_id));
		model.addAttribute("order_count", orderService.getOrderProcessCount(mem_id));
	}
	
	
	
}
