package com.practice.simpleWeb.Dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardResponseDto {
    private Long id;
    private String title;
    private String content;
    private MemberDto member;

    @Builder
    public BoardResponseDto(Long id, String title, String content, MemberDto member) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.member = member;
    }
}
