package com.example.demo.member.service;

import com.example.demo.member.entity.Member;
import com.example.demo.member.model.MemberInput;
import com.example.demo.member.model.ResetPasswordInput;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface MemberService extends UserDetailsService {

  boolean register(MemberInput parameter);

  // uuid에 해당하는 이메일을 활성화 함.
  boolean emailAuth(String uuid);

  // 입력한 이메일로 비밀번호를 재설정하는 메일을 보냄.
  boolean sendResetPassword(ResetPasswordInput parameter);

  boolean resetPassword(String id, String password);

  boolean checkResetPassword(String uuid);

  List<Member> list();
}
