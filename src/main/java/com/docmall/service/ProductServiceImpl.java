package com.docmall.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.docmall.domain.CategoryVO;
import com.docmall.domain.ProductVO;
import com.docmall.dto.Criteria;
import com.docmall.mapper.ProductMapper;

@Service
public class ProductServiceImpl implements ProductService{

	//매퍼 주입
	@Autowired
	private ProductMapper productMapper;

	//1차 카테고리 목록
	@Override
	public List<CategoryVO> getCategoryList() {
		
		return productMapper.getCategoryList();
	}

	//선택한 1차 카테고리에 따른 2차 카테고리 목록
	@Override
	public List<CategoryVO> getSubCategoryList(Integer cate_code) {
		
		return productMapper.getSubCategoryList(cate_code);
	}

	//2차 카테고리를 참조하는 상품목록.
	@Override
	public List<ProductVO> getProductListbysubCategory(Integer cate_code, Criteria cri) {
		
		return productMapper.getProductListbysubCategory(cate_code, cri);
	}

	//2차 카테고리를 참조하는 상품목록의 개수
	@Override
	public int getProductCountbysubCategory(Integer cate_code, Criteria cri) {
		
		return productMapper.getProductCountbysubCategory(cate_code, cri);
	}

	//상품상세정보
	@Override
	public ProductVO getProductDetail(Integer pdt_num) {
		
		return productMapper.getProductDetail(pdt_num);
	}
}
