package com.example.serviceuser.pojo;

import lombok.Data;

@Data
public class Token {

   

    private String localToken;
    public Token(){
        
    }
    public String getLocalToken() {
        return localToken;
    }

    public void setLocalToken(String localToken) {
        this.localToken = localToken;
    }
    

    
    
}
