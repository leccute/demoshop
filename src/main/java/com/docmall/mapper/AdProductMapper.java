package com.docmall.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.docmall.domain.CategoryVO;
import com.docmall.domain.ProductVO;
import com.docmall.dto.Criteria;

public interface AdProductMapper {

	//1차 카테고리 목록
	List<CategoryVO> getCategoryList();
	
	//선택한 1차 카테고리에 따른 2차 카테고리 목록
	List<CategoryVO> getSubCategoryList(Integer cate_code);
	
	//상품 등록 정보 저장
	void pruductInsert(ProductVO vo);
	
	//상품 목록
	List<ProductVO> getProductList(Criteria cri);
	
	//상품 개수
	int getProductTotalCount(Criteria cri);
	
	//수정할 상품 정보
	ProductVO getProductByNum(Integer pdt_num);
	
	//상품수정
	void productModify(ProductVO vo);
	
	//상품삭제
	void productDelete(Integer pdt_num);
	
	//상품 판매 여부 변경하기
	void btnSalesYN (@Param("pdt_num") Integer pdt_num, @Param("pdt_buy") String pdt_buy);
}
