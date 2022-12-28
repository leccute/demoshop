package com.docmall.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.docmall.domain.ReviewVO;
import com.docmall.dto.Criteria;
import com.docmall.mapper.ReviewMapper;

import lombok.Setter;

@Service
public class ReviewServiceImpl implements ReviewService{

	//매퍼 주입 
	@Setter(onMethod_ = {@Autowired})
	ReviewMapper reviewMapper;

	//후기 등록
	@Override
	public void create(ReviewVO vo) {
		reviewMapper.create(vo);
	}

	//후기 목록
	@Override
	public List<ReviewVO> list(Integer pdt_num, Criteria cri) {
		
		return reviewMapper.list(pdt_num, cri);
	}

	//후기 총 갯수
	@Override
	public int listCount(Integer pdt_num) {
		return reviewMapper.listCount(pdt_num);
	}

	//후기 삭제
	@Override
	public int delete(Long rv_num) {
		
		return reviewMapper.delete(rv_num);
	}

	//후기 수정
	@Override
	public int update(ReviewVO vo) {
		
		return reviewMapper.update(vo);
	}
}
