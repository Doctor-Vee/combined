package com.reloadly.paypro.combined.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequest {

    private String username;

    private String password;

}
