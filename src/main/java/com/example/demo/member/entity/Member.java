package com.example.demo.member.entity;


import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Member implements MemberCode{

  @Id
  private String userId;
  private String userName;
  private String phone;
  private String password;
  private LocalDateTime regDt;
  private LocalDateTime udtDt;//회원 정보 수정일

  private boolean emailAuthYn;
  private LocalDateTime emailAuthDt;
  private String emailAuthKey;

  private String resetPasswordKey;
  private LocalDateTime resetPasswordLimitDt;

  private boolean adminYn;

  private String userStatus;//이용 가능한 상태, 정지 상태


  private String zipcode;
  private String addr;
  private String addrDetail;
}
