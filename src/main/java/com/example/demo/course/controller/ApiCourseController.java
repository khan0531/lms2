package com.example.demo.course.controller;


import com.example.demo.admin.service.CategoryService;
import com.example.demo.course.model.ServiceResult;
import com.example.demo.course.model.TakeCourseInput;
import com.example.demo.course.service.CourseService;
import com.example.demo.common.model.ResponseResult;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ApiCourseController extends BaseController {
    
    private final CourseService courseService;
    private final CategoryService categoryService;
    
    @PostMapping("/api/course/req.api")
    public ResponseEntity<?> courseReq(Model model
            , @RequestBody TakeCourseInput parameter
            , Principal principal) {
        
        parameter.setUserId(principal.getName());
    
        ServiceResult result = courseService.req(parameter);
        if (!result.isResult()) {
            ResponseResult responseResult = new ResponseResult(false, result.getMessage());
            return ResponseEntity.ok().body(responseResult);
        }
    
        ResponseResult responseResult = new ResponseResult(true);
        return ResponseEntity.ok().body(responseResult);
    }
    
    
}
