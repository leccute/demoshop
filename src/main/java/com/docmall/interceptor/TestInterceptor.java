package com.docmall.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


//다음 클래스를 동작시키기 위해서는 servlet-context.xml에 설정을 해야 한다.
//인터셉터 기능을 사용하기 위해서는 HandlerInterceptorAdapter 추상 메소드를 상속받아야 한다.
public class TestInterceptor extends HandlerInterceptorAdapter{

	//클라이언트 매핑 주소 : /test/testA
	//인터셉터가 발생되면 preHandle()호출 (TestController의 매핑 주소가 먼저 실행되는 거 아님)
	/*	preHandle()의 값이 true이면, TestController의 testA() 메소드 호출 -> 
		postHandle() 호출 -> /test/testA주소에 해당하는 jsp 실행(뷰작업) ->
		afterCompletion() 호출
	*/
	
	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		//log.info("첫번째 호출 메소드: preHandle"); 로그가 안먹어서 다른 식으로 찍어봐야 한다.
		System.out.println("첫번째 호출 메소드: preHandle");
		return super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		//log.info("세번째 호출 메소드: postHandle");
		System.out.println("세번째 호출 메소드: postHandle");
		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
		//log.info("네번째 호출 메소드: afterCompletion");
		System.out.println("네번째 호출 메소드: afterCompletion");
		super.afterCompletion(request, response, handler, ex);
	}

}
