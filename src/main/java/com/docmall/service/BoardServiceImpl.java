package com.docmall.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.docmall.domain.BoardVO;
import com.docmall.mapper.BoardMapper;

import lombok.Setter;

@Service
public class BoardServiceImpl implements BoardService{

	//매퍼 주입
	@Setter(onMethod_ = {@Autowired})
	private BoardMapper boardMapper;

	//게시글 작성
	@Override
	public void write(BoardVO vo) {
		boardMapper.write(vo);
		
	}

	//리스트
	@Override
	public List<BoardVO> list() {
		
		return boardMapper.list();
	}
}
