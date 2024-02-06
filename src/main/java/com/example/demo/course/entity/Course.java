package com.example.demo.course.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
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
public class Course {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  long categoryId;

  String imagePath;
  String keyword;
  String subject;

  @Column(length = 1000)
  String summary;

  @Lob
  String contents;
  long price;
  long salePrice;
  LocalDate saleEndDt;

  LocalDateTime regDt;//등록일(추가날짜)
  LocalDateTime udtDt;//수정일(수정날짜)


  String filename;
  String urlFilename;
}
