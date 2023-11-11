package com.example.serviceuser.pojo;

import lombok.Data;

@Data
public class ForgotPassword {

    private String email;
    private String password;
    private String confirmPassword;
}
