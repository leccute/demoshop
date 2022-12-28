package com.docmall.service;

import java.util.List;

import com.docmall.domain.BoardVO;

public interface BoardService {

	//게시글 등록
	void write(BoardVO vo);
	
	//리스트
	public List<BoardVO> list();
}
