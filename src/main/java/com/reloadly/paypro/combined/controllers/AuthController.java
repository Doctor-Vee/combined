package com.reloadly.paypro.combined.controllers;

import com.reloadly.paypro.combined.payload.request.LoginRequest;
import com.reloadly.paypro.combined.payload.request.SignupRequest;
import com.reloadly.paypro.combined.payload.response.LoginResponse;
import com.reloadly.paypro.combined.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody SignupRequest signupRequest){
        String response = authService.processSignup(signupRequest);
        return ResponseEntity.created(URI.create("")).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginRequest loginRequest){
        LoginResponse response = authService.processLogin(loginRequest);
        return ResponseEntity.ok(response);
    }

}
