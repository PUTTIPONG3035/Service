package com.example.serviceuser.controller;



import com.example.serviceuser.pojo.RegisterUser;
import com.example.serviceuser.pojo.Role;
import com.example.serviceuser.pojo.User;
import com.example.serviceuser.repository.JwtService;
import com.example.serviceuser.repository.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginUser(@RequestBody MultiValueMap<String, String> formdata, HttpServletResponse response) {
        try{

            Map<String, String> data = formdata.toSingleValueMap();

            String email = data.get("email");
            String password = data.get("password");





            User user = this.userService.CheckLogin(
                    email, password
            );
            System.out.println("Folk");
            if(user != null){
                String token = jwtService.generateToken(email);

                return token;
            }

            else{

                return  "ไม่มีเมลนี้นะ";
            }
        }

        catch(Exception e){

            return  e.getMessage();
        }
    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Map<String, Object> registerUser(@RequestBody MultiValueMap<String, String> formdata){
//        System.out.println("Received register request:");
        try{

            Map<String, String> data = formdata.toSingleValueMap();



            String email = data.get("email");
            String password = data.get("password");
            Date time = new Date();
            String username = data.get("username");
            String gender = data.get("gender");
            String image = "https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Gatto_europeo4.jpg/800px-Gatto_europeo4.jpg";
            int credit = 0;


//         BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//         String encodedPassword = passwordEncoder.encode(password);


            User user = this.userService.findUserWithEmail(email);



            if(user == null){
                User newUser = new User(null, email, password, time, username, gender, image, credit, Role.USER );

                userService.addUser(newUser);
                Map<String, Object> response = new HashMap<>();
                response.put("Message", "สมัครสำเร็จ");

                return  response;

            }
            else{
                Map<String, Object> response = new HashMap<>();

                response.put("Message" , "emailHave");

                return response;


            }
        }
        catch(Exception e){

            System.out.println(e);

            Map<String, Object> error = new HashMap<>();

            error.put("Message", "ระบบผิดพลาดอย่างแรง");

            return error;
        }





    }

    @RequestMapping(value = "/forgot", method = RequestMethod.POST)
    public Map<String, Object> forgotPassword(@RequestBody MultiValueMap<String, String> formdata){
        try{
            Map<String, String> data = formdata.toSingleValueMap();
            String email = data.get("email");
            String password = data.get("password");
            User user = this.userService.findUserWithEmail(email);
            if(user != null){
                user.setPassword(password);

                System.out.println(user.getPassword());
                userService.updateUser(user);

                Map<String, Object> response =new HashMap<>();
                response.put("message", "updateComplete");
                return response;

            }

            else{

                Map<String, Object> response = new HashMap<>();
                response.put("message", "ไม่มีอีเมลนี้");

                return response;

            }

        }
        catch (Exception e){
            Map<String, Object> response = new HashMap<>();

            response.put("message", e);

            return  response;
        }
    }
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    public Map<String, Object> updateUser(@RequestBody RegisterUser registerUser){


        try{
            String email = registerUser.getEmail();
            String password = registerUser.getPassword();

            String username = registerUser.getUsername();
            String gender = registerUser.getGender();
            String image = registerUser.getImage();
            int credit = registerUser.getCredit();
//            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//            String encodedPassword = passwordEncoder.encode(password);
            User user = this.userService.findUserWithEmail(email);

            if(user != null){

                user.setPassword(password);
                user.setImage(image);
                user.setCredit(credit);
                user.setGender(gender);
                user.setUsername(username);

                userService.updateUser(user);

                Map<String, Object> response = new HashMap<>();
                response.put("message", "แก้ไขขัอมูลสำเร็จ");

                return response;

            }

            else{

                Map<String, Object> response = new HashMap<>();
                response.put("message", "ไม่มีอีเมลนี้");

                return response;


            }
        }

        catch (Exception e){
            Map<String, Object> response = new HashMap<>();

            response.put("message", e);

            return  response;
        }
    }

    @RequestMapping(value ="/me", method = RequestMethod.GET)
    public Optional<User> getUser(HttpServletRequest request) {
        try {

            System.out.println("Token : " + request.getHeader("Authorization"));

            String authorizationHeader = request.getHeader("Authorization");
            String[] token = authorizationHeader.split(" ");
            Claims claims = jwtService.parseToken(token[1]);
            System.out.println(claims.getSubject().toString());
            return this.userService.Me(claims.getSubject().toString());
        } catch (Exception e) {
            return null;
        }
    }




    @RequestMapping(value = "/AllUser", method = RequestMethod.GET)
    public ArrayList<User> getUsers(){
        try{
            return this.userService.getAllUsers().users;

        }
        catch(Exception e){
            return null;
        }

    }


    @RequestMapping(value ="/file", method = RequestMethod.POST)
   public ResponseEntity<String> storeFilesIntoDB(@RequestParam("file") MultipartFile file) throws IOException {

       String response =  userService.storeFile(file);

       return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(value = "/getFileByName/{filename}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getImage(@PathVariable String filename){
        byte[] imageData = userService.getFiles(filename);

        System.out.println(imageData);

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(imageData);
    }




//    public boolean CheckLogin(String userEmail, String userPassword){
//        String email = "flok3035@gmail.com";
//        String password = "Folk1234";
//        if(email.equals(userEmail) && password.equals(userPassword)){
//            return  true;
//        }
//        return false;
//    }
}
