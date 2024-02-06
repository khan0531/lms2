package com.example.demo.course.controller;


import com.example.demo.admin.dto.CategoryDto;
import com.example.demo.admin.service.CategoryService;
import com.example.demo.course.dto.CourseDto;
import com.example.demo.course.model.CourseParam;
import com.example.demo.course.service.CourseService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class CourseController extends BaseController {
    
    private final CourseService courseService;
    private final CategoryService categoryService;
    
    @GetMapping("/course")
    public String course(Model model
            , CourseParam parameter) {
        
        List<CourseDto> list = courseService.frontList(parameter);
        model.addAttribute("list", list);
        
        int courseTotalCount = 0;
        List<CategoryDto> categoryList = categoryService.frontList(CategoryDto.builder().build());
        if (categoryList != null) {
            for(CategoryDto x : categoryList) {
                courseTotalCount += x.getCourseCount();
            }
        }
        
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("courseTotalCount", courseTotalCount);
        
        return "course/index";
    }
    
    @GetMapping("/course/{id}")
    public String courseDetail(Model model
            , CourseParam parameter) {
        
        CourseDto detail = courseService.frontDetail(parameter.getId());
        model.addAttribute("detail", detail);
        
        return "course/detail";
    }
    
    
    
    
    
    
}
