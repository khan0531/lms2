package com.example.demo.member.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MemberInput {
  private String userId;
  private String userName;
  private String phone;
  private String password;

  private String newPassword;

  private String zipcode;
  private String addr;
  private String addrDetail;
}
