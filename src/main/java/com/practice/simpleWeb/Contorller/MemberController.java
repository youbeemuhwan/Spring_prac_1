package com.practice.simpleWeb.Contorller;

import com.practice.simpleWeb.Dto.MemberCreateRequestDto;
import com.practice.simpleWeb.Dto.MemberLoginRequestDto;
import com.practice.simpleWeb.Dto.MemberLoginResponseDto;
import com.practice.simpleWeb.Service.MemberService;
import com.practice.simpleWeb.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/member/sign_up")
    @ResponseBody
    public String signUp(@RequestBody MemberCreateRequestDto memberCreateRequestDto){
        memberService.memberSignUp(memberCreateRequestDto);
        return "SIGN UP DONE";
    }

    @GetMapping("/member/login")
    @ResponseBody
    public MemberLoginResponseDto login(@RequestBody MemberLoginRequestDto memberLoginRequestDto){
        return memberService.memberLogin(memberLoginRequestDto);
    }

    @GetMapping("/memberList")
    @ResponseBody
    public List<Member> list(){
        return memberService.memberList();
    }

}
