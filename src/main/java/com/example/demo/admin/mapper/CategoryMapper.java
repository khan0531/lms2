package com.example.demo.admin.mapper;


import com.example.demo.admin.dto.CategoryDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper {

    List<CategoryDto> select(CategoryDto parameter);

}
