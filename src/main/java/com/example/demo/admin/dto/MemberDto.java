package com.example.demo.admin.dto;

import com.example.demo.member.entity.Member;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {

  String userId;
  String userName;
  String phone;
  String password;
  LocalDateTime regDt;
  LocalDateTime udtDt;

  boolean emailAuthYn;
  LocalDateTime emailAuthDt;
  String emailAuthKey;

  String resetPasswordKey;
  LocalDateTime resetPasswordLimitDt;

  boolean adminYn;
  String userStatus;

  private String zipcode;
  private String addr;
  private String addrDetail;

  //추가컬럼
  long totalCount;
  long seq;


  public static MemberDto of(Member member) {

    return MemberDto.builder()
        .userId(member.getUserId())
        .userName(member.getUserName())
        .phone(member.getPhone())
        //.password(member.getPassword())
        .regDt(member.getRegDt())
        .udtDt(member.getUdtDt())
        .emailAuthYn(member.isEmailAuthYn())
        .emailAuthDt(member.getEmailAuthDt())
        .emailAuthKey(member.getEmailAuthKey())
        .resetPasswordKey(member.getResetPasswordKey())
        .resetPasswordLimitDt(member.getResetPasswordLimitDt())
        .adminYn(member.isAdminYn())
        .userStatus(member.getUserStatus())

        .zipcode(member.getZipcode())
        .addr(member.getAddr())
        .addrDetail(member.getAddrDetail())

        .build();
  }


  public String getRegDtText() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
    return regDt != null ? regDt.format(formatter) : "";
  }

  public String getUdtDtText() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
    return udtDt != null ? udtDt.format(formatter) : "";

  }
}
