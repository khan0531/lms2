package com.example.demo.admin.controller;

import com.example.demo.admin.dto.MemberDto;
import com.example.demo.admin.model.MemberParam;
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
  public String list(Model model, MemberParam parameter) {

    List<MemberDto> members = memberService.list(parameter);

    model.addAttribute("list", members);

    return "admin/member/list";
  }

}
