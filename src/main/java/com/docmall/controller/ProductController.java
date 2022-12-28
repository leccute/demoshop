package com.docmall.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.docmall.domain.CategoryVO;
import com.docmall.domain.ProductVO;
import com.docmall.dto.Criteria;
import com.docmall.dto.PageDTO;
import com.docmall.service.ProductService;
import com.docmall.util.FileUtils;

import lombok.extern.log4j.Log4j;

@Controller //jsp파일을 사용한다. <->RestController
@Log4j
@RequestMapping("/product/*")
public class ProductController {

	//서비스 주입 작업
	@Autowired
	private ProductService productService;
	
	//업로드 폴더 주입
	@Resource(name = "uploadPath") //servlet-context.xml에 설정.
	private String uploadPath; //"C:\\JAVA\\ezen_dev\\upload\\goods\\"
	
	//1차 카테고리를 참조하는 2차 카테고리 목록
	@ResponseBody
	@GetMapping("/subCategoryList/{cate_code}") //요청주소 : /admin/product//subCategoryList/선택한1차카테고리value값
	public ResponseEntity<List<CategoryVO>> subCategoryList(@PathVariable("cate_code") Integer cate_code){
		
		ResponseEntity<List<CategoryVO>> entity = null;
		
		entity = new ResponseEntity<List<CategoryVO>>(productService.getSubCategoryList(cate_code), HttpStatus.OK);
		
		return entity;
	}
	
	//2차 카테고리를 참조하는 상품리스트(페이징 포함, 검색 제외)
	// 특수문자 '/'의 값을 서버가 받을 때, @PathVariable이 해결을 할 수 가 없다. 중간 작업이 필요하다.
	// 요청주소 1: /productList/10/맨투맨/후드티    결과: 404 매핑 주소를 찾을 수 없다.
	// 요청주소 2: /productList/10/맨투맨%2F후드티  결과: 404 에러는 발생되지 않는다. @PathVariable에서 해결되지 못한다. 다른 방법으로 처리해야 한다.(추후 설명)
	@GetMapping("/productList/{cate_code}/{cate_name}")
	public String productList(@ModelAttribute("cri") Criteria cri,@PathVariable("cate_code") Integer cate_code,@PathVariable("cate_name") String cate_name, Model model) {
		log.info("2차 카테고리 코드: " + cate_code); 
		log.info("2차 카테고리 이름: " + cate_name);
		
		//2차 카테고리 이름
		model.addAttribute("cate_name", cate_name);
		
		//1)상품 목록
		List<ProductVO> productList = productService.getProductListbysubCategory(cate_code, cri);
		
		//브라우저에서 상품 이미지 출력시 데이터베이스의 날짜 폴더가 \ 역슬래시로 되어 있어 특수 문자로 인한 요청에 문제가 발생된다.
		//역슬래시를 슬래시로 변환하여, 사용하게 해야 한다.
		productList.forEach(vo -> { //함수적 인터페이스(함수, 배열에 forEach 지원, for문도 사용 가능.)
			vo.setPdt_img_folder(vo.getPdt_img_folder().replace("\\", "/")); 
		});
		
		model.addAttribute("productList", productList);
		
		//2)페이징 작업
		int totalCount = productService.getProductCountbysubCategory(cate_code, cri);
		model.addAttribute("pageMaker", new PageDTO(cri, totalCount));
		
		return "/product/productList";
	}
	
	//상품상세
	@GetMapping("/productDetail")
	public void productDetail(Model model, Integer pdt_num) {
		
		 ProductVO productVO = productService.getProductDetail(pdt_num);
		 productVO.setPdt_img_folder(productVO.getPdt_img_folder().replace("\\", "/"));
		 
		 model.addAttribute("productVO", productVO);
		 /*
		 이것과 위 구문이 같은 뜻. 즉, 아래 구문처럼 쓰면 모델 이름은 ProductVO -> productVO 이렇게 생성된다.
		*/
		//model.addAttribute(productService.getProductDetail(pdt_num));
	}
	
	//상품 목록에서 이미지 보여주기.
	@ResponseBody //ajax 형식
	@GetMapping("/displayFile")
	public ResponseEntity<byte[]> displayFile(String folderName, String fileName) throws IOException{
		
		//C:\\JAVA\\ezen_dev\\upload\\goods\\2022\\11\\22\\
		return FileUtils.getFile(uploadPath + folderName, fileName);
	}
	
}
