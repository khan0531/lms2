package com.example.demo.admin.dto;

import com.example.demo.admin.entity.Category;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryDto {
    
    Long id;
    String categoryName;
    int sortValue;
    boolean usingYn;
    
    
    //ADD COLUMNS
    int courseCount;
    
    
    public static List<CategoryDto> of (List<Category> categories) {
        if (categories != null) {
            List<CategoryDto> categoryList = new ArrayList<>();
            for(Category x : categories) {
                categoryList.add(of(x));
            }
            return categoryList;
        }
        
        return null;
    }
    
    public static CategoryDto of(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .categoryName(category.getCategoryName())
                .sortValue(category.getSortValue())
                .usingYn(category.isUsingYn())
                .build();
    }
    
    
}
