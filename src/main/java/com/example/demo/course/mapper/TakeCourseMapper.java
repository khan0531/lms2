package com.example.demo.course.mapper;

import com.example.demo.course.dto.TakeCourseDto;
import com.example.demo.course.model.TakeCourseParam;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TakeCourseMapper {
    
    long selectListCount(TakeCourseParam parameter);
    List<TakeCourseDto> selectList(TakeCourseParam parameter);
    
    List<TakeCourseDto> selectListMyCourse(TakeCourseParam parameter);
}
