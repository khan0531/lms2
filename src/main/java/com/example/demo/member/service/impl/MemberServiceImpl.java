package com.example.demo.member.service.impl;

import com.example.demo.components.MailComponents;
import com.example.demo.member.entity.Member;
import com.example.demo.member.model.MemberInput;
import com.example.demo.member.repository.MemberRepository;
import com.example.demo.member.service.MemberService;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

  private final MemberRepository memberRepository;
  private final MailComponents mailComponents;

  @Override
  public boolean register(MemberInput parameter) {

    Optional<Member> optionalMember = memberRepository.findById(parameter.getUserId());
    if (optionalMember.isPresent()) {
      return false;
    }

    String uuid = UUID.randomUUID().toString();

    Member member = Member.builder()
        .userId(parameter.getUserId())
        .userName(parameter.getUserName())
        .password(parameter.getPassword())
        .phone(parameter.getPhone())
        .emailAuthYn(false)
        .emailAuthKey(uuid)
        .createdAt(LocalDateTime.now())
        .build();
    memberRepository.save(member);

    String email = parameter.getUserId();
    String subject = "회원가입을 축하드립니다.";
    String text = "<p>회원가입을 축하드립니다.<p><p>아래 링크를 클릭하시면 이메일 인증이 완료됩니다.</p>"
        + "<div><a target='_blank' href=\"http://localhost:8080/member/email-auth?id=" + uuid + "'>가입 완료</a></div>";
    mailComponents.sendMail(email, subject, text);

    return true;
  }

  @Override
  public boolean emailAuth(String uuid) {

    Optional<Member> optionalMember = memberRepository.findByEmailAuthKey(uuid);
    if (!optionalMember.isPresent()) {
      return false;
    }
    Member member = optionalMember.get();
    member.setEmailAuthYn(true);
    member.setEmailAuthDt(LocalDateTime.now());
    memberRepository.save(member);

    return true;
  }
}
