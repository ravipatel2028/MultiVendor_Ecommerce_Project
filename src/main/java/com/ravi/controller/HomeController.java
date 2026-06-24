package com.ravi.controller;

import com.ravi.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping
    public ApiResponse home() {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Hello World! This is my ecom-multivendor system. :)");
        return apiResponse;
    }
}
