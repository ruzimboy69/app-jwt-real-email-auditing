package com.example.appjwtrealemailauditing.controller;

import com.example.appjwtrealemailauditing.payload.LoginDto;
import com.example.appjwtrealemailauditing.payload.RegisterDto;
import com.example.appjwtrealemailauditing.service.ApiResponse;
import com.example.appjwtrealemailauditing.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthService authService;
    @PostMapping("/register")
    public HttpEntity<?> RegisterUser(@RequestBody RegisterDto registerDto){
        ApiResponse apiResponse = authService.registerUser(registerDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 :409).body(apiResponse);

    }
    @GetMapping("/verifyEmail")
    public HttpEntity<?> VerifyEmail(@RequestParam String emailCode, @RequestParam String email){
        ApiResponse apiResponse = authService.verifyEmail(emailCode, email);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 :409).body(apiResponse);
    }
    @PostMapping
    public HttpEntity<?> login(@RequestBody LoginDto loginDto){

        ApiResponse apiResponse = authService.login(loginDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200:401).body(apiResponse);
    }
}
