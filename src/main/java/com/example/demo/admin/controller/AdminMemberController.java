package com.example.demo.admin.controller;

import com.example.demo.admin.dto.MemberDto;
import com.example.demo.admin.model.MemberParam;
import com.example.demo.member.entity.Member;
import com.example.demo.member.service.MemberService;
import com.example.demo.util.PageUtil;
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

    parameter.init();
    List<MemberDto> members = memberService.list(parameter);

    long totalCount = 0;
    if (members != null && members.size() > 0) {
      totalCount = members.get(0).getTotalCount();
    }
    String queryString = parameter.getQueryString();
    String pagerHtml = getPaperHtml(totalCount, parameter.getPageSize(), parameter.getPageIndex(), queryString);

    model.addAttribute("list", members);
    model.addAttribute("totalCount", totalCount);
    model.addAttribute("pager", pagerHtml);

    return "admin/member/list";
  }

  @GetMapping("/admin/member/detail.do")
  public String detail(Model model, MemberParam parameter) {

    parameter.init();

    MemberDto member = memberService.detail(parameter.getUserId());
    model.addAttribute("member", member);

    return "admin/member/detail";
  }

}
