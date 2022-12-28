package com.docmall.mapper;

import org.apache.ibatis.annotations.Param;

public interface AdPaymentMapper {

	//결제 관련 내용이 많아질 수 있으니 따로 만들어서 뺐다.
	void orderPayTotalPriceChange(@Param("odr_code") Long odr_code, @Param("odr_price") int odr_price);
}
