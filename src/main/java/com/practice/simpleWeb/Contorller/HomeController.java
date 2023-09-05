package com.practice.simpleWeb.Contorller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class HomeController {

    @RequestMapping("/home")
    @ResponseBody
    public String home(){
        return "HOME";

    }
}
