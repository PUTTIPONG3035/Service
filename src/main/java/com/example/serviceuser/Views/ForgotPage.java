package com.example.serviceuser.Views;

import com.example.serviceuser.pojo.ForgotPassword;
import com.example.serviceuser.pojo.User;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.page.History;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import javax.print.attribute.standard.Media;
import java.util.List;
import java.util.Map;

@Route("ForgotPage")
public class ForgotPage extends Div {
    public ForgotPage() {
        // Create main container
        Div mainContainer = new Div();
        mainContainer.getStyle().set("display", "flex");

        mainContainer.getStyle().set("height", "100vh"); // 100% of the viewport height

        // Create orange background on the left (1/3 of the height)

        Div leftSection = new Div();
        Image logoImage = new Image("/images/Logo.png", "Logo");
        logoImage.getStyle().set("width" ,"300px").set("height", "150px").set("margin-left", "120px");

        H1 headerRegister = new H1("Register");
        Button registerButton = new Button("register", e -> navigateToMainView());

        registerButton.getStyle().set("background-color", "#5F9DB2").set("color", "white").set("border", "1px solid white").set("border-radius", "5px").set("width", "400px").set("margin-left", "65px").set("margin-top", "50px");




        headerRegister.getStyle().set("text-align", "center").set("margin-top", "100px").set("color", "white");
        leftSection.getStyle().set("background-color", "#5F9DB2");
        leftSection.getStyle().set("flex", "1");
        leftSection.getStyle().set("height", "100%"); // 1/3 of the height
        leftSection.add(logoImage, headerRegister, registerButton);


        // Create login form on the right (2/3 of the height)
        FormLayout loginForm = new FormLayout();
        H1 header = new H1("Forgot Password");
        RouterLink back = new RouterLink("Back to Login", MainView.class);


        back.getStyle().set("text-align", "center").set("margin-top", "20px");

        header.getStyle().set("text-align", "center");
        TextField emailField = new TextField("Email");
        PasswordField passwordField = new PasswordField("Password");
        PasswordField confirmPasswordField = new PasswordField("Confirm Password");

        Button forgotButton = new Button("Confirm");

        loginForm.add(header, emailField, passwordField, confirmPasswordField, forgotButton, back);
        loginForm.getStyle().set("flex", "2"); // 2/3 of the height
        loginForm.getStyle().set("width", "500px");
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(loginForm);

        horizontalLayout.getStyle().set("margin", "200px");

        Binder<ForgotPassword> binder = new Binder<>();

        // Define a data class to hold form data


        // Bind form fields to the data class


        // Add validators
        binder.forField(emailField)
                .asRequired("Email is required.")
                .withValidator(email -> email.matches(".+@.+\\..+"), "Invalid email address.")
                .bind(ForgotPassword::getEmail, ForgotPassword::setEmail);

        binder.forField(passwordField)
                .asRequired("Password is required.")
                .bind(ForgotPassword::getPassword, ForgotPassword::setPassword);

        binder.forField(confirmPasswordField)
                .asRequired("Confirm Password is required.")
                .withValidator(confirmPassword -> confirmPassword.equals(passwordField.getValue()), "Passwords do not match.")
                .bind(ForgotPassword::getConfirmPassword, ForgotPassword::setConfirmPassword);


        // Add sections to the main container
        mainContainer.add(leftSection, horizontalLayout);


        // Add the main container to the view
        add(mainContainer);

        Dialog dialogForgotComplete = new Dialog();
//        Dialog dialogCreateSuccess = new Dialog();
        dialogForgotComplete.setCloseOnOutsideClick(false);
//        dialogCreateWrong.setCloseOnOutsideClick(false);

        H1 dialogHeaderF = new H1("Message");
        dialogHeaderF.getStyle().set("text-align", "center");
        Paragraph dialogContentF = new Paragraph("Register Success");
        Button okButtonF = new Button("OK", event -> {
            dialogForgotComplete.close();
            // You can add additional logic if needed after clicking OK
        });
        okButtonF.getStyle().set("text-align", "center");



        dialogForgotComplete.add(dialogHeaderF, dialogContentF, okButtonF);

        forgotButton.addClickListener(event ->{
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

            String email = emailField.getValue();
            String password = passwordField.getValue();
            String confirmPassword =confirmPasswordField.getValue();

            if (binder.isValid()) {

                if (password.equals(confirmPassword)) {
                    formData.add("email", email);
                    formData.add("password", password);

                    System.out.println(formData);

                    Map<String, Object> userList = WebClient.create()
                            .post()
                            .uri("http://localhost:8080/forgot")
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .body(BodyInserters.fromFormData(formData))
                            .retrieve()
                            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                            })
                            .block();

                    System.out.println(userList);

                    if (userList != null) {
                        // Loop through the entries and print each key-value pair
                        for (Map.Entry<String, Object> entry : userList.entrySet()) {
                            System.out.println(entry.getValue());

                            if(entry.getValue().equals("updateComplete")) {
                                dialogForgotComplete.open();
                                UI.getCurrent().navigate(MainView.class);
                            }
                        }
                    } else {
                        System.out.println("Response map is null.");
                    }

                } else {
                    System.out.println("รหัสไม่ตรงกัน");
                }
            }

            else{
                Notification.show("Please correct the errors in the form.");

            }






        });


    }



    private void navigateToMainView(){
        System.out.println("Folk");
        UI.getCurrent().navigate(RegisterPage.class);
    }
}
