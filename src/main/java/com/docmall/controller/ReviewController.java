package com.docmall.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.docmall.domain.MemberVO;
import com.docmall.domain.ReviewVO;
import com.docmall.dto.Criteria;
import com.docmall.dto.PageDTO;
import com.docmall.service.ReviewService;

import lombok.Setter;

//productDetail.jsp에서 전부 사용할 기능들이다.

@RequestMapping("/review/*")
@RestController //댓글을 전부 ajax로 처리 할 것. 이럴 때, RestController로 하면 된다. 전부 ajax니까 jsp파일이 필요 없다.
public class ReviewController {

	//@ResponseBody | jsp 파일로 가는 게 아니라 결과 정보 그 자체를 보여주기 위해 필요한 어노테이션.
	
	//서비스 주입
	@Setter(onMethod_ = {@Autowired})
	private ReviewService reviewService;
	
	//@RequestBody 클라이언트에서 넘어 온 json 문자열을 ReviewVO vo 객체로 변환하는 기능.
	@PostMapping(value = "/new", consumes = "application/json", produces = {MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<String> create(@RequestBody ReviewVO vo, HttpSession session) {
		ResponseEntity<String> entity = null;
		
		String mem_id = ((MemberVO) session.getAttribute("loginStatus")).getMem_id();
		vo.setMem_id(mem_id);
		
		reviewService.create(vo);
		
		entity = new ResponseEntity<String>("success", HttpStatus.OK);
		
		return entity;
	}
	
	//댓글 목록             /list/4/1	
	//리턴 타입 1)Map 컬렉션 : 여러 개의 정보를 반환할 때. 예> 1)댓글 목록 데이터:List<ReviewVO> + 2)댓글 페이징 데이터: PageDTO
	//		2)List 컬렉션 : 한 개의 정보를 반환할 때.
	@GetMapping("/list/{pdt_num}/{page}")
	public ResponseEntity<Map<String, Object>> reviewList(@PathVariable("pdt_num") Integer pdt_num, @PathVariable("page") int page){
		
		ResponseEntity<Map<String, Object>> entity = null;
		Map<String, Object> map = new HashMap<String, Object>();
		
		// 1)댓글 목록 데이터:List<ReviewVO> 
		Criteria cri = new Criteria();
		cri.setPageNum(page);
		
		List<ReviewVO> list = reviewService.list(pdt_num, cri);
		map.put("list", list);
		
		// 2)댓글 페이징 데이터: PageDTO
		PageDTO pageMaker = new PageDTO(cri, reviewService.listCount(pdt_num));
		map.put("pageMaker", pageMaker);
		
		entity = new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
		
		return entity;
	}
	
	//상품후기 삭제하기
	@DeleteMapping(value = "/delete/{rv_num}")
	public ResponseEntity<String> delete(@PathVariable("rv_num") Long rv_num){
		
		//본인(로그인 정보; 아이디)이 쓴 댓글만 지우기
		//이미 본인이 쓴 댓글에만 수정/삭제 버튼이 보인다.
		ResponseEntity<String> entity = null;
		
		//db에서 삭제는 되는데, 삭제 완료했다는 알람은 안뜨고 자꾸 에러로 뜬다. => if(reviewService.delete(rv_num) == 1) 이 설정을 안했을 때는 괜찮았는데, 왤까.
		if(reviewService.delete(rv_num) == 1) {
			entity = new ResponseEntity<String>("success", HttpStatus.OK);
		}else {
			entity = new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
		}

		return entity;
	}
	
	//상품후기 수정하기
	@PatchMapping(value = "/modify", consumes ="application/json", produces = {MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<String> modify(@RequestBody ReviewVO vo, HttpSession session){
		
		ResponseEntity<String> entity = null;
		
		if(reviewService.update(vo) == 1) {
			entity = new ResponseEntity<String>("success", HttpStatus.OK);
		}else {
			entity = new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
		}
			
		return entity;
	}
}
