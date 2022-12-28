package com.docmall.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.docmall.domain.ReviewVO;
import com.docmall.dto.Criteria;

public interface ReviewMapper {

	//댓글 등록
	void create(ReviewVO vo);
	
	//댓글 목록
	List<ReviewVO> list(@Param("pdt_num") Integer pdt_num, @Param("cri") Criteria cri);
	
	//댓글 총 갯수
	int listCount(Integer pdt_num);
	
	//댓글 삭제
	int delete(Long rv_num);
	
	//후기 수정
	int update(ReviewVO vo);
}
