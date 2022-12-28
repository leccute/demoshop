package com.docmall.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import net.coobird.thumbnailator.Thumbnailator;

//컨트롤러에서 호출할 목적으로 생성.
public class FileUtils {

	//상품 이미지 파일에 대한 업로드 작업
	/*
	 1. 날짜 폴더 생성해서 파일 관리
	 2. 썸네일 이미지 생성: 이미지 파일
	 3. 일반 파일과 이미지 파일 구분 작업
	 4. byte[] 배열로 다운로드 작업. 
	 */
	
	//파일 업로드 작업. C:\\JAVA\\ezen_dev\\upload\\goods\\2022\\11\22
	public static String uploadFile(String uploadFolder, String uploadDateFolderPath, MultipartFile uploadFile) {
		
		String uploadFileName = ""; //실제 업로드한 파일명
		
		//File 클래스: 파일과 폴더 작업을 하는 목적
		
		
		//폴더 정보
		File uploadPath = new File(uploadFolder, uploadDateFolderPath);
		
		//폴더 존재 여부(새 날짜마다 폴더를 생성해야 하기 때문에, 폴더가 없으면 생성 / 있으면 안생성)
		if(uploadPath.exists() == false) {
			uploadPath.mkdirs(); //폴더가 존재하지 않으면, 하위 폴더들까지 한번에 생성.
		}
	
		//클라이언트에서 업로드한 파일명.
		String uploadClientFileName = uploadFile.getOriginalFilename();
		
		//중복되지 않는 고유의 문자열 생성
		UUID uuid = UUID.randomUUID();
		
		//업로드시 중복되지 않는 파일 이름을 생성
		uploadFileName = uuid.toString() + "_" + uploadClientFileName;
		
		try {
			//파일 정보
			File saveFile = new File(uploadPath, uploadFileName);
			uploadFile.transferTo(saveFile); //서버의 폴더에 파일 복사(업로드)
			
			//썸네일 작업 (썸네일: 원본은 크기가 크고, 용량이 커서 목록에서 띄우면 로딩 길다, 이때 띄울 작은 사이즈 사진 만들기)
			if(checkImageType(saveFile)) {
				//썸네일 작업? 원본 이미지를 대상으로 사본 이미지를 해상도의 손실을 줄이고, 크기를 작게 만드는 것.
				
				//출력 스트림은 객체만 생성이 되어도, 실제 경로에 파일이 생성되어 있다!! C:\\JAVA\\ezen_dev\\upload\\goods\\2022\\11\22\\abc.jpeg
				FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath, "s_" + uploadFileName));
				//생성된 파일 크키 : 0byte. 원본 이미지의 파일을 입력스트림을 통하여 읽고, 너비 100, 높이 100인 사본 파일을 생성한다.
				Thumbnailator.createThumbnail(uploadFile.getInputStream(), thumbnail, 100, 100);
				thumbnail.close();
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return uploadFileName;
	}
	
	//날짜 폴더를 생성하기 위한 날짜 형태의 문자열 (운영 체제 경로의 구분자에 맞게 리턴).
	public static String getFolder() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Date date = new Date();
		
		String str = sdf.format(date); //오늘이라면 "2022-11-22"
		
		//1)리눅스 또는 맥은 구분자 / : "2022-11-22" -> "2022/11/22"
		//2)윈도우즈 구분자 \ : "2022-11-22" -> "2022\11\22"
		
		return str.replace("-", File.separator); //서버에 배포하면 내가 설정한 sdf 포맷으로 설정되지 않을 가능성이 있어서. 
	}
	
	//일반 파일 또는 이미지 파일 구분
	private static boolean checkImageType(File saveFile) {
		boolean isImage = false;
		
		try {
			//파일에 대한 MIME 정보(파일 분류, 성격 등): text/html, text/plain, image/jpeg
			String contentType = Files.probeContentType(saveFile.toPath());
			
			isImage = contentType.startsWith("image"); //image면 true라는 뜻인가봐,, defalut가 false라 그런가?
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return isImage;
	}
	
	//상품 이미지를 바이트 배열로 읽어오는 작업
	public static ResponseEntity<byte[]> getFile(String uploadPath, String fileName) throws IOException{ //업로드 파일 경로 중 날짜까지.
		
		ResponseEntity<byte[]> entity = null;
		
		File file = new File(uploadPath, fileName);
				
		//이미지가 존재하지 않을 경우
		if(! file.exists()) {
			return null; //jsp에서 onerror 이벤트로 넘어가서 image 파일에 저장된 다른 사진으로 대체되게 이벤트 걸려 있다.
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", Files.probeContentType(file.toPath()));
		
		entity = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(file), headers, HttpStatus.OK);
		
		return entity;
	}
	
	//파일삭제
	//uploadPath: "C:\\JAVA\\ezen_dev\\upload\\goods\\"
	//folder: 2022/11/23
	//fileName: 7a851b92-e85f-420d-b6aa-9bc79acb2bac_GD00083616.default.1.png
	public static void deleteFile(String uploadPath, String folder, String fileName) {
		//원본 이미지 삭제
		new File((uploadPath + folder + "/" + fileName).replace('/', File.separatorChar)).delete();
		//썸네일 이미지 삭제
		new File((uploadPath + folder + "/s_" + fileName).replace('/', File.separatorChar)).delete();
	}
}
