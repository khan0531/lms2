package com.example.demo.member.service.impl;

import com.example.demo.admin.dto.MemberDto;
import com.example.demo.admin.mapper.MemberMapper;
import com.example.demo.admin.model.MemberParam;
import com.example.demo.components.MailComponents;
import com.example.demo.course.model.ServiceResult;
import com.example.demo.member.entity.Member;
import com.example.demo.member.entity.MemberCode;
import com.example.demo.member.exception.MemberNotEmailAuthException;
import com.example.demo.member.exception.MemberStopUserException;
import com.example.demo.member.model.MemberInput;
import com.example.demo.member.model.ResetPasswordInput;
import com.example.demo.member.repository.MemberRepository;
import com.example.demo.member.service.MemberService;
import com.example.demo.util.PasswordUtils;
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
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

  private final MemberRepository memberRepository;
  private final MailComponents mailComponents;
  private final MemberMapper memberMapper;

  @Override
  public ServiceResult withdraw(String userId, String password) {

    Optional<Member> optionalMember = memberRepository.findById(userId);
    if (!optionalMember.isPresent()) {
      return new ServiceResult(false, "회원 정보가 존재하지 않습니다.");
    }

    Member member = optionalMember.get();

    if (!PasswordUtils.equals(password, member.getPassword())) {
      return new ServiceResult(false, "비밀번호가 일치하지 않습니다.");
    }

    member.setUserName("삭제회원");
    member.setPhone("");
    member.setPassword("");
    member.setRegDt(null);
    member.setUdtDt(null);
    member.setEmailAuthYn(false);
    member.setEmailAuthDt(null);
    member.setEmailAuthKey("");
    member.setResetPasswordKey("");
    member.setResetPasswordLimitDt(null);
    member.setUserStatus(MemberCode.MEMBER_STATUS_WITHDRAW);
    member.setZipcode("");
    member.setAddr("");
    member.setAddrDetail("");
    memberRepository.save(member);

    return new ServiceResult();
  }

  @Override
  public ServiceResult updateMember(MemberInput parameter) {

    String userId = parameter.getUserId();

    Optional<Member> optionalMember = memberRepository.findById(userId);
    if (!optionalMember.isPresent()) {
      return new ServiceResult(false, "회원 정보가 존재하지 않습니다.");
    }

    Member member = optionalMember.get();

    member.setPhone(parameter.getPhone());
    member.setZipcode(parameter.getZipcode());
    member.setAddr(parameter.getAddr());
    member.setAddrDetail(parameter.getAddrDetail());
    member.setUdtDt(LocalDateTime.now());
    memberRepository.save(member);

    return new ServiceResult();
  }

  @Override
  public ServiceResult updateMemberPassword(MemberInput parameter) {

    String userId = parameter.getUserId();

    Optional<Member> optionalMember = memberRepository.findById(userId);
    if (!optionalMember.isPresent()) {
      return new ServiceResult(false, "회원 정보가 존재하지 않습니다.");
    }

    Member member = optionalMember.get();

    if (!PasswordUtils.equals(parameter.getPassword(), member.getPassword())) {
      return new ServiceResult(false, "비밀번호가 일치하지 않습니다.");
    }

    String encPassword = PasswordUtils.encPassword(parameter.getNewPassword());
    member.setPassword(encPassword);
    memberRepository.save(member);

    return new ServiceResult(true);
  }

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

    long totalCount = memberMapper.selectListCount(parameter);
    List<MemberDto> list = memberMapper.selectList(parameter);
    if (!CollectionUtils.isEmpty(list)) {
      int i = 0;
      for(MemberDto x : list) {
        x.setTotalCount(totalCount);
        x.setSeq(totalCount - parameter.getPageStart() - i);
        i++;
      }
    }
    return list;
  }

  @Override
  public MemberDto detail(String userId) {

    Optional<Member> optionalMember  = memberRepository.findById(userId);
    if (!optionalMember.isPresent()) {
      return null;
    }

    Member member = optionalMember.get();

    return MemberDto.of(member);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    Optional<Member> optionalMember = memberRepository.findById(username);
    if (!optionalMember.isPresent()) {
      throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
    }

    Member member = optionalMember.get();
    if (member.getUserStatus().equals(Member.MEMBER_STATUS_REQ)) {
      throw new MemberNotEmailAuthException("이메일 인증이 완료되지 않았습니다.");
    }

    if (member.getUserStatus().equals(Member.MEMBER_STATUS_STOP)) {
      throw new MemberStopUserException("정지된 계정입니다.");
    }

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
      //현재 userId에 해당하는 데이터 존재
      return false;
    }

    String encPassword = BCrypt.hashpw(parameter.getPassword(), BCrypt.gensalt());
    String uuid = UUID.randomUUID().toString();

    Member member = Member.builder()
        .userId(parameter.getUserId())
        .userName(parameter.getUserName())
        .phone(parameter.getPhone())
        .password(encPassword)
        .regDt(LocalDateTime.now())
        .emailAuthYn(false)
        .emailAuthKey(uuid)
        .userStatus(Member.MEMBER_STATUS_REQ)
        .build();
    memberRepository.save(member);

    String email = parameter.getUserId();
    String subject = "fastlms 사이트 가입을 축하드립니다. ";
    String text = "<p>fastlms 사이트 가입을 축하드립니다.<p><p>아래 링크를 클릭하셔서 가입을 완료 하세요.</p>"
        + "<div><a target='_blank' href='http://localhost:8080/member/email-auth?id=" + uuid + "'> 가입 완료 </a></div>";
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
    member.setUserStatus(Member.MEMBER_STATUS_ING);
    memberRepository.save(member);

    return true;
  }

  @Override
  public boolean updateStatus(String userId, String userStatus) {

    Optional<Member> optionalMember = memberRepository.findById(userId);
    if (!optionalMember.isPresent()) {
      throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
    }

    Member member = optionalMember.get();

    member.setUserStatus(userStatus);
    memberRepository.save(member);

    return true;
  }

  @Override
  public boolean updatePassword(String userId, String password) {

    Optional<Member> optionalMember = memberRepository.findById(userId);
    if (!optionalMember.isPresent()) {
      throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
    }

    Member member = optionalMember.get();

    String encPassword = BCrypt.hashpw(password, BCrypt.gensalt());
    member.setPassword(encPassword);
    memberRepository.save(member);

    return true;

  }

}
