package com.example.serviceuser.Views;

import com.example.serviceuser.pojo.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.StreamResource;
import org.bson.types.Binary;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Route("UserProfile")
public class UserProfile extends Div {
    private H1 name;
    private Image profileImage;
    private Image profileImg;

    public UserProfile() {
        createUI();
    }

    private void createUI() {

        
        // Create main container
        Div mainContainer = new Div();
        mainContainer.getStyle().set("display", "flex");

        mainContainer.getStyle().set("height", "100vh");

        mainContainer.getStyle().set("flex", "1");// 100% of the viewport height

        // Create orange background on the left (1/3 of the height)

        Div leftSection = new Div();
        Image logoImage = new Image("/images/Logo.png", "Logo");
        Button logout = new Button("LogOut");

        logout.getStyle().set("width", "200px");
        logoImage.getStyle().set("width" ,"300px").set("height", "150px");










        Div logo = new Div();

        logo.getStyle().set("text-align", "center");

        logo.add(logoImage, logout);
        leftSection.getStyle().set("background-color", "#5F9DB2");
        leftSection.getStyle().set("flex", "1");
        leftSection.getStyle().set("height", "100%"); // 1/3 of the height
        leftSection.getStyle().set("width", "5%");




        leftSection.add(logo, logout);

        Div rightSection = new Div();

        rightSection.getStyle().set("background-color", "white");
        rightSection.getStyle().set("flex", "3");
        rightSection.getStyle().set("height", "100%");


        Div searchBar = new Div();

        searchBar.getStyle().set("display" , "flex");
        TextField search = new TextField();
        profileImage = new Image();
        search.getStyle().set("margin", "50px").set("width", "800px");
        profileImage.getStyle().set("width", "50px").set("height", "50px").set("border-radius", "50px").set("margin-top", "50px");


        searchBar.add(search, profileImage);


        Div profileBar = new Div();
        profileBar.getStyle().set("display", "flex");

        profileImg = new Image();
        profileImg.getStyle().set("width", "300px").set("height", "300px").set("border-radius", "50%").set("margin" , "50px");
        Div nameBar = new Div();
        name = new H1();

        H6 text = new H6("playList 6");

        nameBar.add(name, text);

        profileBar.add(profileImg, nameBar);


        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setAcceptedFileTypes("image/jpeg", "image/png");

        Button uploadButton = new Button("Upload Image");

        // Create a FormLayout to arrange components
        FormLayout formLayout = new FormLayout();
        formLayout.add(upload, uploadButton);

        // Handle the file upload
        upload.addSucceededListener(event -> {
            try {
                Binary imageData = new Binary(buffer.getInputStream().readAllBytes());
                System.out.println(imageData);

                byte[] imageDataBytes = imageData.getData();

                String hexString = bytesToHex(imageDataBytes);

                // Create data URI
                String dataUri = "data:image/jpeg;base64," + hexString;

                // Print the data URI
                System.out.println(dataUri);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // Save the image data to MongoDB
            // Your logic to save imageData to MongoDB goes here

            // Update the profile image
            profileImg.setSrc(new StreamResource("filename", () -> {
                try {

                    return new ByteArrayInputStream(buffer.getInputStream().readAllBytes());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }));


        });

        uploadButton.addClickListener(event -> {
            // Your logic for additional actions on button click
            try {
                Binary imageData = new Binary(buffer.getInputStream().readAllBytes());
                System.out.println(imageData);


            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });






        rightSection.add(searchBar, profileBar, formLayout);






        // Create login form on the right (2/3 of the height)
//        FormLayout loginForm = new FormLayout();
//        H1 header = new H1("Log in");
//
//        RouterLink forgot = new RouterLink("Forgot Password", ForgotPage.class);
//        header.getStyle().set("text-align", "center");
//        TextField usernameField = new TextField("Username");
//        PasswordField passwordField = new PasswordField("Password");
//        Button loginButton = new Button("Login", event -> {
//            Notification.show("Login button clicked");
//        });
//
//        loginForm.add(header, usernameField, passwordField, loginButton, forgot);
//        forgot.getStyle().set("text-align", "center").set("margin-top", "20px");
//        loginForm.getStyle().set("flex", "4");
//        loginForm.getStyle().set("width", "600px");
//        HorizontalLayout horizontalLayout = new HorizontalLayout();
//        horizontalLayout.add(loginForm);
//
//        horizontalLayout.getStyle().set("margin", "200px");

        // Add sections to the main container
        mainContainer.add(leftSection,rightSection);

        // Add the main container to the view
        add(mainContainer);
        loadPage();


        logout.addClickListener(event ->{
            logout();
        });




    }
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexStringBuilder = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            hexStringBuilder.append(String.format("%02x", b));
        }
        return hexStringBuilder.toString();
    }






    private void loadPage() {
        String key = "token";
        UI.getCurrent().getPage().executeJs("return localStorage.getItem($0)", key)
                .then(String.class, this::fetchUserData);
    }

    private void fetchUserData(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        if(token == null){
            UI.getCurrent().navigate(MainView.class);
        }

        try {
            String jsonResponse = WebClient.builder()
                    .baseUrl("http://localhost:8080")
                    .defaultHeaders(header -> header.addAll(headers))
                    .build()
                    .get()
                    .uri("/me")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println("JSON Response: " + jsonResponse);


            ObjectMapper objectMapper = new ObjectMapper();

            // Convert JSON string to User object using Jackson
            User user = objectMapper.readValue(jsonResponse, User.class);

            // Store the username in a class variable



            System.out.println("Username: " + user.getUsername());
            System.out.println("Email: " + user.getEmail());

            name.setText(user.getUsername());
            profileImage.setSrc(user.getImage());
            profileImg.setSrc(user.getImage());



        } catch (Exception e) {
            // Handle the exception appropriately
            System.err.println("Error fetching user data: " + e.getMessage());
        }
    }

    private void logout() {
        // Remove token from localStorage
        UI.getCurrent().getPage().executeJs("localStorage.removeItem($0)", "token");

        // Redirect to the login page or perform other logout actions
        UI.getCurrent().navigate(MainView.class);
    }




}
