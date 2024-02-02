package com.example.demo.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminMainController {


  @GetMapping("/admin/main")
  public String main() {
    return "admin/main";
  }

}
