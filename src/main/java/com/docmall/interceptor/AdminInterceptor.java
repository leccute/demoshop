package com.docmall.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.docmall.domain.AdminVO;

//여기선 롬복 log4j가 안 먹으니까, System.out.println()으로라도 로그 확인해보기.

public class AdminInterceptor extends HandlerInterceptorAdapter{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		boolean result = false; //true면 다음으로 넘어가고, false면 다음 과정으로 진행하지 않는다.
				
		HttpSession session = request.getSession();

		AdminVO adminVO = (AdminVO) session.getAttribute("adminStatus");
		
		if(adminVO == null) { //로그인 안 한 상태
			result = false;
			
			if(isAjaxRequest(request)) { //ajax 방식이면. ajax에서 로그인 페이지로 보내라고 redirect 한다.
				response.sendError(400);
				
			}else { //ajax 방식이 아니면.
				getDestination(request);
				
				//admin_ok 얘도 servlet에서 예외처리 하지 않으면, 로그인이 이 경로를 탄다.
				//System.out.println("다시 로그인 주소로");
				
				response.sendRedirect("/admin/");
			}
		}else { //로그인 한 상태
			
			result = true;
		}
		
		return result;
	}

	private void getDestination(HttpServletRequest request) {
		
		String uri = request.getRequestURI();
		String query = request.getQueryString();
		
		if(query == null || query.equals("null")) {
			query = "";
		}else {
			query = "?" + query;
		}
		
		String dest = uri + query;
		
		if(request.getMethod().equals("GET")) {
			request.getSession().setAttribute("dest", dest);
		}
	}

	private boolean isAjaxRequest(HttpServletRequest request) {
		
		boolean isAjax = false;
		
		String header = request.getHeader("AJAX"); //클라이언트에서 사용자 정의로 추가된 헤더
		if(header != null && header.equals("true")) { //header != null 이 처리를 안해주면 header가 없을 때도 가져오려다가 null 에러난다.
			isAjax = true;
		}
		
		return isAjax;
	}

	
}
