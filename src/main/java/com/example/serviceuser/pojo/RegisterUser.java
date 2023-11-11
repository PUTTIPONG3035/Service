package com.example.serviceuser.pojo;


import lombok.Data;

@Data
public class RegisterUser {

    private  String email;
    private  String password;
    private  String username;
    private  String gender;
    private  String image;
    private   int credit;

    public RegisterUser() {
        this.email = email;
        this.password = password;
        this.username = username;
        this.gender = gender;
        this.image = image;
        this.credit = credit;
    }
}
