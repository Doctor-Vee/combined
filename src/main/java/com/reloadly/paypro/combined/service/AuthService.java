package com.reloadly.paypro.combined.service;

import com.reloadly.paypro.combined.payload.request.LoginRequest;
import com.reloadly.paypro.combined.payload.request.SignupRequest;
import com.reloadly.paypro.combined.payload.response.LoginResponse;

public interface AuthService {

    String processSignup(SignupRequest signupRequest);

    LoginResponse processLogin(LoginRequest loginRequest);

}
