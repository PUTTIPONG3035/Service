package com.example.serviceuser.Views;

import com.example.serviceuser.pojo.User;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H6;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.internal.PendingJavaScriptInvocation;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;


@Route("MainViews")
public class MainView extends Div{







    public MainView() {
        Div mainContainer = new Div();
        mainContainer.getStyle().set("display", "flex");

        mainContainer.getStyle().set("height", "100vh");



        Div leftSection = new Div();
        Image logoImage = new Image("/images/Logo.png", "Logo");
        logoImage.getStyle().set("width" ,"300px").set("height", "150px").set("margin-left", "120px");

        H1 headerRegister = new H1("Register");
        Button registerButton = new Button("register");

        registerButton.getStyle().set("background-color", "#5F9DB2").set("color", "white").set("border", "1px solid white").set("border-radius", "5px").set("width", "400px").set("margin-left", "65px").set("margin-top", "50px");




        headerRegister.getStyle().set("text-align", "center").set("margin-top", "100px").set("color", "white");
        leftSection.getStyle().set("background-color", "#5F9DB2");
        leftSection.getStyle().set("flex", "1");
        leftSection.getStyle().set("height", "100%");
        leftSection.add(logoImage, headerRegister, registerButton);



        FormLayout loginForm = new FormLayout();
        H1 header = new H1("Log in");

        RouterLink forgot = new RouterLink("Forgot Password", ForgotPage.class);
        header.getStyle().set("text-align", "center");
        TextField emailField = new TextField("Username");
        PasswordField passwordField = new PasswordField("Password");
        Button loginButton = new Button("Login");

        loginForm.add(header, emailField, passwordField, loginButton, forgot);
        forgot.getStyle().set("text-align", "center").set("margin-top", "20px");
        loginForm.getStyle().set("flex", "2"); // 2/3 of the height
        loginForm.getStyle().set("width", "500px");
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(loginForm);

        horizontalLayout.getStyle().set("margin", "200px");


        mainContainer.add(leftSection, horizontalLayout);

        add(mainContainer);








        loginButton.addClickListener(event -> {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

            String email = emailField.getValue();
            String password = passwordField.getValue();

            if (!email.isEmpty() && !password.isEmpty()) {
                formData.add("email", email);
                formData.add("password", password);
                System.out.println(email);

                String token = WebClient.create()
                        .post()
                        .uri("http://localhost:8080/login")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .body(BodyInserters.fromFormData(formData))
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();

                System.out.println(token);



                if( token.length() > 20){
                    try{

                        UI.getCurrent().access(() -> {
                            UI.getCurrent().getPage().executeJs("localStorage.setItem($0, $1)", "token", token);


                            UI.getCurrent().navigate(UserProfile.class);
                        });
                    }
                    catch(Exception e){
                        System.out.println(e);
                    }

                }
                




            }
        });


















    }



//    private void LoadPage(){
//





//
//        // Set up the request headers with the JWT token
//        HttpHeaders headerss = new HttpHeaders();
//        headerss.set("Authorization", "Bearer " + token);
//
//        // Make a GET request to the "/me" endpoint using WebClient
//        User user = WebClient.builder()
//
//                .defaultHeaders(headers -> headers.addAll(headerss))
//                .build()
//                .get()
//                .uri("http://localhost:8080/me")
//                .retrieve()
//                .bodyToMono(User.class)
//                .block();
//
//
//
//
//        System.out.println(user);


//    }


}
