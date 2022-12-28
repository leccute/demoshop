package com.docmall.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.docmall.domain.CategoryVO;
import com.docmall.domain.ProductVO;
import com.docmall.dto.Criteria;
import com.docmall.mapper.AdProductMapper;

import lombok.Setter;

@Service
public class AdProductServiceImple implements AdProductService{

	//Mapper 주입
	@Setter(onMethod_ = {@Autowired})
	private AdProductMapper adProductMapper;

	//1차 카테고리 목록
	@Override
	public List<CategoryVO> getCategoryList() {
		
		return adProductMapper.getCategoryList();
	}

	//선택한 1차 카테고리에 따른 2차 카테고리 목록
	@Override
	public List<CategoryVO> getSubCategoryList(Integer cate_code) {
		
		return adProductMapper.getSubCategoryList(cate_code);
	}

	//상품 등록 정보 저장
	@Override
	public void pruductInsert(ProductVO vo) {
		
		adProductMapper.pruductInsert(vo);
	}
	
	//상품 목록
	@Override
	public List<ProductVO> getProductList(Criteria cri) {
		
		return adProductMapper.getProductList(cri);
	}

	//상품 개수
	@Override
	public int getProductTotalCount(Criteria cri) {
		
		return adProductMapper.getProductTotalCount(cri);
	}

	//수정할 상품 정보
	@Override
	public ProductVO getProductByNum(Integer pdt_num) {
		
		return adProductMapper.getProductByNum(pdt_num);
	}

	//상품수정
	@Override
	public void productModify(ProductVO vo) {
		adProductMapper.productModify(vo);
		
	}

	@Override
	public void productDelete(Integer pdt_num) {
		adProductMapper.productDelete(pdt_num);
		
	}

	//상품 판매 여부 변경하기
	@Override
	public void btnSalesYN(Integer pdt_num, String pdt_buy) {
		adProductMapper.btnSalesYN(pdt_num, pdt_buy);
		
	}
}
