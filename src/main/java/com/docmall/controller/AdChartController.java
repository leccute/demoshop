package com.docmall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.docmall.domain.ChartCartVO;
import com.docmall.service.AdChartService;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
@Controller
@RequestMapping("/admin/chart/*")
public class AdChartController {

	//서비스 주입
	@Setter(onMethod_ = {@Autowired})
	private AdChartService adChartService;
	
	@GetMapping("/chartProductPrice")
	public void chartProductPrice(Model model) {
		
		String chartCartProductData = "[";
		chartCartProductData += "['상품명', '금액'],";
		
		List<ChartCartVO> cartProductList = adChartService.chartCartList();
		
		//강사님 방식으로 내가 해 본,,
//		for (int i = 0; i < cartProductList.size(); i++) {
//			chartCartProductData = "['";
//			chartCartProductData += ((ChartCartVO)cartProductList).getPdt_name();
//			chartCartProductData += "', '";
//			chartCartProductData += ((ChartCartVO)cartProductList).getPrice();
//			chartCartProductData += "'],";
//		}
		
		int i = 0;
		for (ChartCartVO vo : cartProductList) {
			chartCartProductData += "['" + vo.getPdt_name() + "', " + vo.getPrice() + "]";
			i++;
			if(i < cartProductList.size()) { //마지막 요소의 데이터에서는 진행되지 않는다.
				chartCartProductData += ",";
			}
		}
		
		chartCartProductData += "]";
		
		log.info("차트" + chartCartProductData);
		
		model.addAttribute("data", chartCartProductData);
		
		/*
		 차트 데이터 구조
		 [
       ['상품명', '금액'],
       ['Mike', 22500], 
       ['Bob', 35000],
       ['Alice', 44000],
       ['Frank', 27000],
       ['Floyd', 92000],
       ['Fritz', 18500]
      ],
		 */
		/* 현재 모습
		 	A	242900	
			B	466662
			C	222220
			D	177776 
		 */
		
	
	}
}
