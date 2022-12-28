package com.docmall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.docmall.domain.BoardVO;
import com.docmall.service.BoardService;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/board/*")
public class BoardController {

	//서비스 주입
	@Setter(onMethod_ = {@Autowired})
	BoardService boardService;
	
	//게시글 작승 폼
	@GetMapping("/register")
	public void register() {
		log.info("글쓰기 폼 불러오기");
	}
	
	//게시글 작성
	@PostMapping("/register")
	public String write(BoardVO vo, RedirectAttributes rttr) {
		
		boardService.write(vo);
		rttr.addFlashAttribute("msg", "게시글 등록 완료.");
		
		return "redirect:/board/list";
	}
	
	//게시물 목록
	@GetMapping("/list")
	public void list(Model model) {
		List<BoardVO> list = boardService.list();
		model.addAttribute("list", list);
		
	}
}
