package com.reloadly.paypro.combined.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupRequest {

    private String email;

    private String username;

    private String password;

    private String phoneNumber;

}
