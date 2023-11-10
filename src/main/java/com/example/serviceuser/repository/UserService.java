package com.example.serviceuser.repository;

import com.example.serviceuser.pojo.User;
import com.example.serviceuser.pojo.Users;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository respository){
        this.repository = respository;
    }

    public User findUserWithEmail(String email) {
        return this.repository.findUserWithEmail(email);
    }


    public Users getAllUsers() {
        Users users = new Users();
        users.users = (ArrayList<User>) this.repository.findAll();
        return users;
    }

    public User CheckLogin(String email, String password) {
        return this.repository.checkLogin(email, password);
    }

    public Optional<User> Me(String email) {
        return Optional.ofNullable(this.repository.findUserWithEmail(email));
    }

    public void addUser(User user){
        repository.insert(user);
    }

    public void updateUser(User user){
        repository.save(user);
    }
}
