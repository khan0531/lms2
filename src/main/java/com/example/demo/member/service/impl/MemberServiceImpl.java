package com.example.demo.member.service.impl;

import com.example.demo.admin.dto.MemberDto;
import com.example.demo.admin.mapper.MemberMapper;
import com.example.demo.admin.model.MemberParam;
import com.example.demo.components.MailComponents;
import com.example.demo.member.entity.Member;
import com.example.demo.member.exception.MemberNotEmailAuthException;
import com.example.demo.member.model.MemberInput;
import com.example.demo.member.model.ResetPasswordInput;
import com.example.demo.member.repository.MemberRepository;
import com.example.demo.member.service.MemberService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

  private final MemberRepository memberRepository;
  private final MailComponents mailComponents;
  private final MemberMapper memberMapper;

  @Override
  public boolean sendResetPassword(ResetPasswordInput parameter) {

    Optional<Member> optionalMember = memberRepository.findByUserIdAndUserName(parameter.getUserId(),
        parameter.getUserName());
    if (!optionalMember.isPresent()) {
      throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
    }
    Member member = optionalMember.get();

    String uuid = UUID.randomUUID().toString();

    member.setResetPasswordKey(uuid);
    member.setResetPasswordLimitDt(LocalDateTime.now().plusHours(1));
    memberRepository.save(member);

    String email = parameter.getUserId();
    String subject = "비밀번호 초기화 메일 입니다.";
    String text = "<p>비밀번호 초기화 메일 입니다.<p>"+
        "<p>아래 링크를 클릭하여 비밀번호를 초기화 해주세요.</p>" +
        "<div><a target='_blank' href=\"http://localhost:8080/member/reset/password?id=" + uuid +
        "'>비밀번호 초기화 링크</a></div>";
    mailComponents.sendMail(email, subject, text);

    return true;
  }

  @Override
  public boolean resetPassword(String uuid, String password) {
    Optional<Member> optionalMember = memberRepository.findByResetPasswordKey(uuid);
    if (!optionalMember.isPresent()) {
      throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
    }
    Member member = optionalMember.get();

    if (member.getResetPasswordLimitDt().isEqual(null)) {
      throw new RuntimeException("유효한 날짜가 아닙니다.");
    }

    if (member.getResetPasswordLimitDt().isBefore(LocalDateTime.now())) {
      throw new RuntimeException("유효한 날짜가 아닙니다.");
    }

    String encodedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
    member.builder()
            .password(encodedPassword)
            .resetPasswordKey(null)
            .resetPasswordLimitDt(null)
            .build();
    memberRepository.save(member);

    return true;
  }

  @Override
  public boolean checkResetPassword(String uuid) {
    Optional<Member> optionalMember = memberRepository.findByResetPasswordKey(uuid);
    if (!optionalMember.isPresent()) {
      return false;
    }
    Member member = optionalMember.get();

    if (member.getResetPasswordLimitDt().isEqual(null)) {
      throw new RuntimeException("유효한 날짜가 아닙니다.");
    }

    if (member.getResetPasswordLimitDt().isBefore(LocalDateTime.now())) {
      throw new RuntimeException("유효한 날짜가 아닙니다.");
    }

    return true;
  }

  @Override
  public List<MemberDto> list(MemberParam parameter) {

    return memberMapper.selectList(parameter);

//    return memberRepository.findAll();
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    Optional<Member> optionalMember = memberRepository.findById(username);
    if (!optionalMember.isPresent()) {
      throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
    }

    Member member = optionalMember.get();

    if (!member.isEmailAuthYn()) {
      throw new MemberNotEmailAuthException("이메일 인증이 완료되지 않았습니다.");
    }

    List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
    grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

    if (member.isAdminYn()) {
      grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    return new User(member.getUserId(), member.getPassword(), grantedAuthorities);
  }

  @Override
  public boolean register(MemberInput parameter) {

    Optional<Member> optionalMember = memberRepository.findById(parameter.getUserId());
    if (optionalMember.isPresent()) {
      return false;
    }

    String encodedPassword = BCrypt.hashpw(parameter.getPassword(), BCrypt.gensalt());

    String uuid = UUID.randomUUID().toString();

    Member member = Member.builder()
        .userId(parameter.getUserId())
        .userName(parameter.getUserName())
        .password(encodedPassword)
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

    if (member.isEmailAuthYn()) {
      return false;
    }

    member.setEmailAuthYn(true);
    member.setEmailAuthDt(LocalDateTime.now());
    memberRepository.save(member);

    return true;
  }


}
