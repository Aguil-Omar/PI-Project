package com.esprit.services.utilisateur;
import javafx.scene.control.Alert;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;
import java.util.Random;
import org.json.JSONObject;
public class EmailService {


    private String code;
    private  String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // Generates a number between 100000 and 999999
        return this.code=String.valueOf(code);
    }


    private static final String FROM_EMAIL = "kthiriyoussef09@gmail.com"; // Replace with your email
    private static final String EMAIL_PASSWORD = "plcw yzun jzcb rfsu"; // Replace with your email password
    private static final String SMTP_HOST = "smtp.gmail.com"; // Gmail SMTP host
    private static final String SMTP_PORT = "587"; // Gmail SMTP port

    public void sendVerificationEmail(String toEmail) {

        String code =this.generateVerificationCode();
        // Set up mail server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.port", SMTP_PORT);

        // Create a session with the email server
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, EMAIL_PASSWORD);
            }
        });

        try {
            // Create a new email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject("Verify Your Email Address");
            message.setText("Your verification code is: " + code);

            // Send the email
            Transport.send(message);
            System.out.println("Verification email sent to " + toEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Failed to send verification email.");
        }
    }
    private static final String API_KEY = "fddf665e3bf5426387836ce26e94e79e"; // Replace with your API key from AbstractAPI
    private static final String API_URL = "https://emailvalidation.abstractapi.com/v1/?api_key=" + API_KEY + "&email=";




    public boolean validateEmail(String email) {
        try {
            // Create the URL for the API request
            String url = API_URL + email;

            // Create the HttpRequest
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            // Send the request and get the response
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Debug: Log the response status and body
            System.out.println("Response Status Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());

            // Check if response status is OK
            if (response.statusCode() != 200) {
                System.out.println("Error: Received non-OK response status: " + response.statusCode());
                return false;
            }

            // Parse the JSON response
            JSONObject jsonResponse = new JSONObject(response.body());

            // Debug: Log the entire JSON response
            System.out.println("Parsed JSON Response: " + jsonResponse.toString(2));

            // Check and retrieve the boolean values safely
            boolean isValid = false;
            boolean isDeliverable = false;

            if (jsonResponse.has("is_valid_format")) {
                JSONObject validFormat = jsonResponse.getJSONObject("is_valid_format");  // Get the nested object
                if (validFormat.has("value")) {
                    isValid = validFormat.getBoolean("value");  // Extract the boolean value
                    // Debug: Log the value of 'is_valid_format'
                    System.out.println("is_valid_format.value: " + isValid);
                } else {
                    System.out.println("Warning: 'value' key missing in 'is_valid_format' object.");
                }
            }

            if (jsonResponse.has("deliverability")) {
                String deliverability = jsonResponse.getString("deliverability");
                // Check if deliverability is "DELIVERABLE"
                isDeliverable = "DELIVERABLE".equalsIgnoreCase(deliverability);
                // Debug: Log the value of 'deliverability'
                System.out.println("deliverability: " + deliverability);
            }

            // Debug: Log the final decision on validity and deliverability
            System.out.println("Final Validation Result: " + (isValid && isDeliverable));

            // Return true if both valid and deliverable
            return isValid && isDeliverable;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred while validating the email.");
        }

        return false;  // Return false in case of error
    }

    public String getGenertaedCode(){
        return this.code;
    }
}
