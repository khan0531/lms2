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
public class Member {

  @Id
  private String userId;

  private String userName;
  private String password;
  private String phone;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  private boolean emailAuthYn;
  private LocalDateTime emailAuthDt;
  private String emailAuthKey;
}
