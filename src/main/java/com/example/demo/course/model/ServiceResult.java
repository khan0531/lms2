package com.example.demo.course.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ServiceResult {

    boolean result;
    String message;
    
    public ServiceResult() {
        result = true;
    }
    
    public ServiceResult(boolean result, String message) {
        this.result = result;
        this.message = message;
    }
    
    public ServiceResult(boolean result) {
        this.result = result;
    }
}
