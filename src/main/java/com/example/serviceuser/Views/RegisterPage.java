package com.example.serviceuser.Views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H6;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Route("RegisterPage")

public class RegisterPage extends  Div {
    public RegisterPage() {
        Div mainContainer = new Div();
        mainContainer.getStyle().set("display", "flex");

        mainContainer.getStyle().set("height", "100vh"); // 100% of the viewport height

        // Create orange background on the left (1/3 of the height)

        Div leftSection = new Div();
        Image logoImage = new Image("/images/Logo.png", "Logo");
        logoImage.getStyle().set("width", "300px").set("height", "150px").set("margin-left", "120px");

        H1 headerRegister = new H1("Register");
        Button registerButton = new Button("register");

        registerButton.getStyle().set("background-color", "#5F9DB2").set("color", "white").set("border", "1px solid white").set("border-radius", "5px").set("width", "400px").set("margin-left", "65px").set("margin-top", "50px");


        headerRegister.getStyle().set("text-align", "center").set("margin-top", "100px").set("color", "white");
        leftSection.getStyle().set("background-color", "#5F9DB2");
        leftSection.getStyle().set("flex", "1");
        leftSection.getStyle().set("height", "100%"); // 1/3 of the height
        leftSection.add(logoImage, headerRegister, registerButton);


        // Create login form on the right (2/3 of the height)


        FormLayout loginForm = new FormLayout();
        H1 header = new H1("Register");
        RouterLink login = new RouterLink("Login", MainView.class);
        login.getStyle().set("text-align", "center").set("margin-top", "20px");

        header.getStyle().set("text-align", "center");
        TextField emailField = new TextField("Email");
        PasswordField passwordField = new PasswordField("Password");
        TextField usernameField = new TextField("Username");
        RadioButtonGroup<String> genderRadio = new RadioButtonGroup<>("Gender", "Male", "Female");


        Button registersButton = new Button("Register");

        loginForm.add(header, emailField, passwordField, usernameField, genderRadio, registersButton, login);
        loginForm.getStyle().set("flex", "2"); // 2/3 of the height
        loginForm.getStyle().set("width", "500px");
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(loginForm);

        horizontalLayout.getStyle().set("margin", "200px");

        // Add sections to the main container
        mainContainer.add(leftSection, horizontalLayout);

        // Add the main container to the view
        add(mainContainer);

        registersButton.addClickListener(event -> {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

            String email = emailField.getValue();
            String password = passwordField.getValue();
            String username = usernameField.getValue();
            String gender = genderRadio.getValue();

            System.out.println(gender);


            if (password != "" || email != "" || username != "" || gender != null) {
                formData.add("email", email);
                formData.add("password", password);
                formData.add("username", username);
                formData.add("gender", gender);

                System.out.println(formData);

                Map<String, Object> userList = WebClient.create()
                        .post()
                        .uri("http://localhost:8080/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .body(BodyInserters.fromFormData(formData))
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                        })
                        .block();

                System.out.println(userList);
            } else {
                System.out.println("ใส่ข้อมูลไม่ครบ");
            }


        });
    }

}
