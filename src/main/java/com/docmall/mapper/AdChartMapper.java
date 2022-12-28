package com.docmall.mapper;

import java.util.List;

import com.docmall.domain.ChartCartVO;

public interface AdChartMapper {

	//장바구니 - 상품 목록 차트
	List<ChartCartVO> chartCartList();
}
