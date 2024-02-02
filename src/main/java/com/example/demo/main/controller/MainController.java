package com.example.demo.main.controller;

// MainPage 클래스의 목적
// 매핑하기 위해서
// 주소와(논리적인 주소, 인터넷 주소) 물리적인 파일 매핑

// 하나의 주소에 대해서
// 어디서 매핑? 누가 매핑?
// 후보군 : 클래스(이건 아니다.), 속성, 메소드(이게 매핑해줄거다.)


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

  @RequestMapping("/")
  public String index() {
    return "index";
  }

  @RequestMapping("/error/denied")
  public String errorDenied() {
    return "/error/denied";
  }


}
