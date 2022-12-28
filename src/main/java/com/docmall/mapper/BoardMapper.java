package com.docmall.mapper;

import java.util.List;

import com.docmall.domain.BoardVO;

public interface BoardMapper {

	//게시글 등록
	public void write(BoardVO vo);
	
	//리스트
	public List<BoardVO> list();
}
