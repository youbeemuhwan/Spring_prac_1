package com.practice.simpleWeb.Service;

import com.practice.simpleWeb.Dto.BoardDto;
import com.practice.simpleWeb.Dto.BoardResponseDto;
import com.practice.simpleWeb.Dto.MemberDto;
import com.practice.simpleWeb.Repository.BoardRepository;
import com.practice.simpleWeb.Repository.MemberRepository;
import com.practice.simpleWeb.domain.Board;
import com.practice.simpleWeb.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public BoardResponseDto create(Authentication authentication, BoardDto boardDto){

        String email = authentication.getName();
        Member member = memberRepository.findByEmail(email).orElseThrow();

        boardDto.setMember(member);

        Board entity = boardDto.toEntity(boardDto);
        boardRepository.save(entity);

        boardRepository.flush();

        return BoardResponseDto
                .builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .member(getMemberDto(member))
                .build();
    }

    public List<BoardResponseDto> boardList(Pageable pageable){
        Page<Board> AllBoard = boardRepository.findAll(pageable);

        List<BoardResponseDto> BoardList = new ArrayList<>();

        for(Board board : AllBoard){
            BoardResponseDto boardResponseDto = BoardResponseDto.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .member(getMemberDto(board.getMember()))
                    .build();

            BoardList.add(boardResponseDto);
        }
        return BoardList;
    }

    public List<BoardResponseDto> getMyBoardList(Authentication authentication, Pageable pageable){
        Member member = memberRepository.findByEmail(authentication.getName()).orElseThrow();
        Page<Board> allByMemberId = boardRepository.findByMember_id(member.getId(), pageable);

        List<BoardResponseDto> boardList = new ArrayList<>();
        for (Board board : allByMemberId){
            BoardResponseDto boardResponseDto = BoardResponseDto
                    .builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .member(getMemberDto(member))
                    .build();

            boardList.add(boardResponseDto);
        }
        return boardList;
    }


    public MemberDto getMemberDto(Member member){
        return MemberDto.builder()
                .email(member.getEmail())
                .username(member.getUsername())
                .role(member.getRole())
                .address(member.getAddress())
                .build();
    }



}
