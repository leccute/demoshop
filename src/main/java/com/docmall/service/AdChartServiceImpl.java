package com.docmall.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.docmall.domain.ChartCartVO;
import com.docmall.mapper.AdChartMapper;

import lombok.Setter;

@Service
public class AdChartServiceImpl implements AdChartService{

	//매퍼 주입
	@Setter(onMethod_ = {@Autowired})
	private AdChartMapper adChartMapper;

	@Override
	public List<ChartCartVO> chartCartList() {
		
		return adChartMapper.chartCartList();
	}
}
