package com.docmall.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.docmall.domain.CategoryVO;
import com.docmall.domain.ProductVO;
import com.docmall.dto.Criteria;
import com.docmall.dto.PageDTO;
import com.docmall.service.AdProductService;
import com.docmall.util.FileUtils;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
@RequestMapping("/admin/product/*")
@Controller
public class AdProductController {

	//Service 주입
	@Setter(onMethod_ = {@Autowired})
	private AdProductService adProductService;
	
	//업로드 폴더 주입
	@Resource(name = "uploadPath") //servlet-context.xml에 설정.
	private String uploadPath; //"C:\\JAVA\\ezen_dev\\upload\\goods\\"
	
	
	//상품등록 페이지. 1차 카테고리 보이도록.
	@GetMapping("/productInsert")
	public void productInsert(Model model) {
		
	List<CategoryVO> categoryList = adProductService.getCategoryList();
	
	model.addAttribute("categoryList", categoryList);
	}
	
	//1차 카테고리를 참조하는 2차 카테고리 목록. 클라이언트에서 ajax 요청.
	@ResponseBody
	@GetMapping("/subCategoryList/{cate_code}") //요청주소 : /admin/product//subCategoryList/선택한1차카테고리value값
	public ResponseEntity<List<CategoryVO>> subCategoryList(@PathVariable("cate_code") Integer cate_code){
		
		ResponseEntity<List<CategoryVO>> entity = null;
		
		entity = new ResponseEntity<List<CategoryVO>>(adProductService.getSubCategoryList(cate_code), HttpStatus.OK);
		
		return entity;
	}
	
	//CKEditor에서 사용하는 파일업로드 <input type="file" name="upload"> 여기서 upload는 multipartFile upload와 일치해야 함
	@PostMapping("/imageUpload")
	public void imageUpload(HttpServletRequest rep, HttpServletResponse res, MultipartFile upload) {
		
		OutputStream out = null;
		PrintWriter printWriter = null;
		
		//jsp파일 맨 위에 선언문같은 걸로 되어 있는데, 이 에디터는 이렇게 따로 써달라고 요청한다.
		res.setCharacterEncoding("utf-8");
		res.setContentType("text/html; charset=utf-8"); 
		
		try {
			String fileName = upload.getOriginalFilename(); //클라이언트에서 보낸 원본 파일 이름.
			byte[] bytes = upload.getBytes(); //업로드 된 파일을 넣어둔 byte 배열.
			
			//String uploadTomcatTempPath =
			
			//CKEditor를 통하여 업로드되는 서버측의 폴더
			String uploadPath = "C:\\JAVA\\ezen_dev\\upload\\ckeditor\\" + fileName;
			
			//1)출력 스트림을 이용하여, 업로드 작업을 진행.(new File로 안하고 이름 바로 넣어도 되긴 한다.)
			out = new FileOutputStream(new File(uploadPath));
			out.write(bytes); //내부적으로 flush()랑 close()를 자동으로 호출하여, 굳이 안 써도 된다.
			out.flush();
			out.close(); //파일에 이미지가 생성되면 일단 업로드까지는 된.
			
			
			//2)업로드된 파일 정보를 CKEditor에게 
			printWriter = res.getWriter();
			String fileUrl = "/upload/" + fileName; //서버에 설정한 외부 참조 주소
			
			//파일 정보를 json 포맷으로 준비해야 한다. "큰 따옴표"를 내용으로 코딩할 경우에는 \" 로 사용해야 한다.
			// {"filename":"abc.gif", "uploaded":1, "url":"/upload/abc.gif"} CKEditor 4.x version에서 요구하는 json포맷
			printWriter.println("{\"filename\":\"" + fileName + "\" , \"uploaded\":1, \"url\":\"" + fileUrl + "\"}");
			printWriter.flush();
			

			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(out != null) {
				try {
					out.close();
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
			if(printWriter != null) {
				printWriter.close();
			}
		}
	}
	
	//상품 정보 저장
	@PostMapping("/productInsert")
	public String productInsert(ProductVO vo, RedirectAttributes rttr) {
		
		log.info("상품 정보: " + vo);
		
		//1)상품 이미지 파일 업로드 작업
		String uploadDateFolderPath = FileUtils.getFolder();
		String saveImageName = FileUtils.uploadFile(uploadPath, uploadDateFolderPath, vo.getUploadFile());
		
		vo.setPdt_img(saveImageName); //db에 저장될 업로드 파일 명
		
		vo.setPdt_img_folder(uploadDateFolderPath);	 //날짜 폴더명
		
		
		//2)상품 정보 저장
		adProductService.pruductInsert(vo);
		
		//이동되는 주소의 jsp에서 참조가 가능하다. (productList.jsp)
		rttr.addFlashAttribute("msg", "상품 등록 성공");
		
		return "redirect:/admin/product/productList";
	}
	
	//상품목록 (페이징, 검색 추가)
	@GetMapping("/productList") //@ModelAttribute(클래스에서), Model(db에서) 둘 다 jsp에서 쓰기 위해. (이건가) 
	public void productList(@ModelAttribute("cri") Criteria cri, Model model) {
		
		log.info(cri);
		
		List<ProductVO> productList = adProductService.getProductList(cri);
		
		//날짜 폴더가 windows 환경에 따라 2022\\11\\23 이렇게 되어 있고, \는 특수문자로 해석 불가능하여 이미지가 안나옴.
		//ProductVO에 있는 \를 (\한 번만 쓰면 sql 시퀀스 어쩌구에 따라 에러, 따라서 \\ 두 번 써줘야 함) /로 변경하면 제대로 나온다
		productList.forEach(vo -> { //함수적 인터페이스(함수, 배열에 forEach 지원, for문도 사용 가능.)
			vo.setPdt_img_folder(vo.getPdt_img_folder().replace("\\", "/")); 
		});
		
		model.addAttribute("productList", productList);
		
		int productCount = adProductService.getProductTotalCount(cri);
		model.addAttribute("pageMaker", new PageDTO(cri, productCount));
		
	}
	
	//상품 목록에서 이미지 보여주기.
	@ResponseBody //ajax 형식
	@GetMapping("/displayFile")
	public ResponseEntity<byte[]> displayFile(String folderName, String fileName) throws IOException{
		
		//C:\\JAVA\\ezen_dev\\upload\\goods\\2022\\11\\22\\
		return FileUtils.getFile(uploadPath + folderName, fileName);
	}
	
	//상품 수정하기. 카테고리 관련 작업, 이미지 관련 작업 등 연관된 게 많다.
	//1) @GM. 2) mapper작업 (파라미터 : Model, pdt_num, Criteria / update문) 3)서비스, 4)컨트롤러
	@GetMapping("/productModify")
	public void modify(@ModelAttribute("cate_code_prt") Integer cate_code_prt, @ModelAttribute("cate_code") Integer cate_code,
						@RequestParam("pdt_num") Integer pdt_num, @ModelAttribute("cri") Criteria cri, Model model) {
		
		/*상품 등록 폼과 구조가 유사. -> 따로 처리했던 이미지, 카테고리 작업 등 관련된 거 다 고려해야 한다.
		 	- 1차 카테고리 목록 -> DB의 1차 카테고리 선택 상태
		 	- 1차 카테고리를 참조하는 2차 카테고리 목록 -> DB의 2차 카테고리 선택 상태
		 	- 수정하기 : 상품정보
		 	- 상품이미지 변경작업
		 */
		
		//1)1차 카테고리 목록 작업 
		List<CategoryVO> categoryList = adProductService.getCategoryList();
		model.addAttribute("cateList", categoryList);
		
		//2)1차 카테고리 참조하는 2차 카테고리 목록 작업
		//1차코드 '1'을 참조하는 10, 11, 12, 13 코드들의 정보를 가져올 건데, 10, 11, 12, 13은 선택할 수 있게.
		List<CategoryVO> subCategoryList = adProductService.getSubCategoryList(cate_code_prt);
		model.addAttribute("subCateList", subCategoryList);
		
		//3)수정 상품 정보 읽어오기.
		ProductVO vo = adProductService.getProductByNum(pdt_num);
		vo.setPdt_img_folder(vo.getPdt_img_folder().replace("\\", "/"));
		model.addAttribute("productVO", vo);
		
	}
	
	//상품수정하기
	@PostMapping("/productModify")
	public String productModify(ProductVO vo, RedirectAttributes rttr, Criteria cri) {
		 
		//이미지 변경 업로드
		if(!vo.getUploadFile().isEmpty()) {
			//1)기존 이미지 삭제. 
			//vo.getPdt_img_folder() 날짜 폴더
			//vo.getPdt_img() 상품 이미지
			//파라미터: uploadPath, vo.getPdt_img_folder(), vo.getPdt_img() 이렇게 다 따로 분리하면 작업이 수월할 수 있다.
			FileUtils.deleteFile(uploadPath, vo.getPdt_img_folder(), vo.getPdt_img());
			
			//2)새 상품 이미지 업로드.
			String uploadDateFolderPath = FileUtils.getFolder();
			String saveImageName = FileUtils.uploadFile(uploadPath, uploadDateFolderPath, vo.getUploadFile());
			
			//3)DB에 저장될 이미지 관련 정보
			vo.setPdt_img(saveImageName); //db에 저장될 업로드 파일 명
			vo.setPdt_img_folder(uploadDateFolderPath);	 //날짜 폴더명
		}
		//4)상품수정
		adProductService.productModify(vo);
		
		rttr.addFlashAttribute("msg", "상품 수정 완료");
		
		return "redirect:/admin/product/productList" + cri.getListLink();
	}
	
	//상품 삭제하기
	@GetMapping("/productDelete")
	public String productDelete(@RequestParam("pdt_num") Integer pdt_num, Criteria cri, RedirectAttributes rttr) {
		//@ModelAttribute : jsp 에서 필드를 쓰고 싶을 때 사용 (인듯)
		
		adProductService.productDelete(pdt_num);
		
		rttr.addFlashAttribute("msg", "상품 삭제 완료");
		
		//요청주소 : /admin/product/productList?pageNum=1&amount=10&type=&keyword=
		//rttr.addAttribute("pageNum",cri.getPageNum()) 모든 걸 이 작업 해줘야 했는데, cri에 미리 만들어뒀다.
		return "redirect:/admin/product/productList" + cri.getListLink();
	}
	
	//상품 판매 여부 변경하기
	@ResponseBody
	@GetMapping("/btnSalesYN")
	public ResponseEntity<String> btnSalesYN (Integer pdt_num, String pdt_buy) {
		
		adProductService.btnSalesYN(pdt_num, pdt_buy);
		
		ResponseEntity<String> entity = new ResponseEntity<String>("success", HttpStatus.OK);
		
		return entity;
	}
	
}
