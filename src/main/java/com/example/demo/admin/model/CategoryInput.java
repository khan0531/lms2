package com.example.demo.admin.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CategoryInput {
    
    long id;
    String categoryName;
    int sortValue;
    boolean usingYn;
    
}
