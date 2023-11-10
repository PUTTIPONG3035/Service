package com.example.serviceuser.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document("User")
public class User {


    @Id
    @JsonProperty("id")
    private String _id;
    @JsonProperty("email")

    private String email;
    @JsonProperty("password")

    private String password;
    @JsonProperty("time")

    private Date time;
    @JsonProperty("username")

    private String username;
    @JsonProperty("gender")

    private String gender;
    @JsonProperty("image")

    private String image;
    @JsonProperty("credit")

    private int credit;
    @JsonProperty("role")

    private Role role;


    public User(@JsonProperty("id") String _id, @JsonProperty("email") String email, @JsonProperty("password")String password, @JsonProperty("time") Date time, @JsonProperty("username") String username, @JsonProperty("gender") String gender, @JsonProperty("image") String image, @JsonProperty("credit") int credit, @JsonProperty("role") Role role) {
        this._id = _id;
        this.email = email;
        this.password = password;
        this.time = time;
        this.username = username;
        this.gender = gender;
        this.image = image;
        this.credit = credit;
        this.role = role;

    }
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
