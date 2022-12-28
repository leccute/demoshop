package com.docmall.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.docmall.domain.CategoryVO;
import com.docmall.domain.ProductVO;
import com.docmall.dto.Criteria;

public interface ProductMapper {

	//1차 카테고리 목록
	List<CategoryVO> getCategoryList();
	
	//선택한 1차 카테고리에 따른 2차 카테고리 목록
	List<CategoryVO> getSubCategoryList(Integer cate_code);
	
	//2차 카테고리를 참조하는 상품목록. Mapper Interface의 메소드가 파라미터 2개 이상일 경우에는 @Param 어노테이션을 써야 한다. (중요)
	List<ProductVO> getProductListbysubCategory(@Param("cate_code") Integer cate_code, @Param("cri") Criteria cri);
	
	//2차 카테고리를 참조하는 상품목록의 개수
	int getProductCountbysubCategory(@Param("cate_code") Integer cate_code, @Param("cri") Criteria cri);
	
	//상품상세정보
	ProductVO getProductDetail(Integer pdt_num);
}
