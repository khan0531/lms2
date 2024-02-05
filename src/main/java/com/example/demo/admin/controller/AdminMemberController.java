package com.example.demo.admin.controller;

import com.example.demo.member.entity.Member;
import com.example.demo.member.service.MemberService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminMemberController {

  private final MemberService memberService;

  @GetMapping("/admin/member/list.do")
  public String list(Model model) {

    List<Member> members = memberService.list();

    model.addAttribute("list", members);

    return "admin/member/list";
  }

}
