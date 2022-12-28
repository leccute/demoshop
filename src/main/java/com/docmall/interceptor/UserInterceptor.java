package com.docmall.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.docmall.domain.MemberVO;

//설정된 매핑 주소의 요청이 발생되면, 아래 인터셉터 클래스는 동작한다.
//매핑 주소의 성격은 로그인 인증만 사용이 가능한 주소.

//유저 화면에서 로그인을 하지 않고, 로그인 정보가 필요한 과정을 하려고 하면 에러가 뜬다. 따라서, 관련 작업에서 오류 화면이 아니라
//로그인 화면으로 연결되게 해주는 작업을 해줘야 한다.
//로그인 정보가 필요한 페이지들을 servlet-context에 설정하는데, ajax로 작동되게 한 것들은 일반 방식으로는 처리가 안된다. (ex.장바구니 - 수량변경)
//따라서, 내가 어디에서 로그인 정보를 필요하게 했는지, 그 페이지에서 또 ajax로 설정해 둔 내용이 무엇인지 스스로 파악을 잘 하고 있어야 이런 작업을 할 수 있다.
//ajax화면에서, beforeSend 작업, error 작업 해야 한다.


public class UserInterceptor extends HandlerInterceptorAdapter{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		boolean result = false;
	
		//클라이언트의 요청이 인증되었는지 여부 체크(세션 객체를 확인)
		HttpSession session = request.getSession();
		MemberVO user = (MemberVO)session.getAttribute("loginStatus");
		
		if(user == null) { //인증 정보가 존재하지 않을 때.
			result = false;
			
			//클라이언트 요청이 ajax(비동기요청)인지 동기 요청인지 체크
			if(isAjaxRequest(request)) { //ajax요청인 경우
				
				//response.sendRedirect("/member/login");  //ajax요청은 다음 구문이 진행 안된다. 
				//(ajax는 콜백 기능을 가지고 있어, 다시 클라이언트의 콜백으로 제어가 동작되어지는 특징을 가지고 있으므로 (원래 화면: jsp로 넘어가는 성격 때문에), 
				//다른 주소로 이동하라는 스프링의 작업이 진행되지 않는다.
				System.out.println("Ajax요청");
				response.sendError(400);
				
			}else { //동기 요청인 경우. 로그인이 필요한 주소를 요청.
				// 1)로그인을 안 한 경우  2)세션이 소멸된 경우
				getDestination(request);
				
				response.sendRedirect("/member/login");  //ajax요청은 다음 구문이 진행 안된다. 
				//(ajax는 콜백 기능을 가지고 있어, 다시 클라이언트의 콜백으로 제어가 동작되어지는 특징을 가지고 있으므로 (원래 화면: jsp로 넘어가는 성격 때문에), 
				//다른 주소로 이동하라는 스프링의 작업이 진행되지 않는다.
			}
		}else { //인증정보가 존재할 때.
			result = true;
		}
		
		/*
		 1) return true;  // 컨트롤러의 요청 주소로 제어가 넘어간다.
		 2) return false; // 스프링에서 진행이 끝남.
		 */
		
		return result;
	}

	//인증되지 않은 상태에서 인증된 사용자만 접근하는 주소의 정보를 저장하는 목적.
	private void getDestination(HttpServletRequest request) {
		
		//요청주소 : /cart/cart_list?pdt_num=10
		String uri = request.getRequestURI(); // /cart/cart_list 등, 로그인 화면으로 자동 이동되지 않았으면 이동하려고 했던 주소
		String query = request.getQueryString(); // ?물음표 뒤에 파라미터값 pdt_num=10
		
		if(query == null || query.equals("null")) {
			query = "";
		}else {
			query = "?" + query;
		}
		
		String destination = uri + query;
		
		if(request.getMethod().equals("GET")) {
			request.getSession().setAttribute("des", destination);
		}
	}

	private boolean isAjaxRequest(HttpServletRequest request) {
		
		boolean isAjax = false;
		
		String header = request.getHeader("AJAX");
		
		if(header != null && header.equals("true")) { //header != null 얘를 쓰지 않으면, ajax가 아닐 경우, header가 널이 되면서 equals를 확인하려고 할 때 null에러가 걸린다.
			isAjax = true;
		}
		
		return isAjax;
	}

	
}
