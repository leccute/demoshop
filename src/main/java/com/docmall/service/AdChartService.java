package com.docmall.service;

import java.util.List;

import com.docmall.domain.ChartCartVO;

public interface AdChartService {

	//장바구니 - 상품 목록 차트
	List<ChartCartVO> chartCartList();
}
