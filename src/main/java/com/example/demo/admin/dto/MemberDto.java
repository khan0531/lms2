package com.example.demo.admin.dto;

import com.example.demo.member.entity.Member;
import java.time.LocalDateTime;
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
  private String userId;
  private String userName;
  private String password;
  private String phone;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  private boolean emailAuthYn;
  private LocalDateTime emailAuthDt;
  private String emailAuthKey;

  private String resetPasswordKey;
  private LocalDateTime resetPasswordLimitDt;

  private boolean adminYn;
  String userStatus;

  long totalCount;
  long seq;

  public static MemberDto of(Member member) {
    return MemberDto.builder()
        .userId(member.getUserId())
        .userName(member.getUserName())
        .phone(member.getPhone())
        .password(member.getPassword())
        .createdAt(member.getCreatedAt())
        .updatedAt(member.getUpdatedAt())

        .emailAuthYn(member.isEmailAuthYn())
        .emailAuthDt(member.getEmailAuthDt())
        .emailAuthKey(member.getEmailAuthKey())

        .resetPasswordKey(member.getResetPasswordKey())
        .resetPasswordLimitDt(member.getResetPasswordLimitDt())

        .adminYn(member.isAdminYn())
        .userStatus(member.getUserStatus())
        .build();
  }
}
