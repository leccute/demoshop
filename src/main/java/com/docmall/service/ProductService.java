package com.docmall.service;

import java.util.List;

import com.docmall.domain.CategoryVO;
import com.docmall.domain.ProductVO;
import com.docmall.dto.Criteria;

public interface ProductService {

	//1차 카테고리 목록
	List<CategoryVO> getCategoryList();
	
	//선택한 1차 카테고리에 따른 2차 카테고리 목록
	List<CategoryVO> getSubCategoryList(Integer cate_code);
	
	//2차 카테고리를 참조하는 상품목록. Mapper Interface의 메소드가 파라미터 2개 이상일 경우에는 @Param 어노테이션을 써야 한다. (중요)
	List<ProductVO> getProductListbysubCategory(Integer cate_code, Criteria cri);
	
	//2차 카테고리를 참조하는 상품목록의 개수
	int getProductCountbysubCategory(Integer cate_code, Criteria cri);
	
	//상품상세정보
	ProductVO getProductDetail(Integer pdt_num);
}
