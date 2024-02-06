package com.example.demo.course.mapper;

import com.example.demo.course.dto.CourseDto;
import com.example.demo.course.model.CourseParam;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CourseMapper {
    
    long selectListCount(CourseParam parameter);
    List<CourseDto> selectList(CourseParam parameter);
    
}
