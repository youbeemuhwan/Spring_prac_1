package com.practice.simpleWeb.Dto;

import com.practice.simpleWeb.domain.Board;
import com.practice.simpleWeb.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BoardDto {

    private Long id;
    private String title;
    private String content;
    private Member member;

    public BoardDto() {
    }

    public BoardDto(Long id, String title, String content, Member member) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.member = member;
    }

    public Board toEntity(BoardDto boardDto){
        return Board.builder()
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .member(boardDto.getMember())
                .build();
    }

}
