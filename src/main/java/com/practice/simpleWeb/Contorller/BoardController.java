package com.practice.simpleWeb.Contorller;

import com.practice.simpleWeb.Dto.BoardDto;
import com.practice.simpleWeb.Dto.BoardResponseDto;
import com.practice.simpleWeb.Repository.BoardRepository;
import com.practice.simpleWeb.Service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/board/create")
    @ResponseBody
    public BoardResponseDto create(Authentication authentication, @RequestBody BoardDto boardDto){
        return boardService.create(authentication, boardDto);
    }

    @GetMapping("/board/list")
    @ResponseBody
    public List<BoardResponseDto> list(@PageableDefault(value = 5) Pageable pageable){
      return   boardService.boardList(pageable);
    }

    @GetMapping("/board/my_board")
    @ResponseBody
    public List<BoardResponseDto> myBoardList(Authentication authentication, @PageableDefault(value = 5) Pageable pageable){
        return boardService.getMyBoardList(authentication,pageable);
    }
}
