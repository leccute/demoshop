package com.docmall.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.docmall.domain.AdOrderDetailVOList;
import com.docmall.domain.OrderDetailProductVO;
import com.docmall.domain.OrderVO;
import com.docmall.domain.PaymentVO;
import com.docmall.dto.Criteria;
import com.docmall.mapper.AdOrderMapper;
import com.docmall.mapper.AdPaymentMapper;

import lombok.Setter;

@Service
public class AdOrderServiceImpl implements AdOrderService{

	//AdOrder 매퍼 주입
	@Setter(onMethod_ = {@Autowired})
	public AdOrderMapper adOrderMapper;
	
	//AdPaymentMapper 주입
	@Setter(onMethod_ = {@Autowired})
	public AdPaymentMapper adPaymentMapper;
	
	//검색기능 추가한 페이징
	@Override
	public List<OrderVO> getOrderList(Criteria cri, String odr_status, String startDate, String endDate) {
		
		return adOrderMapper.getOrderList(cri, odr_status, startDate, endDate);
	}

	//총 데이터 갯수
	@Override
	public int getOrderTotalCount(Criteria cri, String odr_status, String startDate, String endDate) {
		
		return adOrderMapper.getOrderTotalCount(cri, odr_status, startDate, endDate);
	}

	//주문 상태 변경
	@Override
	public int orderStatusChange(String odr_status, Long odr_code) {
		
		return adOrderMapper.orderStatusChange(odr_status, odr_code);
	}

	//진행 상태 카운트
	@Override
	public int orderStatusCount(String odr_status) {
		
		return adOrderMapper.orderStatusCount(odr_status);
	}

	//주문상세정보1. OrderDetailVO
	@Override
	public List<AdOrderDetailVOList> getOrderDetailList1(Long odr_code) {
		
		return adOrderMapper.getOrderDetailList1(odr_code);
	}
	//주문상세정보2. OrderDetailVO
	@Override
	public List<OrderDetailProductVO> getOrderDetailList2(Long odr_code) {
		
		return adOrderMapper.getOrderDetailList2(odr_code);
	}
	

	//결제정보. PaymentVO
	@Override
	public PaymentVO getPaymentVO(Long odr_code) {
		
		return adOrderMapper.getPaymentVO(odr_code);
	}

	//주문정보. OrderVO
	@Override
	public OrderVO getOrderVO(Long odr_code) {
		
		return adOrderMapper.getOrderVO(odr_code);
	}

	//관리자 메모
	@Override
	public void pay_memo(String pay_memo, Integer pay_code) {
		adOrderMapper.pay_memo(pay_memo, pay_code);
		
	}
	
	//주문 삭제. 참조키 설정이 지금은 안되어 있지만, 되어 있다면 참조해주는 오더 테이블을 제일 늦게 삭제해야 한다.
	@Transactional
	@Override
	public void orderInfoDelete(Long odr_code) {
		adOrderMapper.orderDetailDelete(odr_code);
		adOrderMapper.orderPaymentDelete(odr_code);
		adOrderMapper.orderDelete(odr_code);
		
	}

	//주문 상품 개별 삭제: 1)ORDER_DETAIL_TBL 상품 삭제 2)ORDER_TBL 총 주문 금액 변경(차감) 3)PAYMENT_TBL 총 주문 금액 변경(차감)
	@Transactional
	@Override
	public void orderDetailProductDelete(Long odr_code, Integer pdt_num, int odr_price) {
		
		//1) 삭제
		adOrderMapper.orderDetailProductDelete(odr_code, pdt_num);
		
		//2) order_tbl 총 주문 금액 변경(차감): 서비스에는 안썼다. 쓰면 여기에 추가할 수 있는 게 아니라 따로 메소드가 생기기 때문에.
		adOrderMapper.orderTotalPriceChange(odr_code, odr_price);
		
		//3)PAYMENT_TBL 총 주문 금액 변경(차감)
		adPaymentMapper.orderPayTotalPriceChange(odr_code, odr_price);
	}


}
