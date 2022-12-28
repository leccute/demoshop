package com.docmall.service;

import java.util.List;

import com.docmall.domain.ReviewVO;
import com.docmall.dto.Criteria;

public interface ReviewService {

	//후기 등록
	void create(ReviewVO vo);
	
	//후기 목록
	List<ReviewVO> list(Integer pdt_num, Criteria cri);
	
	//후기 총 갯수
	int listCount(Integer pdt_num);
	
	//후기 삭제
	int delete(Long rv_num);
	
	//후기 수정
	int update(ReviewVO vo);
}
