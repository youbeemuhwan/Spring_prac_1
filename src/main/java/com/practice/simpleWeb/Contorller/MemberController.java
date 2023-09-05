package com.practice.simpleWeb.Contorller;

import com.practice.simpleWeb.Dto.MemberCreateRequestDto;
import com.practice.simpleWeb.Dto.MemberListDto;
import com.practice.simpleWeb.Dto.MemberLoginRequestDto;
import com.practice.simpleWeb.Dto.MemberLoginResponseDto;
import com.practice.simpleWeb.Service.MemberService;
import com.practice.simpleWeb.domain.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public List<Board> list(@PageableDefault(size = 5) Pageable pageable){
        return memberService.memberList(pageable);
    }

    @DeleteMapping("/member/logout")
    @ResponseBody
    public String logout(Authentication authentication, HttpServletRequest request){
        memberService.memberLogout(authentication, request);
        return "LOGOUT DONE";
    }
}
